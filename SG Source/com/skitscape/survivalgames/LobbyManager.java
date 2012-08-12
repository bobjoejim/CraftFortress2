/*     */ package com.skitscape.survivalgames;
/*     */ 
/*     */ import com.sk89q.worldedit.Vector;
/*     */ import com.sk89q.worldedit.bukkit.WorldEditPlugin;
/*     */ import com.sk89q.worldedit.bukkit.selections.Selection;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Chunk;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.BlockState;
/*     */ import org.bukkit.block.Sign;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.material.MaterialData;
/*     */ import org.bukkit.scheduler.BukkitScheduler;
/*     */ 
/*     */ public class LobbyManager
/*     */   implements Listener
/*     */ {
/*     */   Sign[][] signs;
/*     */   SurvivalGames p;
/*  27 */   private int runningThread = 0;
/*  28 */   private static LobbyManager instance = new LobbyManager();
/*  29 */   public HashSet<Chunk> lobbychunks = new HashSet();
/*     */ 
/* 164 */   boolean showingMessage = false;
/* 165 */   ArrayList<String[]> messagequeue = new ArrayList(3);
/*     */   private boolean error;
/* 189 */   int tid = 0;
/*     */ 
/*     */   public static LobbyManager getInstance()
/*     */   {
/*  36 */     return instance;
/*     */   }
/*     */ 
/*     */   public void setup(SurvivalGames p)
/*     */   {
/*  42 */     this.p = p;
/*  43 */     loadSigns();
/*     */   }
/*     */ 
/*     */   public void loadSigns()
/*     */   {
/*  48 */     FileConfiguration c = SettingsManager.getInstance().getSystemConfig();
/*     */     try {
/*  50 */       if (!c.getBoolean("sg-system.lobby.sign.set"))
/*  51 */         return; 
/*     */     } catch (Exception e) {
/*  52 */       return;
/*  53 */     }boolean usingx = false;
/*  54 */     int hdiff = 0;
/*  55 */     int x1 = c.getInt("sg-system.lobby.sign.x1");
/*  56 */     int y1 = c.getInt("sg-system.lobby.sign.y1");
/*  57 */     int z1 = c.getInt("sg-system.lobby.sign.z1");
/*  58 */     int x2 = c.getInt("sg-system.lobby.sign.x2");
/*  59 */     int y2 = c.getInt("sg-system.lobby.sign.y2");
/*  60 */     int z2 = c.getInt("sg-system.lobby.sign.z2");
/*  61 */     int inc = 0;
/*     */ 
/*  64 */     byte temp = ((Sign)new Location(this.p.getServer().getWorld(c.getString("sg-system.lobby.sign.world")), x1, y1, z1).getBlock().getState()).getData().getData();
/*     */     Location l;
/*  66 */     if ((temp == 3) || (temp == 4)) {
/*  67 */       Location l = new Location(Bukkit.getWorld(c.getString("sg-system.lobby.sign.world")), x1, y1, z1);
/*  68 */       inc = -1;
/*     */     } else {
/*  70 */       l = new Location(Bukkit.getWorld(c.getString("sg-system.lobby.sign.world")), x2, y1, z2);
/*  71 */       inc = 1;
/*     */     }
/*     */ 
/*  75 */     usingx = x2 - x1 != 0;
/*  76 */     if (usingx) {
/*  77 */       hdiff = x1 - x2 + 1;
/*     */     }
/*     */     else {
/*  80 */       hdiff = z1 - z2 + 1;
/*     */     }
/*  82 */     int vdiff = y1 - y2 + 1;
/*     */ 
/*  85 */     System.out.println(vdiff + "              " + hdiff);
/*  86 */     this.signs = new Sign[vdiff][hdiff];
/*  87 */     for (int y = vdiff - 1; y >= 0; y--) {
/*  88 */       for (int x = hdiff - 1; x >= 0; x--)
/*     */       {
/*  91 */         BlockState b = this.p.getServer().getWorld(SettingsManager.getInstance().getSystemConfig().getString("sg-system.lobby.sign.world")).getBlockAt(l).getState();
/*  92 */         this.lobbychunks.add(b.getChunk());
/*  93 */         if ((b instanceof Sign)) {
/*  94 */           this.signs[y][x] = ((Sign)b);
/*     */         }
/*  96 */         if (usingx)
/*  97 */           l = l.add(inc, 0.0D, 0.0D);
/*     */         else {
/*  99 */           l = l.add(0.0D, 0.0D, inc);
/*     */         }
/*     */       }
/* 102 */       l = l.add(0.0D, -1.0D, 0.0D);
/* 103 */       if (inc == -1) {
/* 104 */         l.setX(x1);
/* 105 */         l.setZ(z1);
/*     */       }
/*     */       else {
/* 108 */         l.setX(x2);
/* 109 */         l.setZ(z2);
/*     */       }
/*     */     }
/* 112 */     this.runningThread += 1;
/* 113 */     showMessage(new String[] { "", "Survival Games", "", "Double0negative", "iMalo", "mc-sg.org", "" });
/*     */   }
/*     */ 
/*     */   public int[] getSignMidPoint()
/*     */   {
/* 119 */     double x = this.signs[0].length * 8;
/* 120 */     double y = this.signs.length * 2;
/*     */ 
/* 122 */     return new int[] { (int)x, (int)y };
/*     */   }
/*     */ 
/*     */   public void setLobbySignsFromSelection(Player pl)
/*     */   {
/* 128 */     FileConfiguration c = SettingsManager.getInstance().getSystemConfig();
/* 129 */     SettingsManager s = SettingsManager.getInstance();
/* 130 */     if (!c.getBoolean("sg-system.lobby.sign.set", false)) {
/* 131 */       c.set("sg-system.lobby.sign.set", Boolean.valueOf(true));
/* 132 */       s.saveSystemConfig();
/*     */     }
/*     */ 
/* 135 */     WorldEditPlugin we = this.p.getWorldEdit();
/* 136 */     Selection sel = we.getSelection(pl);
/* 137 */     if (sel == null) {
/* 138 */       pl.sendMessage(ChatColor.RED + "You must make a WorldEdit Selection first");
/* 139 */       return;
/*     */     }
/* 141 */     if ((sel.getNativeMaximumPoint().getBlockX() - sel.getNativeMaximumPoint().getBlockX() != 0) && (sel.getNativeMaximumPoint().getBlockZ() - sel.getNativeMaximumPoint().getBlockZ() != 0)) {
/* 142 */       pl.sendMessage(ChatColor.RED + " Must be in a straight line!");
/* 143 */       return;
/*     */     }
/*     */ 
/* 146 */     Vector max = sel.getNativeMaximumPoint();
/* 147 */     Vector min = sel.getNativeMinimumPoint();
/*     */ 
/* 149 */     c.set("sg-system.lobby.sign.world", pl.getWorld().getName());
/* 150 */     c.set("sg-system.lobby.sign.x1", Integer.valueOf(max.getBlockX()));
/* 151 */     c.set("sg-system.lobby.sign.y1", Integer.valueOf(max.getBlockY()));
/* 152 */     c.set("sg-system.lobby.sign.z1", Integer.valueOf(max.getBlockZ()));
/* 153 */     c.set("sg-system.lobby.sign.x2", Integer.valueOf(min.getBlockX()));
/* 154 */     c.set("sg-system.lobby.sign.y2", Integer.valueOf(min.getBlockY()));
/* 155 */     c.set("sg-system.lobby.sign.z2", Integer.valueOf(min.getBlockZ()));
/*     */ 
/* 157 */     pl.sendMessage(ChatColor.GREEN + "Lobby Status wall successfuly created");
/* 158 */     s.saveSystemConfig();
/* 159 */     loadSigns();
/*     */   }
/*     */ 
/*     */   public void showMessage(String[] msg9)
/*     */   {
/* 170 */     signShowMessage(msg9);
/*     */   }
/*     */ 
/*     */   public void signShowMessage(String[] msg)
/*     */   {
/* 186 */     signShowMessage(msg, 5000L);
/*     */   }
/*     */ 
/*     */   public void signShowMessage(String[] msg9, long wait)
/*     */   {
/* 192 */     this.runningThread += 1;
/*     */ 
/* 194 */     this.messagequeue.add(msg9);
/* 195 */     if (this.showingMessage)
/* 196 */       return;
/* 197 */     this.showingMessage = true;
/* 198 */     if (this.tid != 0) {
/* 199 */       Bukkit.getScheduler().cancelTask(this.tid);
/*     */     }
/*     */ 
/* 239 */     clearSigns();
/*     */ 
/* 241 */     for (int c = 0; c < this.messagequeue.size(); c++) {
/* 242 */       String[] msg = (String[])this.messagequeue.get(c);
/* 243 */       int x = getSignMidPoint()[1] - msg.length / 2;
/* 244 */       int lineno = x % 3;
/* 245 */       x /= 4;
/* 246 */       for (int a = msg.length - 1; a > -1; a--) {
/* 247 */         int y = getSignMidPoint()[0] - msg[a].length() / 2;
/*     */ 
/* 250 */         char[] line = msg[a].toCharArray();
/* 251 */         for (int b = 0; b < line.length; b++)
/*     */         {
/* 254 */           Sign sig = this.signs[x][(y / 16)];
/* 255 */           sig.setLine(lineno, sig.getLine(lineno) + line[b]);
/*     */ 
/* 257 */           this.signs[x][(y / 16)].update();
/*     */ 
/* 259 */           y++;
/*     */         }
/* 261 */         if (lineno == 0) {
/* 262 */           lineno = 3;
/* 263 */           x++;
/*     */         }
/*     */         else {
/* 266 */           lineno--;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 272 */     this.tid = Bukkit.getScheduler().scheduleSyncDelayedTask(this.p, new Runnable() {
/*     */       public void run() {
/* 274 */         Bukkit.getScheduler().scheduleSyncRepeatingTask(LobbyManager.this.p, new LobbyManager.LobbySignUpdater(LobbyManager.this), 1L, 20L);
/* 275 */         LobbyManager.this.clearSigns();
/*     */       }
/*     */     }
/*     */     , 100L);
/*     */ 
/* 282 */     this.messagequeue.clear();
/*     */ 
/* 284 */     this.showingMessage = false;
/*     */   }
/*     */ 
/*     */   public void updateGameStatus()
/*     */   {
/* 307 */     int b = this.signs.length - 1;
/* 308 */     if (!SurvivalGames.config_todate) {
/* 309 */       this.signs[b][0].setLine(0, ChatColor.RED + "CONFIG");
/* 310 */       this.signs[b][0].setLine(1, ChatColor.RED + "OUTDATED!");
/* 311 */       this.signs[b][1].setLine(0, ChatColor.RED + "Please reset");
/* 312 */       this.signs[b][1].setLine(1, ChatColor.RED + "your config");
/* 313 */       this.signs[b][0].update();
/* 314 */       this.signs[b][1].update();
/* 315 */       return;
/*     */     }
/* 317 */     if (!SurvivalGames.dbcon) {
/* 318 */       this.signs[b][0].setLine(0, ChatColor.RED + "No Database");
/* 319 */       this.signs[b][0].update();
/* 320 */       return;
/*     */     }
/* 322 */     if (GameManager.getInstance().getGameCount() == 0) {
/* 323 */       this.signs[b][0].setLine(1, ChatColor.RED + "No Arenas");
/* 324 */       this.signs[b][0].update();
/* 325 */       return;
/*     */     }
/*     */     try {
/* 328 */       SettingsManager.getInstance().getLobbySpawn();
/*     */     }
/*     */     catch (Exception e) {
/* 331 */       this.signs[b][0].setLine(1, ChatColor.RED + "No Lobby spawn!");
/* 332 */       this.signs[b][0].update();
/* 333 */       return;
/*     */     }
/* 335 */     if (this.error) {
/* 336 */       this.signs[b][0].setLine(1, ChatColor.RED + "Error");
/* 337 */       this.signs[b][0].update();
/* 338 */       return;
/*     */     }
/*     */ 
/* 341 */     ArrayList games = GameManager.getInstance().getGames();
/*     */ 
/* 343 */     for (int a = 0; a < games.size(); a++)
/*     */       try {
/* 345 */         Game game = (Game)games.get(a);
/*     */ 
/* 347 */         this.signs[b][0].setLine(0, "[SurvivalGames]");
/* 348 */         this.signs[b][0].setLine(1, "Click to join");
/* 349 */         this.signs[b][0].setLine(2, "Arena " + game.getID());
/* 350 */         this.signs[b][1].setLine(0, "Arena " + game.getID());
/* 351 */         this.signs[b][1].setLine(1, game.getMode());
/* 352 */         this.signs[b][1].setLine(2, game.getActivePlayers() + 
/* 353 */           "/" + ChatColor.GRAY + game.getInactivePlayers() + ChatColor.BLACK + 
/* 354 */           "/" + SettingsManager.getInstance().getSpawnCount(game.getID()));
/* 355 */         if (game.getMode() == Game.GameMode.STARTING) {
/* 356 */           this.signs[b][1].setLine(3, game.getCountdownTime());
/* 357 */         } else if ((game.getMode() == Game.GameMode.RESETING) || (game.getGameMode() == Game.GameMode.FINISHING)) {
/* 358 */           this.signs[b][2].setLine(3, game.getRBStatus());
/* 359 */           if (game.getRBPercent() > 100.0D) {
/* 360 */             this.signs[b][a].setLine(1, "Saving Queue");
/* 361 */             this.signs[b][1].setLine(3, (int)game.getRBPercent() + " left");
/*     */           }
/*     */           else
/*     */           {
/* 365 */             this.signs[b][1].setLine(3, (int)game.getRBPercent() + "%");
/*     */           }
/*     */         } else {
/* 368 */           this.signs[b][1].setLine(3, "");
/* 369 */         }this.signs[b][0].update();
/* 370 */         this.signs[b][1].update();
/* 371 */         this.signs[b][2].update();
/*     */ 
/* 373 */         int signno = 2;
/* 374 */         int line = 0;
/* 375 */         Player[] active = game.getPlayers()[0];
/* 376 */         Player[] inactive = game.getPlayers()[1];
/* 377 */         for (Player p : active) {
/* 378 */           if (signno >= this.signs[b].length)
/*     */             continue;
/* 380 */           this.signs[b][signno].setLine(line, (SurvivalGames.auth.contains(p.getName()) ? ChatColor.DARK_BLUE : ChatColor.BLACK) + (p.getName().equalsIgnoreCase("Double0negative") ? "Double0" : p.getName()));
/* 381 */           this.signs[b][signno].update();
/*     */ 
/* 383 */           line++;
/* 384 */           if (line == 4) {
/* 385 */             line = 0;
/* 386 */             signno++;
/*     */           }
/*     */         }
/*     */ 
/* 390 */         for (Player p : inactive) {
/* 391 */           if (signno < this.signs[b].length) {
/* 392 */             this.signs[b][signno].setLine(line, (SurvivalGames.auth.contains(p.getName()) ? ChatColor.DARK_RED : ChatColor.GRAY) + (p.getName().equalsIgnoreCase("Double0negative") ? "Double0" : p.getName()));
/* 393 */             this.signs[b][signno].update();
/* 394 */             line++;
/* 395 */             if (line == 4) {
/* 396 */               line = 0;
/* 397 */               signno++;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 404 */         b--; } catch (Exception e) {
/* 405 */         e.printStackTrace(); this.signs[0][0].setLine(1, ChatColor.RED + "ERROR"); this.signs[0][0].setLine(1, ChatColor.RED + "Check Console");
/*     */       }
/*     */   }
/*     */ 
/*     */   public void clearSigns()
/*     */   {
/*     */     try
/*     */     {
/* 414 */       for (int y = this.signs.length - 1; y != -1; y--)
/* 415 */         for (int a = 0; a < 4; a++)
/*     */         {
/* 417 */           for (int x = 0; x != this.signs[0].length; x++)
/*     */           {
/* 419 */             Sign sig = this.signs[y][x];
/* 420 */             sig.setLine(a, "");
/* 421 */             sig.update();
/*     */           }
/*     */         }
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void error(boolean e) {
/* 431 */     this.error = e;
/*     */   }
/*     */ 
/*     */   class LobbySignUpdater
/*     */     implements Runnable
/*     */   {
/*     */     LobbySignUpdater()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 294 */       LobbyManager.this.updateGameStatus();
/*     */     }
/*     */   }
/*     */ 
/*     */   class ThreadMessageDisplay extends Thread
/*     */   {
/*     */     String[] message;
/*     */ 
/*     */     ThreadMessageDisplay(String[] msg)
/*     */     {
/* 177 */       this.message = msg;
/*     */     }
/*     */ 
/*     */     public void run() {
/* 181 */       LobbyManager.this.signShowMessage(this.message);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.LobbyManager
 * JD-Core Version:    0.6.0
 */