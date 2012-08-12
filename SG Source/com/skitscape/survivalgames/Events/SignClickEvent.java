/*    */ package com.skitscape.survivalgames.Events;
/*    */ 
/*    */ import com.skitscape.survivalgames.GameManager;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.block.Block;
/*    */ import org.bukkit.block.Sign;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.EventPriority;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.block.Action;
/*    */ import org.bukkit.event.player.PlayerInteractEvent;
/*    */ 
/*    */ public class SignClickEvent
/*    */   implements Listener
/*    */ {
/*    */   @EventHandler(priority=EventPriority.HIGHEST)
/*    */   public void clickHandler(PlayerInteractEvent e)
/*    */   {
/* 21 */     if ((e.getAction() != Action.RIGHT_CLICK_BLOCK) && (e.getAction() != Action.LEFT_CLICK_BLOCK)) return;
/*    */ 
/* 24 */     Block clickedBlock = e.getClickedBlock();
/* 25 */     if ((clickedBlock.getType() != Material.SIGN) && (clickedBlock.getType() != Material.SIGN_POST) && (clickedBlock.getType() != Material.WALL_SIGN)) return;
/* 26 */     Sign thisSign = (Sign)clickedBlock.getState();
/*    */ 
/* 28 */     String[] lines = thisSign.getLines();
/* 29 */     if (lines.length < 3) return;
/* 30 */     if (lines[0].equalsIgnoreCase("[SurvivalGames]")) {
/* 31 */       e.setCancelled(true);
/* 32 */       if ((!e.getPlayer().hasPermission("sg.arena.join")) && (!e.getPlayer().isOp()))
/*    */       {
/* 36 */         e.getPlayer().sendMessage(ChatColor.RED + "No Permission");
/* 37 */         return;
/*    */       }
/*    */       try {
/* 40 */         if (lines[2].equalsIgnoreCase("Auto Assign")) {
/* 41 */           GameManager.getInstance().autoAddPlayer(e.getPlayer());
/*    */         }
/*    */         else {
/* 44 */           String game = lines[2].replace("Arena ", "");
/* 45 */           int gameno = Integer.parseInt(game);
/*    */ 
/* 47 */           GameManager.getInstance().addPlayer(e.getPlayer(), gameno);
/*    */         }
/*    */       }
/*    */       catch (Exception localException)
/*    */       {
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.Events.SignClickEvent
 * JD-Core Version:    0.6.0
 */