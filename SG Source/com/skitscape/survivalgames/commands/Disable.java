/*    */ package com.skitscape.survivalgames.commands;
/*    */ 
/*    */ import com.skitscape.survivalgames.Game;
/*    */ import com.skitscape.survivalgames.GameManager;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class Disable
/*    */   implements SubCommand
/*    */ {
/*    */   public boolean onCommand(Player player, String[] args)
/*    */   {
/* 13 */     if ((!player.hasPermission("sg.arena.disable")) && (!player.isOp())) {
/* 14 */       player.sendMessage(ChatColor.RED + "No Permission");
/* 15 */       return true;
/*    */     }
/*    */ 
/* 18 */     if (args.length == 0) {
/* 19 */       for (Game g : GameManager.getInstance().getGames()) {
/* 20 */         g.disable();
/*    */       }
/* 22 */       player.sendMessage(ChatColor.GREEN + "All Arenas disabled");
/*    */     }
/*    */     else
/*    */     {
/* 26 */       GameManager.getInstance().disableGame(Integer.parseInt(args[0]));
/* 27 */       player.sendMessage(ChatColor.GREEN + "Arena " + args[0] + " disabled");
/*    */     }
/* 29 */     return true;
/*    */   }
/*    */ 
/*    */   public String help(Player p) {
/* 33 */     return "/sg disable <id> - Disabled arena <id>";
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.commands.Disable
 * JD-Core Version:    0.6.0
 */