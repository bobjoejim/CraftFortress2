/*    */ package com.skitscape.survivalgames.commands;
/*    */ 
/*    */ import com.skitscape.survivalgames.GameManager;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class CreateArena
/*    */   implements SubCommand
/*    */ {
/*    */   public boolean onCommand(Player player, String[] args)
/*    */   {
/* 15 */     if ((!player.hasPermission("sg.arena.create")) && (!player.isOp())) {
/* 16 */       player.sendMessage(ChatColor.RED + "No Permission");
/* 17 */       return true;
/*    */     }
/* 19 */     GameManager.getInstance().createArenaFromSelection(player);
/*    */ 
/* 21 */     return true;
/*    */   }
/*    */ 
/*    */   public String help(Player p)
/*    */   {
/* 26 */     return "/sg createarena - Creates a new arena in the current world edit selection";
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.commands.CreateArena
 * JD-Core Version:    0.6.0
 */