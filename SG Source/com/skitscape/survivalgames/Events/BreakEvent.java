/*    */ package com.skitscape.survivalgames.Events;
/*    */ 
/*    */ import com.skitscape.survivalgames.Game;
/*    */ import com.skitscape.survivalgames.Game.GameMode;
/*    */ import com.skitscape.survivalgames.GameManager;
/*    */ import com.skitscape.survivalgames.SettingsManager;
/*    */ import java.util.ArrayList;
/*    */ import org.bukkit.block.Block;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.EventPriority;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.block.BlockBreakEvent;
/*    */ 
/*    */ public class BreakEvent
/*    */   implements Listener
/*    */ {
/* 20 */   public ArrayList<Integer> allowedBreak = new ArrayList();
/*    */ 
/*    */   public BreakEvent() {
/* 23 */     this.allowedBreak.addAll(SettingsManager.getInstance().getConfig().getIntegerList("block.break.whitelist"));
/*    */   }
/*    */   @EventHandler(priority=EventPriority.HIGHEST)
/*    */   public void onBlockBreak(BlockBreakEvent event) {
/* 28 */     Player p = event.getPlayer();
/* 29 */     int pid = GameManager.getInstance().getPlayerGameId(p);
/*    */ 
/* 32 */     if (pid == -1) {
/* 33 */       int blockgameid = GameManager.getInstance().getBlockGameId(event.getBlock().getLocation());
/*    */ 
/* 35 */       if ((blockgameid != -1) && 
/* 36 */         (GameManager.getInstance().getGame(blockgameid).getGameMode() != Game.GameMode.DISABLED)) {
/* 37 */         event.setCancelled(true);
/*    */       }
/*    */ 
/* 40 */       return;
/*    */     }
/*    */ 
/* 44 */     Game g = GameManager.getInstance().getGame(pid);
/*    */ 
/* 46 */     if (g.getMode() == Game.GameMode.DISABLED) {
/* 47 */       return;
/*    */     }
/* 49 */     if (g.getMode() != Game.GameMode.INGAME) {
/* 50 */       event.setCancelled(true);
/* 51 */       return;
/*    */     }
/*    */ 
/* 54 */     if (!this.allowedBreak.contains(Integer.valueOf(event.getBlock().getTypeId()))) event.setCancelled(true);
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.Events.BreakEvent
 * JD-Core Version:    0.6.0
 */