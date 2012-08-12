/*    */ package com.skitscape.survivalgames.Events;
/*    */ 
/*    */ import com.skitscape.survivalgames.Game;
/*    */ import com.skitscape.survivalgames.GameManager;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.player.PlayerTeleportEvent;
/*    */ import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
/*    */ 
/*    */ public class TeleportEvent
/*    */   implements Listener
/*    */ {
/*    */   @EventHandler
/*    */   public void playerTeleport(PlayerTeleportEvent event)
/*    */   {
/* 15 */     Player p = event.getPlayer();
/* 16 */     int id = GameManager.getInstance().getPlayerGameId(p);
/* 17 */     if (id == -1) return;
/* 18 */     if ((GameManager.getInstance().getGame(id).isPlayerActive(p)) && (event.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND)) {
/* 19 */       p.sendMessage(ChatColor.RED + " Cannot teleport while ingame!");
/* 20 */       event.setCancelled(true);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.Events.TeleportEvent
 * JD-Core Version:    0.6.0
 */