/*    */ package com.skitscape.survivalgames.commands;
/*    */ 
/*    */ import com.skitscape.survivalgames.stats.StatsWallManager;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class SetStatsWall
/*    */   implements SubCommand
/*    */ {
/*    */   public boolean onCommand(Player player, String[] args)
/*    */   {
/* 13 */     StatsWallManager.getInstance().setStatsSignsFromSelection(player);
/* 14 */     return false;
/*    */   }
/*    */ 
/*    */   public String help()
/*    */   {
/* 19 */     return "/sg setstatswall - Sets the stats wall";
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.commands.SetStatsWall
 * JD-Core Version:    0.6.0
 */