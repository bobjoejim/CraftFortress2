/*    */ package com.skitscape.survivalgames.commands;
/*    */ 
/*    */ import com.skitscape.survivalgames.Game;
/*    */ import com.skitscape.survivalgames.GameManager;
/*    */ import com.skitscape.survivalgames.SettingsManager;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class Flag
/*    */   implements SubCommand
/*    */ {
/*    */   public boolean onCommand(Player player, String[] args)
/*    */   {
/* 15 */     if (!player.hasPermission("sg.arena.flag")) {
/* 16 */       player.sendMessage(ChatColor.RED + "No Permission");
/* 17 */       return true;
/*    */     }
/* 19 */     if (args.length < 2) {
/* 20 */       player.sendMessage(help(player));
/* 21 */       return true;
/*    */     }
/*    */ 
/* 24 */     Game g = GameManager.getInstance().getGame(Integer.parseInt(args[0]));
/*    */ 
/* 26 */     if (g == null) {
/* 27 */       player.sendMessage(ChatColor.RED + "Arena does not exist!");
/* 28 */       return true;
/*    */     }
/*    */ 
/* 31 */     if (args[1].equals("vip")) {
/*    */       try {
/* 33 */         SettingsManager.getInstance().getSystemConfig().set("sg-system.arenas." + args[2] + ".vip", args[3]);
/* 34 */         player.sendMessage(ChatColor.GREEN + "Vip" + (args[3].equalsIgnoreCase("true") ? "enabled" : "disabled") + " on arena " + args[0]);
/* 35 */         return true; } catch (Exception e) {
/* 36 */         player.sendMessage(help(player)); return true;
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 42 */     return false;
/*    */   }
/*    */ 
/*    */   public String help(Player p)
/*    */   {
/* 47 */     return "/sg flag <id> <flag> <value> - Settings an arena specific setting";
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.commands.Flag
 * JD-Core Version:    0.6.0
 */