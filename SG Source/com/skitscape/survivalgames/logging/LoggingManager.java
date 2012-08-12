/*     */ package com.skitscape.survivalgames.logging;
/*     */ 
/*     */ import com.skitscape.survivalgames.Game.GameMode;
/*     */ import com.skitscape.survivalgames.GameManager;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.block.BlockBreakEvent;
/*     */ import org.bukkit.event.block.BlockBurnEvent;
/*     */ import org.bukkit.event.block.BlockFadeEvent;
/*     */ import org.bukkit.event.block.BlockGrowEvent;
/*     */ import org.bukkit.event.block.BlockIgniteEvent;
/*     */ import org.bukkit.event.block.BlockPistonExtendEvent;
/*     */ import org.bukkit.event.block.BlockPlaceEvent;
/*     */ import org.bukkit.event.block.LeavesDecayEvent;
/*     */ import org.bukkit.event.entity.EntityExplodeEvent;
/*     */ 
/*     */ public class LoggingManager
/*     */   implements Listener
/*     */ {
/*  32 */   public static HashMap<String, Integer> i = new HashMap();
/*     */ 
/*  34 */   private static LoggingManager instance = new LoggingManager();
/*     */ 
/*     */   private LoggingManager()
/*     */   {
/*  40 */     i.put("BCHANGE", Integer.valueOf(1));
/*  41 */     i.put("BPLACE", Integer.valueOf(1));
/*  42 */     i.put("BFADE", Integer.valueOf(1));
/*  43 */     i.put("BBLOW", Integer.valueOf(1));
/*  44 */     i.put("BSTARTFIRE", Integer.valueOf(1));
/*  45 */     i.put("BBURN", Integer.valueOf(1));
/*  46 */     i.put("BREDSTONE", Integer.valueOf(1));
/*  47 */     i.put("LDECAY", Integer.valueOf(1));
/*  48 */     i.put("BPISTION", Integer.valueOf(1));
/*     */   }
/*     */ 
/*     */   public static LoggingManager getInstance()
/*     */   {
/*  54 */     return instance;
/*     */   }
/*     */ 
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void blockChanged(BlockBreakEvent e) {
/*  60 */     if (e.isCancelled()) return;
/*  61 */     logBlockDestoryed(e.getBlock());
/*  62 */     i.put("BCHANGE", Integer.valueOf(((Integer)i.get("BCHANGE")).intValue() + 1));
/*     */   }
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void blockChanged(BlockPlaceEvent e) {
/*  67 */     if (e.isCancelled()) return;
/*     */ 
/*  69 */     logBlockCreated(e.getBlock());
/*  70 */     i.put("BPLACE", Integer.valueOf(((Integer)i.get("BPLACE")).intValue() + 1));
/*     */   }
/*     */ 
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void blockChanged(BlockFadeEvent e)
/*     */   {
/*  81 */     if (e.isCancelled()) return;
/*     */ 
/*  83 */     logBlockDestoryed(e.getBlock());
/*  84 */     i.put("BFADE", Integer.valueOf(((Integer)i.get("BFADE")).intValue() + 1));
/*     */   }
/*     */ 
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void blockChange(EntityExplodeEvent e)
/*     */   {
/*  92 */     if (e.isCancelled()) return;
/*     */ 
/*  94 */     for (Block b : e.blockList()) {
/*  95 */       logBlockDestoryed(b);
/*     */     }
/*     */ 
/* 100 */     i.put("BBLOW", Integer.valueOf(((Integer)i.get("BBLOW")).intValue() + 1));
/*     */   }
/*     */ 
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void blockChange(BlockIgniteEvent e) {
/* 106 */     if (e.isCancelled()) return;
/*     */ 
/* 108 */     logBlockCreated(e.getBlock());
/* 109 */     i.put("BSTARTFIRE", Integer.valueOf(((Integer)i.get("BSTARTFIRE")).intValue() + 1));
/*     */   }
/*     */ 
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void blockChanged(BlockBurnEvent e)
/*     */   {
/* 117 */     if (e.isCancelled()) return;
/*     */ 
/* 119 */     logBlockDestoryed(e.getBlock());
/* 120 */     i.put("BBURN", Integer.valueOf(((Integer)i.get("BBURN")).intValue() + 1));
/*     */   }
/*     */ 
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void blockChanged(BlockGrowEvent e)
/*     */   {
/* 127 */     if (e.isCancelled()) return;
/*     */ 
/* 129 */     logBlockCreated(e.getBlock());
/*     */   }
/*     */ 
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void blockChanged(LeavesDecayEvent e)
/*     */   {
/* 149 */     if (e.isCancelled()) return;
/*     */ 
/* 151 */     logBlockDestoryed(e.getBlock());
/* 152 */     i.put("LDECAY", Integer.valueOf(((Integer)i.get("LDECAY")).intValue() + 1));
/*     */   }
/*     */ 
/*     */   public void blockChange(BlockPistonExtendEvent e)
/*     */   {
/* 165 */     if (e.isCancelled()) return;
/*     */ 
/* 167 */     for (Block b : e.getBlocks()) {
/* 168 */       logBlockCreated(b);
/*     */     }
/* 170 */     i.put("BPISTION", Integer.valueOf(((Integer)i.get("BPISTION")).intValue() + 1));
/*     */   }
/*     */ 
/*     */   public void logBlockCreated(Block b)
/*     */   {
/* 175 */     if (GameManager.getInstance().getBlockGameId(b.getLocation()) == -1)
/* 176 */       return;
/* 177 */     if (GameManager.getInstance().getGameMode(GameManager.getInstance().getBlockGameId(b.getLocation())) == Game.GameMode.DISABLED)
/* 178 */       return;
/* 179 */     QueueManager.getInstance().add(
/* 180 */       new BlockData(
/* 181 */       GameManager.getInstance().getBlockGameId(b.getLocation()), 
/* 182 */       b.getWorld().getName(), 
/* 183 */       0, 
/* 184 */       0, 
/* 185 */       b.getTypeId(), 
/* 186 */       b.getData(), 
/* 187 */       b.getX(), 
/* 188 */       b.getY(), 
/* 189 */       b.getZ()));
/*     */   }
/*     */ 
/*     */   public void logBlockDestoryed(Block b)
/*     */   {
/* 194 */     if (GameManager.getInstance().getBlockGameId(b.getLocation()) == -1)
/* 195 */       return;
/* 196 */     if (GameManager.getInstance().getGameMode(GameManager.getInstance().getBlockGameId(b.getLocation())) == Game.GameMode.DISABLED)
/* 197 */       return;
/* 198 */     if (b.getTypeId() == 51)
/* 199 */       return;
/* 200 */     QueueManager.getInstance().add(
/* 201 */       new BlockData(
/* 202 */       GameManager.getInstance().getBlockGameId(b.getLocation()), 
/* 203 */       b.getWorld().getName(), 
/* 204 */       b.getTypeId(), 
/* 205 */       b.getData(), 
/* 206 */       0, 
/* 207 */       0, 
/* 208 */       b.getX(), 
/* 209 */       b.getY(), 
/* 210 */       b.getZ()));
/*     */   }
/*     */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.logging.LoggingManager
 * JD-Core Version:    0.6.0
 */