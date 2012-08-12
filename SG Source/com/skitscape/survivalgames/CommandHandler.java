/*     */ package com.skitscape.survivalgames;
/*     */ 
/*     */ import com.skitscape.survivalgames.commands.CreateArena;
/*     */ import com.skitscape.survivalgames.commands.DelArena;
/*     */ import com.skitscape.survivalgames.commands.Disable;
/*     */ import com.skitscape.survivalgames.commands.Enable;
/*     */ import com.skitscape.survivalgames.commands.Flag;
/*     */ import com.skitscape.survivalgames.commands.ForceStart;
/*     */ import com.skitscape.survivalgames.commands.GameCount;
/*     */ import com.skitscape.survivalgames.commands.Join;
/*     */ import com.skitscape.survivalgames.commands.Leave;
/*     */ import com.skitscape.survivalgames.commands.LeaveQueue;
/*     */ import com.skitscape.survivalgames.commands.ListPlayers;
/*     */ import com.skitscape.survivalgames.commands.ResetSpawns;
/*     */ import com.skitscape.survivalgames.commands.SetLobbySpawn;
/*     */ import com.skitscape.survivalgames.commands.SetLobbyWall;
/*     */ import com.skitscape.survivalgames.commands.SetSpawn;
/*     */ import com.skitscape.survivalgames.commands.Spectate;
/*     */ import com.skitscape.survivalgames.commands.Start;
/*     */ import com.skitscape.survivalgames.commands.SubCommand;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Vector;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandExecutor;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.PluginDescriptionFile;
/*     */ 
/*     */ public class CommandHandler
/*     */   implements CommandExecutor
/*     */ {
/*     */   private Plugin plugin;
/*     */   private HashMap<String, SubCommand> commands;
/*     */ 
/*     */   public CommandHandler(Plugin plugin)
/*     */   {
/*  45 */     this.plugin = plugin;
/*  46 */     this.commands = new HashMap();
/*  47 */     loadCommands();
/*     */   }
/*     */ 
/*     */   private void loadCommands()
/*     */   {
/*  52 */     this.commands.put("createarena", new CreateArena());
/*  53 */     this.commands.put("join", new Join());
/*  54 */     this.commands.put("setlobbywall", new SetLobbyWall());
/*  55 */     this.commands.put("setspawn", new SetSpawn());
/*  56 */     this.commands.put("getcount", new GameCount());
/*  57 */     this.commands.put("disable", new Disable());
/*  58 */     this.commands.put("start", new ForceStart());
/*  59 */     this.commands.put("enable", new Enable());
/*  60 */     this.commands.put("vote", new Start());
/*  61 */     this.commands.put("leave", new Leave());
/*  62 */     this.commands.put("setlobbyspawn", new SetLobbySpawn());
/*  63 */     this.commands.put("resetspawns", new ResetSpawns());
/*  64 */     this.commands.put("delarena", new DelArena());
/*  65 */     this.commands.put("flag", new Flag());
/*  66 */     this.commands.put("spectate", new Spectate());
/*  67 */     this.commands.put("lq", new LeaveQueue());
/*  68 */     this.commands.put("leavequeue", new LeaveQueue());
/*  69 */     this.commands.put("list", new ListPlayers());
/*     */   }
/*     */ 
/*     */   public boolean onCommand(CommandSender sender, Command cmd1, String commandLabel, String[] args)
/*     */   {
/*  75 */     String cmd = cmd1.getName();
/*  76 */     PluginDescriptionFile pdfFile = this.plugin.getDescription();
/*     */ 
/*  78 */     Player player = null;
/*  79 */     if ((sender instanceof Player)) {
/*  80 */       player = (Player)sender;
/*     */     }
/*     */     else {
/*  83 */       System.out.println("Only ingame players can use SurvivalGames commands");
/*  84 */       return true;
/*     */     }
/*     */ 
/*  87 */     if (!SurvivalGames.config_todate) {
/*  88 */       player.sendMessage("Your config file is outdated. Please reset(DELETE) your config file!");
/*  89 */       return true;
/*     */     }
/*     */ 
/*  92 */     if (!SurvivalGames.dbcon) {
/*  93 */       player.sendMessage("Could not connect to the database. Plugin disabled");
/*  94 */       return true;
/*     */     }
/*     */ 
/*  98 */     if (cmd.equalsIgnoreCase("survivalgames")) {
/*  99 */       if ((args == null) || (args.length < 1)) {
/* 100 */         player.sendMessage(ChatColor.GOLD + ChatColor.BOLD + "Survival Games - Double0negative" + ChatColor.RESET + ChatColor.YELLOW + " Version: " + pdfFile.getVersion());
/* 101 */         player.sendMessage(ChatColor.GOLD + "Type /sg help for help");
/*     */ 
/* 103 */         return true;
/*     */       }
/* 105 */       if (args[0].equalsIgnoreCase("help")) {
/* 106 */         help(player);
/* 107 */         return true;
/*     */       }
/* 109 */       String sub = args[0];
/*     */ 
/* 111 */       Vector l = new Vector();
/* 112 */       l.addAll(Arrays.asList(args));
/* 113 */       l.remove(0);
/* 114 */       args = (String[])l.toArray(new String[0]);
/* 115 */       if (!this.commands.containsKey(sub)) {
/* 116 */         player.sendMessage(ChatColor.RED + "Command dosent exist.");
/* 117 */         player.sendMessage(ChatColor.DARK_AQUA + "Type /sg help for help");
/* 118 */         return true;
/*     */       }
/*     */       try
/*     */       {
/* 122 */         ((SubCommand)this.commands.get(sub)).onCommand(player, args); } catch (Exception e) {
/* 123 */         e.printStackTrace(); player.sendMessage(ChatColor.RED + "An error occured while executing the command. Check the      console"); player.sendMessage(ChatColor.BLUE + "Type /sg help for help");
/*     */       }
/* 125 */       return true;
/*     */     }
/* 127 */     return false;
/*     */   }
/*     */ 
/*     */   public void help(Player p) {
/* 131 */     p.sendMessage("/sg <command> <args>");
/* 132 */     for (SubCommand v : this.commands.values())
/* 133 */       p.sendMessage(ChatColor.AQUA + v.help(p));
/*     */   }
/*     */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.CommandHandler
 * JD-Core Version:    0.6.0
 */