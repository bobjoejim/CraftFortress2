/*    */ package com.skitscape.survivalgames.Events;
/*    */ 
/*    */ import com.skitscape.survivalgames.Game;
/*    */ import com.skitscape.survivalgames.Game.GameMode;
/*    */ import com.skitscape.survivalgames.GameManager;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.EventPriority;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.player.PlayerMoveEvent;
/*    */ import org.bukkit.util.Vector;
/*    */ 
/*    */ public class MoveEvent
/*    */   implements Listener
/*    */ {
/* 60 */   HashMap<Player, Vector> playerpos = new HashMap();
/*    */ 
/*    */   @EventHandler(priority=EventPriority.HIGHEST)
/*    */   public void outOfBoundsHandler(PlayerMoveEvent e) {
/*    */   }
/*    */   @EventHandler(priority=EventPriority.HIGHEST)
/*    */   public void frozenSpawnHandler(PlayerMoveEvent e) {
/* 69 */     if (GameManager.getInstance().getPlayerGameId(e.getPlayer()) == -1) {
/* 70 */       this.playerpos.remove(e.getPlayer());
/* 71 */       return;
/*    */     }
/* 73 */     if (GameManager.getInstance().getGame(GameManager.getInstance().getPlayerGameId(e.getPlayer())).getMode() == Game.GameMode.INGAME)
/* 74 */       return;
/* 75 */     Game.GameMode mo3 = GameManager.getInstance().getGameMode(GameManager.getInstance().getPlayerGameId(e.getPlayer()));
/* 76 */     if ((GameManager.getInstance().isPlayerActive(e.getPlayer())) && (mo3 != Game.GameMode.INGAME)) {
/* 77 */       if (this.playerpos.get(e.getPlayer()) == null) {
/* 78 */         this.playerpos.put(e.getPlayer(), e.getPlayer().getLocation().toVector());
/* 79 */         return;
/*    */       }
/* 81 */       Location l = e.getPlayer().getLocation();
/* 82 */       Vector v = (Vector)this.playerpos.get(e.getPlayer());
/* 83 */       if ((l.getBlockX() != v.getBlockX()) || (l.getBlockZ() != v.getBlockZ())) {
/* 84 */         l.setX(v.getBlockX() + 0.5D);
/* 85 */         l.setZ(v.getBlockZ() + 0.5D);
/* 86 */         l.setYaw(e.getPlayer().getLocation().getYaw());
/* 87 */         l.setPitch(e.getPlayer().getLocation().getPitch());
/* 88 */         e.getPlayer().teleport(l);
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.Events.MoveEvent
 * JD-Core Version:    0.6.0
 */