/*    */ package com.skitscape.survivalgames.Events;
/*    */ 
/*    */ import com.skitscape.survivalgames.GameManager;
/*    */ import com.skitscape.survivalgames.SettingsManager;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.EventPriority;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.player.PlayerCommandPreprocessEvent;
/*    */ 
/*    */ public class CommandCatch
/*    */   implements Listener
/*    */ {
/*    */   @EventHandler(priority=EventPriority.HIGHEST)
/*    */   public void onPlayerDieEvent(PlayerCommandPreprocessEvent event)
/*    */   {
/* 20 */     String m = event.getMessage();
/*    */ 
/* 22 */     if ((!GameManager.getInstance().isPlayerActive(event.getPlayer())) && (!GameManager.getInstance().isPlayerInactive(event.getPlayer())) && (!GameManager.getInstance().isSpectator(event.getPlayer())))
/* 23 */       return;
/* 24 */     if (m.equalsIgnoreCase("/list")) {
/* 25 */       Player[] act = GameManager.getInstance().getGame(GameManager.getInstance().getPlayerGameId(event.getPlayer())).getPlayers()[0];
/* 26 */       Player[] deact = GameManager.getInstance().getGame(GameManager.getInstance().getPlayerGameId(event.getPlayer())).getPlayers()[1];
/* 27 */       String act1 = ChatColor.AQUA + "[Alive: " + act.length + "]";
/* 28 */       String deact1 = ChatColor.RED + "[Inactive: " + deact.length + "]";
/* 29 */       for (Player p : act) {
/* 30 */         act1 = act1 + p.getName() + ", ";
/*    */       }
/* 32 */       for (Player p : deact) {
/* 33 */         deact1 = deact1 + p.getName() + ", ";
/*    */       }
/*    */ 
/* 36 */       event.getPlayer().sendMessage(act1);
/* 37 */       event.getPlayer().sendMessage(deact1);
/* 38 */       event.setCancelled(true);
/* 39 */       return;
/*    */     }
/* 41 */     if (!SettingsManager.getInstance().getConfig().getBoolean("disallow-commands"))
/* 42 */       return;
/* 43 */     if ((event.getPlayer().isOp()) || (event.getPlayer().hasPermission("sg.arena.nocmdblock")))
/* 44 */       return;
/* 45 */     if ((m.startsWith("/sg")) || (m.startsWith("/survivalgames")) || (m.startsWith("/hg")) || (m.startsWith("/hungergames")) || (m.startsWith("/msg"))) {
/* 46 */       return;
/*    */     }
/* 48 */     event.setCancelled(true);
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.Events.CommandCatch
 * JD-Core Version:    0.6.0
 */