/*    */ package com.skitscape.survivalgames;
/*    */ 
/*    */ import org.bukkit.Location;
/*    */ 
/*    */ public class Arena
/*    */ {
/*    */   Location min;
/*    */   Location max;
/*    */ 
/*    */   public Arena(Location min, Location max)
/*    */   {
/* 13 */     this.max = max;
/* 14 */     this.min = min;
/*    */   }
/*    */ 
/*    */   public boolean containsBlock(Location v)
/*    */   {
/* 19 */     if (v.getWorld() != this.min.getWorld())
/* 20 */       return false;
/* 21 */     double x = v.getX();
/* 22 */     double y = v.getY();
/* 23 */     double z = v.getZ();
/*    */ 
/* 26 */     return (x >= this.min.getBlockX()) && (x < this.max.getBlockX() + 1) && 
/* 25 */       (y >= this.min.getBlockY()) && (y < this.max.getBlockY() + 1) && 
/* 26 */       (z >= this.min.getBlockZ()) && (z < this.max.getBlockZ() + 1);
/*    */   }
/*    */ 
/*    */   public Location getMax()
/*    */   {
/* 32 */     return this.max;
/*    */   }
/*    */ 
/*    */   public Location getMin() {
/* 36 */     return this.min;
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.Arena
 * JD-Core Version:    0.6.0
 */