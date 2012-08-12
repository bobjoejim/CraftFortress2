/*    */ package com.skitscape.survivalgames.Events;
/*    */ 
/*    */ import com.skitscape.survivalgames.Game;
/*    */ import com.skitscape.survivalgames.Game.GameMode;
/*    */ import com.skitscape.survivalgames.GameManager;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.EventPriority;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.entity.EntityDamageEvent;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.inventory.PlayerInventory;
/*    */ 
/*    */ public class DeathEvent
/*    */   implements Listener
/*    */ {
/*    */   @EventHandler(priority=EventPriority.HIGHEST)
/*    */   public void onPlayerDieEvent(EntityDamageEvent event)
/*    */   {
/* 27 */     if (!(event.getEntity() instanceof Player))
/*    */     {
/* 29 */       return;
/* 30 */     }Player player = (Player)event.getEntity();
/* 31 */     int gameid = GameManager.getInstance().getPlayerGameId(player);
/* 32 */     if (gameid == -1)
/* 33 */       return;
/* 34 */     if (!GameManager.getInstance().isPlayerActive(player))
/* 35 */       return;
/* 36 */     Game game = GameManager.getInstance().getGame(gameid);
/* 37 */     if (game.getMode() != Game.GameMode.INGAME) {
/* 38 */       event.setCancelled(true);
/* 39 */       return;
/*    */     }
/* 41 */     if (game.isProtectionOn()) {
/* 42 */       event.setCancelled(true);
/* 43 */       return;
/*    */     }
/* 45 */     if (player.getHealth() <= event.getDamage()) {
/* 46 */       event.setCancelled(true);
/* 47 */       player.setHealth(20);
/* 48 */       player.setFoodLevel(20);
/* 49 */       player.setFireTicks(0);
/* 50 */       PlayerInventory inv = player.getInventory();
/* 51 */       Location l = player.getLocation();
/*    */ 
/* 53 */       for (ItemStack i : inv.getContents()) {
/* 54 */         if (i != null)
/* 55 */           l.getWorld().dropItemNaturally(l, i);
/*    */       }
/* 57 */       for (ItemStack i : inv.getArmorContents()) {
/* 58 */         if ((i != null) && (i.getTypeId() != 0)) {
/* 59 */           l.getWorld().dropItemNaturally(l, i);
/*    */         }
/*    */       }
/* 62 */       GameManager.getInstance().getGame(GameManager.getInstance().getPlayerGameId(player)).killPlayer(player, false);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.Events.DeathEvent
 * JD-Core Version:    0.6.0
 */