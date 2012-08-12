/*     */ package com.skitscape.survivalgames;
/*     */ 
/*     */ import com.sk89q.worldedit.bukkit.WorldEditPlugin;
/*     */ import com.skitscape.survivalgames.Events.BreakEvent;
/*     */ import com.skitscape.survivalgames.Events.ChestReplaceEvent;
/*     */ import com.skitscape.survivalgames.Events.CommandCatch;
/*     */ import com.skitscape.survivalgames.Events.DeathEvent;
/*     */ import com.skitscape.survivalgames.Events.JoinEvent;
/*     */ import com.skitscape.survivalgames.Events.KeepLobbyLoadedEvent;
/*     */ import com.skitscape.survivalgames.Events.LogoutEvent;
/*     */ import com.skitscape.survivalgames.Events.MoveEvent;
/*     */ import com.skitscape.survivalgames.Events.PlaceEvent;
/*     */ import com.skitscape.survivalgames.Events.SignClickEvent;
/*     */ import com.skitscape.survivalgames.Events.SpectatorEvents;
/*     */ import com.skitscape.survivalgames.Events.TeleportEvent;
/*     */ import com.skitscape.survivalgames.logging.LoggingManager;
/*     */ import com.skitscape.survivalgames.logging.QueueManager;
/*     */ import com.skitscape.survivalgames.stats.StatsManager;
/*     */ import com.skitscape.survivalgames.util.ChestRatioStorage;
/*     */ import com.skitscape.survivalgames.util.DatabaseManager;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.command.PluginCommand;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.PluginDescriptionFile;
/*     */ import org.bukkit.plugin.PluginManager;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import org.bukkit.scheduler.BukkitScheduler;
/*     */ 
/*     */ public class SurvivalGames extends JavaPlugin
/*     */ {
/*     */   Logger logger;
/*     */   private static File datafolder;
/*  31 */   private static boolean active = false;
/*  32 */   public static boolean dbcon = false;
/*  33 */   public static boolean config_todate = false;
/*  34 */   public static int config_version = 1;
/*     */ 
/*  36 */   public static List<String> auth = Arrays.asList(new String[] { "Double0negative", "iMalo", "beechboy2000", "Medic0987", "alex_markey", "skitscape", "AntVenom", "YoshiGenius", "pimpinpsp", "WinryR", "Jazed2011", 
/*  37 */     "KiwiPantz", "blackracoon", "CuppingCakes", "4rr0ws", "Fawdz", "Timothy13", "rich91", "ModernPrestige", "Snowpool", "egoshk", "puppyYo", "nickm140", "Jeroon35", "chaseoes" });
/*     */ 
/*  39 */   SurvivalGames p = this;
/*     */ 
/*  41 */   public void onDisable() { active = false;
/*  42 */     PluginDescriptionFile pdfFile = this.p.getDescription();
/*  43 */     SettingsManager.getInstance().saveSpawns();
/*  44 */     SettingsManager.getInstance().saveSystemConfig();
/*  45 */     for (Game g : GameManager.getInstance().getGames()) {
/*  46 */       g.disable();
/*     */     }
/*     */ 
/*  49 */     this.logger.info("The " + pdfFile.getName() + " version " + pdfFile.getVersion() + " has now been disabled and reset");
/*     */   }
/*     */ 
/*     */   public void onEnable()
/*     */   {
/*  54 */     Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Startup(), 10L);
/*     */     try {
/*  56 */       new Metrics(this).start();
/*     */     }
/*     */     catch (IOException e) {
/*  59 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setCommands()
/*     */   {
/* 132 */     getCommand("survivalgames").setExecutor(new CommandHandler(this.p));
/*     */   }
/*     */ 
/*     */   public static File getPluginDataFolder()
/*     */   {
/* 139 */     return datafolder;
/*     */   }
/*     */ 
/*     */   public static boolean isActive() {
/* 143 */     return active;
/*     */   }
/*     */ 
/*     */   public WorldEditPlugin getWorldEdit() {
/* 147 */     Plugin worldEdit = getServer().getPluginManager().getPlugin("WorldEdit");
/* 148 */     if ((worldEdit instanceof WorldEditPlugin)) {
/* 149 */       return (WorldEditPlugin)worldEdit;
/*     */     }
/* 151 */     return null;
/*     */   }
/*     */ 
/*     */   class Startup
/*     */     implements Runnable
/*     */   {
/*     */     Startup()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/*  68 */       SurvivalGames.this.logger = SurvivalGames.this.p.getLogger();
/*  69 */       SurvivalGames.active = true;
/*  70 */       SurvivalGames.datafolder = SurvivalGames.this.p.getDataFolder();
/*  71 */       PluginManager pm = SurvivalGames.this.getServer().getPluginManager();
/*  72 */       SurvivalGames.this.setCommands();
/*     */ 
/*  75 */       SettingsManager.getInstance().setup(SurvivalGames.this.p);
/*     */       try
/*     */       {
/*  78 */         FileConfiguration c = SettingsManager.getInstance().getConfig();
/*  79 */         if ((c.getBoolean("stats.enabled")) || (c.getBoolean("logging.usesql")))
/*  80 */           DatabaseManager.getInstance().setup(SurvivalGames.this.p);
/*  81 */         QueueManager.getInstance().setup(SurvivalGames.this.p, c.getBoolean("logging.usesql"));
/*  82 */         StatsManager.getInstance().setup(SurvivalGames.this.p, c.getBoolean("stats.enabled"));
/*  83 */         SurvivalGames.dbcon = true;
/*     */       }
/*     */       catch (Exception e) {
/*  86 */         SurvivalGames.dbcon = false;
/*  87 */         e.printStackTrace();
/*  88 */         SurvivalGames.this.logger.severe("!!!Failed to connect to the database. Please check the settings and try again!!!");
/*  89 */         return;
/*     */       }
/*     */       finally {
/*  92 */         GameManager.getInstance().setup(SurvivalGames.this.p);
/*     */ 
/*  95 */         LobbyManager.getInstance().setup(SurvivalGames.this.p);
/*     */       }
/*     */ 
/*  98 */       ChestRatioStorage.getInstance().setup();
/*     */ 
/* 101 */       pm.registerEvents(new PlaceEvent(), SurvivalGames.this.p);
/* 102 */       pm.registerEvents(new BreakEvent(), SurvivalGames.this.p);
/* 103 */       pm.registerEvents(new DeathEvent(), SurvivalGames.this.p);
/* 104 */       pm.registerEvents(new MoveEvent(), SurvivalGames.this.p);
/* 105 */       pm.registerEvents(new CommandCatch(), SurvivalGames.this.p);
/* 106 */       pm.registerEvents(new SignClickEvent(), SurvivalGames.this.p);
/* 107 */       pm.registerEvents(new ChestReplaceEvent(), SurvivalGames.this.p);
/* 108 */       pm.registerEvents(new LogoutEvent(), SurvivalGames.this.p);
/* 109 */       pm.registerEvents(new JoinEvent(SurvivalGames.this.p), SurvivalGames.this.p);
/* 110 */       pm.registerEvents(new TeleportEvent(), SurvivalGames.this.p);
/* 111 */       pm.registerEvents(LoggingManager.getInstance(), SurvivalGames.this.p);
/* 112 */       pm.registerEvents(new SpectatorEvents(), SurvivalGames.this.p);
/* 113 */       pm.registerEvents(new KeepLobbyLoadedEvent(), SurvivalGames.this.p);
/*     */ 
/* 116 */       for (Player p : Bukkit.getOnlinePlayers())
/* 117 */         if (GameManager.getInstance().getBlockGameId(p.getLocation()) != -1)
/* 118 */           p.teleport(SettingsManager.getInstance().getLobbySpawn());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.SurvivalGames
 * JD-Core Version:    0.6.0
 */