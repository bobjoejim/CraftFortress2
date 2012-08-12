/*    */ package com.skitscape.survivalgames.commands;
/*    */ 
/*    */ import com.skitscape.survivalgames.Game;
/*    */ import com.skitscape.survivalgames.GameManager;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class Enable
/*    */   implements SubCommand
/*    */ {
/*    */   public boolean onCommand(Player player, String[] args)
/*    */   {
/* 13 */     if ((!player.hasPermission("sg.arena.enable")) && (!player.isOp())) {
/* 14 */       player.sendMessage(ChatColor.RED + "No Permission");
/* 15 */       return true;
/*    */     }
/*    */ 
/* 18 */     if (args.length == 0) {
/* 19 */       for (Game g : GameManager.getInstance().getGames()) {
/* 20 */         g.enable();
/*    */       }
/*    */ 
/* 23 */       player.sendMessage(ChatColor.GREEN + "Enabled all arenas");
/*    */     }
/*    */     else
/*    */     {
/* 27 */       GameManager.getInstance().enableGame(Integer.parseInt(args[0]));
/* 28 */       player.sendMessage(ChatColor.GREEN + "Arena " + args[0] + " Enabled");
/*    */     }
/* 30 */     return true;
/*    */   }
/*    */ 
/*    */   public String help(Player p)
/*    */   {
/* 36 */     return "/sg enable <id> - enables a the arena <id>";
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.commands.Enable
 * JD-Core Version:    0.6.0
 */