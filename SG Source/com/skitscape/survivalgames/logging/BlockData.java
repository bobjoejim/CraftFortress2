/*    */ package com.skitscape.survivalgames.logging;
/*    */ 
/*    */ public class BlockData
/*    */ {
/*    */   private String world;
/*    */   private int previd;
/*    */   private int newid;
/*    */   private byte prevdata;
/*    */   private byte newdata;
/*    */   private int x;
/*    */   private int y;
/*    */   private int z;
/*    */   private int gameid;
/*    */ 
/*    */   public BlockData(int gameid, String world, int previd, byte prevdata, int newid, byte newdata, int x, int y, int z)
/*    */   {
/* 24 */     this.gameid = gameid;
/* 25 */     this.world = world;
/* 26 */     this.previd = previd;
/* 27 */     this.prevdata = prevdata;
/* 28 */     this.newid = newid;
/* 29 */     this.newdata = newdata;
/* 30 */     this.x = x;
/* 31 */     this.y = y;
/* 32 */     this.z = z;
/*    */   }
/*    */ 
/*    */   public int getGameId()
/*    */   {
/* 37 */     return this.gameid;
/*    */   }
/*    */ 
/*    */   public String getWorld() {
/* 41 */     return this.world;
/*    */   }
/*    */ 
/*    */   public byte getPrevdata() {
/* 45 */     return this.prevdata;
/*    */   }
/*    */ 
/*    */   public byte getNewdata() {
/* 49 */     return this.newdata;
/*    */   }
/*    */ 
/*    */   public int getPrevid() {
/* 53 */     return this.previd;
/*    */   }
/*    */ 
/*    */   public int getNewid() {
/* 57 */     return this.newid;
/*    */   }
/*    */ 
/*    */   public int getX() {
/* 61 */     return this.x;
/*    */   }
/*    */ 
/*    */   public int getY() {
/* 65 */     return this.y;
/*    */   }
/*    */ 
/*    */   public int getZ() {
/* 69 */     return this.z;
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.logging.BlockData
 * JD-Core Version:    0.6.0
 */