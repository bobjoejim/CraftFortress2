/*    */ package com.skitscape.survivalgames.commands;
/*    */ 
/*    */ import com.skitscape.survivalgames.GameManager;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class ListPlayers
/*    */   implements SubCommand
/*    */ {
/*    */   public boolean onCommand(Player player, String[] args)
/*    */   {
/* 12 */     int gid = 0;
/*    */ 
/* 14 */     if (args.length == 0) {
/* 15 */       gid = GameManager.getInstance().getPlayerGameId(player);
/*    */     }
/*    */     else {
/* 18 */       gid = Integer.parseInt(args[0]);
/*    */     }
/*    */ 
/* 21 */     Player[] act = GameManager.getInstance().getGame(gid).getPlayers()[0];
/* 22 */     Player[] deact = GameManager.getInstance().getGame(gid).getPlayers()[1];
/* 23 */     String act1 = ChatColor.AQUA + "[Alive: " + act.length + "]";
/* 24 */     String deact1 = ChatColor.RED + "[Inactive: " + deact.length + "]";
/* 25 */     for (Player p : act) {
/* 26 */       act1 = act1 + p.getName() + ", ";
/*    */     }
/* 28 */     for (Player p : deact) {
/* 29 */       deact1 = deact1 + p.getName() + ", ";
/*    */     }
/*    */ 
/* 32 */     player.sendMessage(act1);
/* 33 */     player.sendMessage(deact1);
/* 34 */     return false;
/*    */   }
/*    */ 
/*    */   public String help(Player p)
/*    */   {
/* 39 */     return "List players in a game";
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.commands.ListPlayers
 * JD-Core Version:    0.6.0
 */