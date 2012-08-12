/*    */ package com.skitscape.survivalgames.commands;
/*    */ 
/*    */ import com.skitscape.survivalgames.Game;
/*    */ import com.skitscape.survivalgames.GameManager;
/*    */ import com.skitscape.survivalgames.LobbyManager;
/*    */ import com.skitscape.survivalgames.SettingsManager;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class DelArena
/*    */   implements SubCommand
/*    */ {
/*    */   public boolean onCommand(Player player, String[] args)
/*    */   {
/* 17 */     if ((!player.hasPermission("sg.arena.delete")) && (!player.isOp())) {
/* 18 */       player.sendMessage(ChatColor.RED + "No Permission");
/* 19 */       return true;
/*    */     }
/* 21 */     if (args.length != 1) {
/* 22 */       player.sendMessage(ChatColor.RED + "Please specify an arena");
/* 23 */       return true;
/*    */     }
/*    */ 
/* 26 */     FileConfiguration s = SettingsManager.getInstance().getSystemConfig();
/*    */ 
/* 28 */     int arena = Integer.parseInt(args[0]);
/* 29 */     Game g = GameManager.getInstance().getGame(arena);
/* 30 */     g.disable();
/* 31 */     s.set("sg-system.arenas." + arena + ".enabled", Boolean.valueOf(false));
/* 32 */     s.set("sg-system.arenano", Integer.valueOf(s.getInt("sg-system.arenano") - 1));
/*    */ 
/* 34 */     player.sendMessage(ChatColor.GREEN + "Arena deleted");
/*    */ 
/* 36 */     SettingsManager.getInstance().saveSystemConfig();
/* 37 */     GameManager.getInstance().hotRemoveArena(arena);
/* 38 */     LobbyManager.getInstance().clearSigns();
/*    */ 
/* 40 */     return true;
/*    */   }
/*    */ 
/*    */   public String help(Player p)
/*    */   {
/* 45 */     return "/sg delarena <id> - Deletes an arena";
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.commands.DelArena
 * JD-Core Version:    0.6.0
 */