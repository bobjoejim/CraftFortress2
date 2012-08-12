/*    */ package com.skitscape.survivalgames.commands;
/*    */ 
/*    */ import com.skitscape.survivalgames.Game;
/*    */ import com.skitscape.survivalgames.GameManager;
/*    */ import com.skitscape.survivalgames.SettingsManager;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class Spectate
/*    */   implements SubCommand
/*    */ {
/*    */   public boolean onCommand(Player player, String[] args)
/*    */   {
/* 14 */     if ((!player.hasPermission("sg.arena.spectate")) && (!player.isOp())) {
/* 15 */       player.sendMessage(ChatColor.RED + "No Permission");
/* 16 */       return true;
/*    */     }
/*    */ 
/* 19 */     if (args.length == 0) {
/* 20 */       if (GameManager.getInstance().isSpectator(player)) {
/* 21 */         GameManager.getInstance().removeSpectator(player);
/* 22 */         return true;
/*    */       }
/*    */ 
/* 25 */       player.sendMessage(ChatColor.RED + "You are not spectating a game. Use /sg spectate <arenaid> to spectate!");
/* 26 */       return true;
/*    */     }
/*    */ 
/* 29 */     if (SettingsManager.getInstance().getSpawnCount(Integer.parseInt(args[0])) == 0) {
/* 30 */       player.sendMessage(ChatColor.RED + "No spawns set!");
/* 31 */       return true;
/*    */     }
/* 33 */     if (GameManager.getInstance().isPlayerActive(player)) {
/* 34 */       player.sendMessage(ChatColor.RED + "Cannot spectate while ingame!");
/* 35 */       return true;
/*    */     }
/* 37 */     GameManager.getInstance().getGame(Integer.parseInt(args[0])).addSpectator(player);
/* 38 */     player.sendMessage(ChatColor.GREEN + "You are now spectating! /sg spectate again to return to lobby");
/* 39 */     return true;
/*    */   }
/*    */ 
/*    */   public String help(Player p)
/*    */   {
/* 44 */     return "/sg sepctate <id> - Allows you to spectate a game";
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.commands.Spectate
 * JD-Core Version:    0.6.0
 */