/*    */ package com.skitscape.survivalgames.commands;
/*    */ 
/*    */ import com.skitscape.survivalgames.GameManager;
/*    */ import com.skitscape.survivalgames.SettingsManager;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class Join
/*    */   implements SubCommand
/*    */ {
/*    */   public boolean onCommand(Player player, String[] args)
/*    */   {
/* 13 */     if (args.length == 1) {
/* 14 */       int a = Integer.parseInt(args[0]);
/* 15 */       if (player.hasPermission("sg.arena.join")) {
/* 16 */         GameManager.getInstance().addPlayer(player, a);
/*    */       }
/*    */       else
/* 19 */         player.sendMessage(ChatColor.RED + "No Permission");
/*    */     }
/*    */     else
/*    */     {
/* 23 */       if (player.hasPermission("sg.lobby.join")) {
/* 24 */         if (GameManager.getInstance().getPlayerGameId(player) != -1) {
/* 25 */           player.sendMessage(ChatColor.RED + "Cannot join the lobby while ingame");
/* 26 */           return true;
/*    */         }
/* 28 */         player.teleport(SettingsManager.getInstance().getLobbySpawn());
/* 29 */         return true;
/*    */       }
/*    */ 
/* 32 */       player.sendMessage(ChatColor.RED + "No Permission");
/*    */     }
/*    */ 
/* 35 */     return true;
/*    */   }
/*    */ 
/*    */   public String help(Player p)
/*    */   {
/* 40 */     return "/sg join - Join the lobby";
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.commands.Join
 * JD-Core Version:    0.6.0
 */