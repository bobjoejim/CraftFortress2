/*    */ package com.skitscape.survivalgames.commands;
/*    */ 
/*    */ import com.skitscape.survivalgames.SettingsManager;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class ResetSpawns
/*    */   implements SubCommand
/*    */ {
/*    */   public boolean onCommand(Player player, String[] args)
/*    */   {
/* 12 */     if ((!player.hasPermission("sg.arena.resetspawns")) && (!player.isOp())) {
/* 13 */       player.sendMessage(ChatColor.RED + "No Permission");
/* 14 */       return true;
/*    */     }
/* 16 */     SettingsManager.getInstance().getSpawns().set("spawns." + Integer.parseInt(args[0]), null);
/* 17 */     return true;
/*    */   }
/*    */ 
/*    */   public String help(Player p)
/*    */   {
/* 22 */     return "/sg resetspawns <id> - resets spawns for an arena";
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.commands.ResetSpawns
 * JD-Core Version:    0.6.0
 */