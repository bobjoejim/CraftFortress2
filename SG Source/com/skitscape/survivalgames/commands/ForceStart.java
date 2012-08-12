/*    */ package com.skitscape.survivalgames.commands;
/*    */ 
/*    */ import com.skitscape.survivalgames.Game;
/*    */ import com.skitscape.survivalgames.Game.GameMode;
/*    */ import com.skitscape.survivalgames.GameManager;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class ForceStart
/*    */   implements SubCommand
/*    */ {
/*    */   public boolean onCommand(Player player, String[] args)
/*    */   {
/* 18 */     if ((!player.hasPermission("sg.arena.forcestart")) && (!player.isOp())) {
/* 19 */       player.sendMessage(ChatColor.RED + "No Permission");
/* 20 */       return true;
/*    */     }
/* 22 */     int game = -1;
/* 23 */     int seconds = 10;
/* 24 */     if (args.length == 2) {
/* 25 */       seconds = Integer.parseInt(args[1]);
/*    */     }
/* 27 */     if (args.length >= 1) {
/* 28 */       game = Integer.parseInt(args[0]);
/*    */     }
/*    */     else
/*    */     {
/* 32 */       game = GameManager.getInstance().getPlayerGameId(player);
/* 33 */     }if (game == -1) {
/* 34 */       player.sendMessage(ChatColor.RED + "Must be in a game!");
/* 35 */       return true;
/*    */     }
/* 37 */     if (GameManager.getInstance().getGame(game).getActivePlayers() < 2) {
/* 38 */       player.sendMessage(ChatColor.RED + "Needs at least 2 players to start!");
/* 39 */       return true;
/*    */     }
/*    */ 
/* 43 */     Game g = GameManager.getInstance().getGame(game);
/* 44 */     if ((g.getMode() != Game.GameMode.WAITING) && (!player.hasPermission("sg.arena.restart"))) {
/* 45 */       player.sendMessage(ChatColor.RED + "Game Already Starting!");
/* 46 */       return true;
/*    */     }
/* 48 */     g.countdown(seconds);
/* 49 */     g.messageAll(ChatColor.YELLOW + "Game starting in " + seconds + " seconds");
/* 50 */     player.sendMessage(ChatColor.GREEN + "Started arena " + game);
/*    */ 
/* 52 */     return true;
/*    */   }
/*    */ 
/*    */   public String help(Player p)
/*    */   {
/* 57 */     return "/sg forcestart - Force starts a game";
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.commands.ForceStart
 * JD-Core Version:    0.6.0
 */