/*    */ package com.skitscape.survivalgames.util;
/*    */ 
/*    */ import com.skitscape.survivalgames.Game;
/*    */ import com.skitscape.survivalgames.logging.QueueManager;
/*    */ 
/*    */ public class GameReset
/*    */ {
/*    */   int gameid;
/*    */   Game g;
/*    */ 
/*    */   public GameReset(Game g)
/*    */   {
/* 21 */     this.gameid = g.getID();
/* 22 */     this.g = g;
/*    */   }
/*    */ 
/*    */   public void resetArena() {
/* 26 */     this.g.setRBStatus("GameReset");
/* 27 */     QueueManager.getInstance().rollback(this, this.gameid);
/*    */   }
/*    */ 
/*    */   public void rollbackFinishedCallback()
/*    */   {
/* 32 */     this.g.resetCallback();
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.util.GameReset
 * JD-Core Version:    0.6.0
 */