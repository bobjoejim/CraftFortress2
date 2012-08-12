/*     */ package com.skitscape.survivalgames.Events;
/*     */ 
/*     */ import com.skitscape.survivalgames.Game;
/*     */ import com.skitscape.survivalgames.GameManager;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.block.Action;
/*     */ import org.bukkit.event.block.BlockBreakEvent;
/*     */ import org.bukkit.event.block.BlockDamageEvent;
/*     */ import org.bukkit.event.block.BlockPlaceEvent;
/*     */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*     */ import org.bukkit.event.entity.EntityDamageEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.event.player.PlayerPickupItemEvent;
/*     */ 
/*     */ public class SpectatorEvents
/*     */   implements Listener
/*     */ {
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void onBlockPlace(BlockPlaceEvent event)
/*     */   {
/*  32 */     Player player = event.getPlayer();
/*  33 */     if (GameManager.getInstance().isSpectator(player))
/*  34 */       event.setCancelled(true);
/*     */   }
/*     */ 
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void onBlockDamage(BlockDamageEvent event) {
/*  40 */     Player player = event.getPlayer();
/*  41 */     if (GameManager.getInstance().isSpectator(player))
/*  42 */       event.setCancelled(true);
/*     */   }
/*     */ 
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void onBlockBreak(BlockBreakEvent event) {
/*  48 */     Player player = event.getPlayer();
/*  49 */     if (GameManager.getInstance().isSpectator(player))
/*  50 */       event.setCancelled(true);
/*     */   }
/*     */ 
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void onPlayerClickEvent(PlayerInteractEvent event) {
/*  56 */     Player player = event.getPlayer();
/*     */     try {
/*  58 */       if (((GameManager.getInstance().isSpectator(player)) && (player.isSneaking()) && ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_AIR))) || (
/*  59 */         (GameManager.getInstance().isSpectator(player)) && (player.isSneaking()) && ((event.getAction() == Action.LEFT_CLICK_AIR) || (event.getAction() == Action.LEFT_CLICK_AIR)))) {
/*  60 */         Player[] players = GameManager.getInstance().getGame(GameManager.getInstance().getPlayerSpectateId(player)).getPlayers()[0];
/*  61 */         Game g = GameManager.getInstance().getGame(GameManager.getInstance().getPlayerSpectateId(player));
/*     */ 
/*  63 */         int i = ((Integer)g.getNextSpec().get(player)).intValue();
/*  64 */         if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_AIR)) {
/*  65 */           i++;
/*     */         }
/*  67 */         else if ((event.getAction() == Action.LEFT_CLICK_AIR) || (event.getAction() == Action.LEFT_CLICK_AIR)) {
/*  68 */           i--;
/*     */         }
/*  70 */         if (i > players.length - 1) {
/*  71 */           i = 0;
/*     */         }
/*  73 */         if (i < 0) {
/*  74 */           i = players.length - 1;
/*     */         }
/*  76 */         g.getNextSpec().put(player, Integer.valueOf(i));
/*  77 */         Player tpto = players[i];
/*  78 */         Location l = tpto.getLocation();
/*  79 */         l.setYaw(0.0F);
/*  80 */         l.setPitch(0.0F);
/*  81 */         player.teleport(l);
/*  82 */         player.sendMessage(ChatColor.AQUA + "You are now spectating " + tpto.getName());
/*     */       }
/*  84 */       else if (GameManager.getInstance().isSpectator(player)) {
/*  85 */         event.setCancelled(true);
/*     */       }
/*     */     } catch (Exception e) {
/*  88 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void onSignChange(PlayerPickupItemEvent event) {
/*  94 */     Player player = event.getPlayer();
/*  95 */     if (GameManager.getInstance().isSpectator(player))
/*  96 */       event.setCancelled(true);
/*     */   }
/*     */ 
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void onEntityDamage(EntityDamageByEntityEvent event) {
/* 102 */     Player player = null;
/* 103 */     if ((event.getDamager() instanceof Player))
/* 104 */       player = (Player)event.getDamager();
/*     */     else
/* 106 */       return;
/* 107 */     if (GameManager.getInstance().isSpectator(player))
/* 108 */       event.setCancelled(true);
/*     */   }
/*     */ 
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void onEntityDamage(EntityDamageEvent event) {
/* 114 */     Player player = null;
/* 115 */     if ((event.getEntity() instanceof Player))
/* 116 */       player = (Player)event.getEntity();
/*     */     else
/* 118 */       return;
/* 119 */     if (GameManager.getInstance().isSpectator(player))
/* 120 */       event.setCancelled(true);
/*     */   }
/*     */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.Events.SpectatorEvents
 * JD-Core Version:    0.6.0
 */