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
/*    */ import org.bukkit.event.block.BlockPlaceEvent;
/*    */ 
/*    */ public class PlaceEvent
/*    */   implements Listener
/*    */ {
/* 19 */   public ArrayList<Integer> allowedPlace = new ArrayList();
/*    */ 
/*    */   public PlaceEvent() {
/* 22 */     this.allowedPlace.addAll(SettingsManager.getInstance().getConfig().getIntegerList("block.place.whitelist"));
/*    */   }
/*    */   @EventHandler(priority=EventPriority.HIGHEST)
/*    */   public void onBlockPlace(BlockPlaceEvent event) {
/* 27 */     Player p = event.getPlayer();
/* 28 */     int id = GameManager.getInstance().getPlayerGameId(p);
/*    */ 
/* 30 */     if (id == -1) {
/* 31 */       int gameblockid = GameManager.getInstance().getBlockGameId(event.getBlock().getLocation());
/* 32 */       if ((gameblockid != -1) && 
/* 33 */         (GameManager.getInstance().getGame(gameblockid).getGameMode() != Game.GameMode.DISABLED)) {
/* 34 */         event.setCancelled(true);
/*    */       }
/*    */ 
/* 37 */       return;
/*    */     }
/*    */ 
/* 41 */     Game g = GameManager.getInstance().getGame(id);
/* 42 */     if (g.isPlayerinactive(p)) {
/* 43 */       return;
/*    */     }
/* 45 */     if (g.getMode() == Game.GameMode.DISABLED) {
/* 46 */       return;
/*    */     }
/* 48 */     if (g.getMode() != Game.GameMode.INGAME) {
/* 49 */       event.setCancelled(true);
/* 50 */       return;
/*    */     }
/*    */ 
/* 54 */     if (!this.allowedPlace.contains(Integer.valueOf(event.getBlock().getTypeId())))
/* 55 */       event.setCancelled(true);
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.Events.PlaceEvent
 * JD-Core Version:    0.6.0
 */