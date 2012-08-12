/*    */ package com.skitscape.survivalgames.commands;
/*    */ 
/*    */ import com.skitscape.survivalgames.Game;
/*    */ import com.skitscape.survivalgames.GameManager;
/*    */ import com.skitscape.survivalgames.SettingsManager;
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class SetSpawn
/*    */   implements SubCommand
/*    */ {
/* 18 */   HashMap<Integer, Integer> next = new HashMap();
/*    */ 
/*    */   public void loadNextSpawn()
/*    */   {
/* 25 */     for (Game g : (Game[])GameManager.getInstance().getGames().toArray(new Game[0]))
/* 26 */       this.next.put(Integer.valueOf(g.getID()), Integer.valueOf(SettingsManager.getInstance().getSpawnCount(g.getID()) + 1));
/*    */   }
/*    */ 
/*    */   public boolean onCommand(Player player, String[] args)
/*    */   {
/* 32 */     if ((!player.hasPermission("sg.arena.setspawn")) && (!player.isOp())) {
/* 33 */       player.sendMessage(ChatColor.RED + "No Permission");
/* 34 */       return true;
/*    */     }
/* 36 */     loadNextSpawn();
/* 37 */     System.out.println("settings spawn");
/* 38 */     Location l = player.getLocation();
/* 39 */     int game = GameManager.getInstance().getBlockGameId(l);
/* 40 */     System.out.println(game + " " + this.next.size());
/* 41 */     if (game == -1) {
/* 42 */       player.sendMessage(ChatColor.RED + "Must be in an arena!");
/*    */     }
/* 44 */     int i = 0;
/* 45 */     if (args[0].equalsIgnoreCase("next")) {
/* 46 */       i = ((Integer)this.next.get(Integer.valueOf(game))).intValue();
/* 47 */       this.next.put(Integer.valueOf(game), Integer.valueOf(((Integer)this.next.get(Integer.valueOf(game))).intValue() + 1));
/*    */     }
/*    */     else {
/*    */       try {
/* 51 */         i = Integer.parseInt(args[0]);
/* 52 */         if ((i > ((Integer)this.next.get(Integer.valueOf(game))).intValue() + 1) || (i < 1)) {
/* 53 */           player.sendMessage(ChatColor.RED + "Spawn must be between 1 & " + this.next.get(Integer.valueOf(game)));
/* 54 */           return true;
/*    */         }
/* 56 */         if (i == ((Integer)this.next.get(Integer.valueOf(game))).intValue())
/* 57 */           this.next.put(Integer.valueOf(game), Integer.valueOf(((Integer)this.next.get(Integer.valueOf(game))).intValue() + 1));
/*    */       }
/*    */       catch (Exception e) {
/* 60 */         player.sendMessage(ChatColor.RED + "Malformed input. Must be \"next\" or a number");
/* 61 */         return false;
/*    */       }
/*    */     }
/* 64 */     if (i == -1) {
/* 65 */       player.sendMessage(ChatColor.RED + "You must be inside an arnea");
/* 66 */       return true;
/*    */     }
/* 68 */     SettingsManager.getInstance().setSpawn(game, i, l.toVector());
/* 69 */     player.sendMessage(ChatColor.GREEN + "Spawn " + i + " in arena " + game + " set!");
/*    */ 
/* 71 */     return true;
/*    */   }
/*    */ 
/*    */   public String help(Player p)
/*    */   {
/* 76 */     return "/sg setspawn next- Sets a spawn in the arena you are located in.";
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.commands.SetSpawn
 * JD-Core Version:    0.6.0
 */