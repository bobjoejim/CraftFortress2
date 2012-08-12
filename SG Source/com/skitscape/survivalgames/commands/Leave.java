/*    */ package com.skitscape.survivalgames.commands;
/*    */ 
/*    */ import com.skitscape.survivalgames.GameManager;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class Leave
/*    */   implements SubCommand
/*    */ {
/*    */   public boolean onCommand(Player player, String[] args)
/*    */   {
/* 11 */     if (GameManager.getInstance().getPlayerGameId(player) == -1) {
/* 12 */       player.sendMessage(ChatColor.RED + "Not in a game!");
/*    */     }
/*    */     else {
/* 15 */       GameManager.getInstance().removePlayer(player, false);
/*    */     }
/* 17 */     return true;
/*    */   }
/*    */ 
/*    */   public String help(Player p)
/*    */   {
/* 22 */     return "/sg leave - Leaves the game";
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.commands.Leave
 * JD-Core Version:    0.6.0
 */