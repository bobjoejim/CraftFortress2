/*    */ package com.skitscape.survivalgames.commands;
/*    */ 
/*    */ import com.skitscape.survivalgames.GameManager;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class GameCount
/*    */   implements SubCommand
/*    */ {
/*    */   public boolean onCommand(Player player, String[] args)
/*    */   {
/* 10 */     player.sendMessage(GameManager.getInstance().getGameCount());
/*    */ 
/* 12 */     return false;
/*    */   }
/*    */ 
/*    */   public String help(Player p)
/*    */   {
/* 17 */     return "/sg setlobbywall - Setings the lobby stats wall";
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.commands.GameCount
 * JD-Core Version:    0.6.0
 */