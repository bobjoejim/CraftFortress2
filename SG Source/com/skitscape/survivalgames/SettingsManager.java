/*     */ package com.skitscape.survivalgames;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.configuration.file.FileConfigurationOptions;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ public class SettingsManager
/*     */ {
/*  20 */   private static SettingsManager instance = new SettingsManager();
/*     */   private static Plugin p;
/*     */   private FileConfiguration spawns;
/*     */   private FileConfiguration system;
/*     */   private File f;
/*     */   private File f2;
/*     */ 
/*     */   public static SettingsManager getInstance()
/*     */   {
/*  33 */     return instance;
/*     */   }
/*     */ 
/*     */   public void setup(Plugin p)
/*     */   {
/*  38 */     p = p;
/*     */ 
/*  40 */     p.getConfig().options().copyDefaults(true);
/*  41 */     p.saveDefaultConfig();
/*     */ 
/*  43 */     this.f = new File(p.getDataFolder(), "spawns.yml");
/*  44 */     this.f2 = new File(p.getDataFolder(), "system.yml");
/*     */     try {
/*  46 */       if (!this.f.exists())
/*  47 */         this.f.createNewFile();
/*  48 */       if (!this.f2.exists())
/*  49 */         this.f2.createNewFile(); 
/*     */     } catch (Exception localException) {
/*     */     }
/*  51 */     reloadSystem();
/*  52 */     saveSystemConfig();
/*  53 */     reloadSystem();
/*  54 */     reloadSpawns();
/*  55 */     saveSpawns();
/*  56 */     reloadSpawns();
/*  57 */     System.out.println(SurvivalGames.config_version + "     " + p.getConfig().getInt("config-version"));
/*  58 */     if (p.getConfig().getInt("config-version") == SurvivalGames.config_version) {
/*  59 */       SurvivalGames.config_todate = true;
/*     */     }
/*  61 */     SurvivalGames.config_todate = true;
/*     */   }
/*     */ 
/*     */   public void set(String arg0, Object arg1) {
/*  65 */     p.getConfig().set(arg0, arg1);
/*     */   }
/*     */ 
/*     */   public FileConfiguration getConfig() {
/*  69 */     return p.getConfig();
/*     */   }
/*     */ 
/*     */   public FileConfiguration getSystemConfig() {
/*  73 */     return this.system;
/*     */   }
/*     */ 
/*     */   public FileConfiguration getSpawns() {
/*  77 */     return this.spawns;
/*     */   }
/*     */ 
/*     */   public void saveConfig()
/*     */   {
/*     */   }
/*     */ 
/*     */   public static World getGameWorld(int game) {
/*  85 */     if (getInstance().getSystemConfig().getString("sg-system.arenas." + game + ".world") == null) {
/*  86 */       LobbyManager.getInstance().error(true);
/*  87 */       return null;
/*     */     }
/*     */ 
/*  90 */     return p.getServer().getWorld(getInstance().getSystemConfig().getString("sg-system.arenas." + game + ".world"));
/*     */   }
/*     */ 
/*     */   public void reloadSpawns() {
/*  94 */     this.spawns = YamlConfiguration.loadConfiguration(this.f);
/*     */   }
/*     */ 
/*     */   public void reloadSystem() {
/*  98 */     this.system = YamlConfiguration.loadConfiguration(this.f2);
/*     */   }
/*     */ 
/*     */   public void saveSystemConfig() {
/*     */     try {
/* 103 */       this.system.save(this.f2);
/*     */     }
/*     */     catch (IOException e) {
/* 106 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void saveSpawns() {
/*     */     try {
/* 112 */       this.spawns.save(this.f);
/*     */     }
/*     */     catch (IOException e) {
/* 115 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getSpawnCount(int gameid) {
/* 120 */     return this.spawns.getInt("spawns." + gameid + ".count");
/*     */   }
/*     */ 
/*     */   public HashMap<String, Object> getGameFlags(int a)
/*     */   {
/* 127 */     HashMap flags = new HashMap();
/*     */ 
/* 129 */     flags.put("AUTOSTART_PLAYERS", Integer.valueOf(this.system.getInt("sg-system.arenas." + a + ".flags.autostart")));
/* 130 */     flags.put("AUTOSTART_VOTE", Integer.valueOf(this.system.getInt("sg-system.arenas." + a + ".flags.vote")));
/* 131 */     flags.put("ENDGAME_ENABLED", Boolean.valueOf(this.system.getBoolean("sg-system.arenas." + a + ".flags.endgame-enabled")));
/* 132 */     flags.put("ENDGAME_PLAYERS", Integer.valueOf(this.system.getInt("sg-system.arenas." + a + ".flags.endgame-players")));
/*     */ 
/* 134 */     return flags;
/*     */   }
/*     */ 
/*     */   public void saveGameFlags(HashMap<String, Object> flags, int a)
/*     */   {
/* 139 */     this.system.set("sg-system.arenas." + a + ".flags.autostart", flags.get("AUTOSTART_PLAYERS"));
/* 140 */     this.system.set("sg-system.arenas." + a + ".flags.vote", flags.get("AUTOSTART_VOTE"));
/* 141 */     this.system.set("sg-system.arenas." + a + ".flags.autostart", flags.get("AUTOSTART_PLAYERS"));
/* 142 */     this.system.set("sg-system.arenas." + a + ".flags.autostart", flags.get("AUTOSTART_PLAYERS"));
/*     */   }
/*     */ 
/*     */   public Location getLobbySpawn()
/*     */   {
/* 149 */     return new Location(Bukkit.getWorld(this.system.getString("sg-system.lobby.spawn.world")), 
/* 150 */       this.system.getInt("sg-system.lobby.spawn.x"), 
/* 151 */       this.system.getInt("sg-system.lobby.spawn.y"), 
/* 152 */       this.system.getInt("sg-system.lobby.spawn.z"));
/*     */   }
/*     */ 
/*     */   public void setLobbySpawn(Location l) {
/* 156 */     this.system.set("sg-system.lobby.spawn.world", l.getWorld().getName());
/* 157 */     this.system.set("sg-system.lobby.spawn.x", Integer.valueOf(l.getBlockX()));
/* 158 */     this.system.set("sg-system.lobby.spawn.y", Integer.valueOf(l.getBlockY()));
/* 159 */     this.system.set("sg-system.lobby.spawn.z", Integer.valueOf(l.getBlockZ()));
/*     */   }
/*     */ 
/*     */   public Location getSpawnPoint(int gameid, int spawnid) {
/* 163 */     return new Location(getGameWorld(gameid), 
/* 164 */       this.spawns.getInt("spawns." + gameid + "." + spawnid + ".x"), 
/* 165 */       this.spawns.getInt("spawns." + gameid + "." + spawnid + ".y"), 
/* 166 */       this.spawns.getInt("spawns." + gameid + "." + spawnid + ".z"));
/*     */   }
/*     */   public void setSpawn(int gameid, int spawnid, Vector v) {
/* 169 */     this.spawns.set("spawns." + gameid + "." + spawnid + ".x", Integer.valueOf(v.getBlockX()));
/* 170 */     this.spawns.set("spawns." + gameid + "." + spawnid + ".y", Integer.valueOf(v.getBlockY()));
/* 171 */     this.spawns.set("spawns." + gameid + "." + spawnid + ".z", Integer.valueOf(v.getBlockZ()));
/* 172 */     if (spawnid > this.spawns.getInt("spawns." + gameid + ".count"))
/* 173 */       this.spawns.set("spawns." + gameid + ".count", Integer.valueOf(spawnid));
/*     */     try
/*     */     {
/* 176 */       this.spawns.save(this.f);
/*     */     }
/*     */     catch (IOException localIOException) {
/*     */     }
/* 180 */     GameManager.getInstance().getGame(gameid).addSpawn();
/*     */   }
/*     */ 
/*     */   public static String getSqlPrefix()
/*     */   {
/* 186 */     return getInstance().getConfig().getString("sql.prefix");
/*     */   }
/*     */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.SettingsManager
 * JD-Core Version:    0.6.0
 */