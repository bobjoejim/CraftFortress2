/*     */ package com.skitscape.survivalgames;
/*     */ 
/*     */ import com.skitscape.survivalgames.stats.StatsManager;
/*     */ import com.skitscape.survivalgames.util.GameReset;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.GameMode;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.entity.EntityType;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.entity.EntityDamageEvent;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
/*     */ import org.bukkit.plugin.PluginManager;
/*     */ import org.bukkit.scheduler.BukkitScheduler;
/*     */ 
/*     */ public class Game
/*     */ {
/*  35 */   private GameMode mode = GameMode.DISABLED;
/*  36 */   private ArrayList<Player> activePlayers = new ArrayList();
/*  37 */   private ArrayList<Player> inactivePlayers = new ArrayList();
/*  38 */   private ArrayList<String> spectators = new ArrayList();
/*  39 */   private ArrayList<Player> queue = new ArrayList();
/*  40 */   HashMap<Player, Integer> nextspec = new HashMap();
/*     */   private Arena arena;
/*     */   private int gameID;
/*     */   private int arenano;
/*  46 */   private int gcount = 0;
/*     */   private FileConfiguration c;
/*     */   private FileConfiguration s;
/*  49 */   private HashMap<Integer, Player> spawns = new HashMap();
/*  50 */   private HashMap<Player, ItemStack[][]> inv_store = new HashMap();
/*  51 */   private int spawnCount = 0;
/*  52 */   private int vote = 0;
/*  53 */   private boolean disabled = false;
/*  54 */   private int endgameTaskID = 0;
/*  55 */   private boolean endgameRunning = false;
/*  56 */   private double rbpercent = 0.0D;
/*  57 */   private String rbstatus = "";
/*  58 */   private long startTime = 0L;
/*     */   private boolean countdownRunning;
/*  60 */   private StatsManager sm = StatsManager.getInstance();
/*     */ 
/* 220 */   ArrayList<Player> voted = new ArrayList();
/*     */ 
/* 679 */   int counttime = 0;
/* 680 */   int threadsync = 0;
/*     */ 
/*     */   public Game(int gameid)
/*     */   {
/*  63 */     this.gameID = gameid;
/*  64 */     this.c = SettingsManager.getInstance().getConfig();
/*     */ 
/*  66 */     this.s = SettingsManager.getInstance().getSystemConfig();
/*  67 */     setup();
/*     */   }
/*     */ 
/*     */   public void setup()
/*     */   {
/*  72 */     this.mode = GameMode.LOADING;
/*  73 */     int x = this.s.getInt("sg-system.arenas." + this.gameID + ".x1");
/*  74 */     int y = this.s.getInt("sg-system.arenas." + this.gameID + ".y1");
/*  75 */     int z = this.s.getInt("sg-system.arenas." + this.gameID + ".z1");
/*  76 */     System.out.println(x + " " + y + " " + z);
/*  77 */     int x1 = this.s.getInt("sg-system.arenas." + this.gameID + ".x2");
/*  78 */     int y1 = this.s.getInt("sg-system.arenas." + this.gameID + ".y2");
/*  79 */     int z1 = this.s.getInt("sg-system.arenas." + this.gameID + ".z2");
/*  80 */     System.out.println(x1 + " " + y1 + " " + z1);
/*  81 */     Location max = new Location(SettingsManager.getGameWorld(this.gameID), Math.max(x, x1), Math.max(y, y1), Math.max(z, z1));
/*  82 */     System.out.println(max.toString());
/*  83 */     Location min = new Location(SettingsManager.getGameWorld(this.gameID), Math.min(x, x1), Math.min(y, y1), Math.min(z, z1));
/*  84 */     System.out.println(min.toString());
/*     */ 
/*  86 */     this.arena = new Arena(min, max);
/*     */ 
/*  88 */     loadspawns();
/*     */ 
/*  90 */     this.mode = GameMode.WAITING;
/*     */   }
/*     */ 
/*     */   public void loadspawns() {
/*  94 */     for (int a = 1; a <= SettingsManager.getInstance().getSpawnCount(this.gameID); a++) {
/*  95 */       this.spawns.put(Integer.valueOf(a), null);
/*  96 */       this.spawnCount = a;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addSpawn() {
/* 101 */     this.spawnCount += 1;
/* 102 */     this.spawns.put(Integer.valueOf(this.spawnCount), null);
/*     */   }
/*     */ 
/*     */   public void setMode(GameMode m) {
/* 106 */     this.mode = m;
/*     */   }
/*     */ 
/*     */   public GameMode getGameMode() {
/* 110 */     return this.mode;
/*     */   }
/*     */ 
/*     */   public Arena getArena() {
/* 114 */     return this.arena;
/*     */   }
/*     */ 
/*     */   public void enable() {
/* 118 */     this.mode = GameMode.WAITING;
/*     */ 
/* 120 */     this.disabled = false;
/* 121 */     int b = SettingsManager.getInstance().getSpawnCount(this.gameID) > this.queue.size() ? this.queue.size() : SettingsManager.getInstance().getSpawnCount(this.gameID);
/* 122 */     for (int a = 0; a < b; a++) {
/* 123 */       addPlayer((Player)this.queue.remove(0));
/*     */     }
/* 125 */     int c = 1;
/* 126 */     for (Player p : this.queue) {
/* 127 */       p.sendMessage(ChatColor.GREEN + "You are now #" + c + " in line for arena " + this.gameID);
/* 128 */       c++;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean addPlayer(Player p)
/*     */   {
/* 135 */     GameManager.getInstance().removeFromOtherQueues(p, this.gameID);
/*     */ 
/* 137 */     if ((GameManager.getInstance().getPlayerGameId(p) != -1) && 
/* 138 */       (GameManager.getInstance().isPlayerActive(p))) {
/* 139 */       p.sendMessage(ChatColor.RED + "Cannot join multiple games!");
/* 140 */       return false;
/*     */     }
/*     */ 
/* 143 */     if (this.spectators.contains(p)) removeSpectator(p);
/* 144 */     if ((this.mode == GameMode.WAITING) || (this.mode == GameMode.STARTING))
/*     */     {
/*     */       int a;
/* 145 */       if (this.activePlayers.size() < SettingsManager.getInstance().getSpawnCount(this.gameID)) {
/* 146 */         p.sendMessage("Joining Arena " + this.gameID);
/* 147 */         boolean placed = false;
/*     */ 
/* 149 */         for (a = 1; a <= SettingsManager.getInstance().getSpawnCount(this.gameID); a++) {
/* 150 */           if (this.spawns.get(Integer.valueOf(a)) != null)
/*     */             continue;
/* 152 */           placed = true;
/* 153 */           this.spawns.put(Integer.valueOf(a), p);
/* 154 */           p.teleport(SettingsManager.getInstance().getLobbySpawn());
/* 155 */           saveInv(p);
/* 156 */           clearInv(p);
/* 157 */           p.teleport(SettingsManager.getInstance().getSpawnPoint(this.gameID, a));
/*     */ 
/* 159 */           p.setHealth(20); p.setFoodLevel(20);
/* 160 */           clearInv(p);
/* 161 */           p.setGameMode(GameMode.SURVIVAL);
/* 162 */           this.activePlayers.add(p);
/* 163 */           this.sm.addPlayer(p, this.gameID);
/* 164 */           break;
/*     */         }
/*     */ 
/* 167 */         if (!placed) {
/* 168 */           p.sendMessage(ChatColor.RED + "Game " + this.gameID + " Is Full!");
/* 169 */           return false;
/*     */         }
/*     */       }
/*     */       else {
/* 173 */         if (SettingsManager.getInstance().getSpawnCount(this.gameID) == 0) {
/* 174 */           p.sendMessage(ChatColor.RED + "No spawns set for Arena " + this.gameID + "!");
/* 175 */           return false;
/*     */         }
/*     */ 
/* 178 */         p.sendMessage(ChatColor.RED + "Game " + this.gameID + " Is Full!");
/* 179 */         return false;
/*     */       }
/* 181 */       for (Player pl : this.activePlayers) {
/* 182 */         pl.sendMessage(ChatColor.GREEN + p.getName() + " joined the game! " + getActivePlayers() + "/" + SettingsManager.getInstance().getSpawnCount(this.gameID));
/*     */       }
/* 184 */       if ((this.activePlayers.size() >= this.c.getInt("auto-start-players")) && (!this.countdownRunning))
/* 185 */         countdown(this.c.getInt("auto-start-time"));
/* 186 */       return true;
/*     */     }
/*     */ 
/* 189 */     if (this.c.getBoolean("enable-player-queue")) {
/* 190 */       if (!this.queue.contains(p)) {
/* 191 */         this.queue.add(p);
/* 192 */         p.sendMessage(ChatColor.GREEN + "Added to queue line");
/*     */       }
/* 194 */       int a = 1;
/* 195 */       for (Player qp : this.queue) {
/* 196 */         if (qp == p) {
/* 197 */           p.sendMessage(ChatColor.AQUA + "You are #" + a + " in line");
/* 198 */           break;
/*     */         }
/* 200 */         a++;
/*     */       }
/*     */     }
/*     */ 
/* 204 */     if (this.mode == GameMode.INGAME)
/* 205 */       p.sendMessage(ChatColor.RED + "Game already started!");
/* 206 */     else if (this.mode == GameMode.DISABLED)
/* 207 */       p.sendMessage(ChatColor.RED + "Arena disabled!");
/* 208 */     else if (this.mode == GameMode.RESETING)
/* 209 */       p.sendMessage(ChatColor.RED + "The arena is reseting!");
/*     */     else {
/* 211 */       p.sendMessage(ChatColor.RED + "Cannot join the game!");
/*     */     }
/* 213 */     return false;
/*     */   }
/*     */ 
/*     */   public void removeFromQueue(Player p) {
/* 217 */     this.queue.remove(p);
/*     */   }
/*     */ 
/*     */   public void vote(Player pl)
/*     */   {
/* 224 */     if (GameMode.STARTING == this.mode) { pl.sendMessage(ChatColor.GREEN + "Game already starting!"); return; }
/* 225 */     if (GameMode.WAITING != this.mode) { pl.sendMessage(ChatColor.GREEN + "Game already started!"); return; }
/* 226 */     if (this.voted.contains(pl)) {
/* 227 */       pl.sendMessage(ChatColor.RED + "You already voted!");
/* 228 */       return;
/*     */     }
/* 230 */     this.vote += 1;
/* 231 */     this.voted.add(pl);
/* 232 */     pl.sendMessage(ChatColor.GREEN + "Voted to start the game!");
/*     */ 
/* 237 */     if (((this.vote + 0.0D) / (getActivePlayers() + 0.0D) >= (this.c.getInt("auto-start-vote") + 0.0D) / 100.0D) && (getActivePlayers() > 1)) {
/* 238 */       countdown(this.c.getInt("auto-start-time"));
/* 239 */       for (Player p : this.activePlayers)
/* 240 */         p.sendMessage(ChatColor.LIGHT_PURPLE + "Game Starting in " + this.c.getInt("auto-start-time"));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removePlayer(Player p, boolean b)
/*     */   {
/* 248 */     if (this.mode == GameMode.INGAME) {
/* 249 */       p.teleport(SettingsManager.getInstance().getLobbySpawn());
/* 250 */       killPlayer(p, b);
/*     */     }
/*     */     else {
/* 253 */       this.sm.removePlayer(p, this.gameID);
/* 254 */       if (!b)
/* 255 */         p.teleport(SettingsManager.getInstance().getLobbySpawn());
/* 256 */       restoreInv(p);
/* 257 */       this.activePlayers.remove(p);
/* 258 */       this.inactivePlayers.remove(p);
/* 259 */       for (Object in : this.spawns.keySet().toArray()) {
/* 260 */         if (this.spawns.get(in) != p) continue; this.spawns.remove(in);
/*     */       }
/* 262 */       LobbyManager.getInstance().clearSigns();
/*     */     }
/* 264 */     GameManager.getInstance().removePlayerRefrence(p);
/*     */   }
/*     */ 
/*     */   public void playerLeave(Player p)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void killPlayer(Player p, boolean left)
/*     */   {
/* 275 */     GameManager.getInstance().removePlayerRefrence(p);
/* 276 */     clearInv(p);
/* 277 */     if (!left) {
/* 278 */       p.teleport(SettingsManager.getInstance().getLobbySpawn());
/*     */     }
/* 280 */     this.sm.playerDied(p, this.activePlayers.size(), this.gameID, new Date().getTime() - this.startTime);
/*     */ 
/* 282 */     if (!this.activePlayers.contains(p))
/* 283 */       return;
/* 284 */     if (!p.isOnline())
/* 285 */       restoreInvOffline(p.getName());
/*     */     else {
/* 287 */       restoreInv(p);
/*     */     }
/* 289 */     this.activePlayers.remove(p);
/* 290 */     this.inactivePlayers.add(p);
/* 291 */     if (left) {
/* 292 */       for (Player pl : getAllPlayers()) {
/* 293 */         pl.sendMessage(ChatColor.DARK_AQUA + p.getName() + " left the arena");
/*     */       }
/*     */ 
/*     */     }
/* 297 */     else if (this.mode != GameMode.WAITING) {
/* 298 */       String damagemsg = "";
/* 299 */       switch ($SWITCH_TABLE$org$bukkit$event$entity$EntityDamageEvent$DamageCause()[p.getLastDamageCause().getCause().ordinal()]) { case 11:
/* 300 */         damagemsg = "{player} Exploded!";
/* 301 */         break;
/*     */       case 10:
/* 302 */         damagemsg = "{player} Drowned!";
/* 303 */         break;
/*     */       case 2:
/* 304 */         damagemsg = EntDmgMsg(p, p.getLastDamageCause());
/* 305 */         break;
/*     */       case 5:
/* 306 */         damagemsg = "{player} hit the ground too hard!";
/* 307 */         break;
/*     */       case 9:
/* 308 */         damagemsg = "{player} burned in lava!";
/* 309 */         break;
/*     */       case 6:
/* 310 */         damagemsg = "{player} burned to death!";
/* 311 */         break;
/*     */       case 7:
/* 312 */         damagemsg = "{player} burned to death!";
/* 313 */         break;
/*     */       case 16:
/* 314 */         damagemsg = "{player} starved to death!";
/* 315 */         break;
/*     */       case 12:
/* 316 */         damagemsg = "{player} was creeper bombed!";
/* 317 */         break;
/*     */       case 3:
/*     */       case 4:
/*     */       case 8:
/*     */       case 13:
/*     */       case 14:
/*     */       case 15:
/*     */       default:
/* 318 */         damagemsg = "{player} died!";
/*     */       }
/*     */ 
/* 321 */       damagemsg = damagemsg.replace("{player}", (SurvivalGames.auth.contains(p.getName()) ? ChatColor.DARK_RED + ChatColor.BOLD : ChatColor.YELLOW) + p.getName() + ChatColor.RESET + ChatColor.DARK_AQUA);
/*     */ 
/* 323 */       if (getActivePlayers() > 1) {
/* 324 */         for (Iterator localIterator2 = getAllPlayers().iterator(); localIterator2.hasNext(); ) { pl = (Player)localIterator2.next();
/* 325 */           ((Player)pl).sendMessage(ChatColor.DARK_AQUA + damagemsg);
/* 326 */           ((Player)pl).sendMessage(ChatColor.DARK_AQUA + "There are " + ChatColor.YELLOW + getActivePlayers() + ChatColor.DARK_AQUA + " players remaining!");
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 332 */     for (Object pl = this.activePlayers.iterator(); ((Iterator)pl).hasNext(); ) { Player pe = (Player)((Iterator)pl).next();
/* 333 */       Location l = pe.getLocation();
/* 334 */       l.setY(l.getWorld().getMaxHeight());
/* 335 */       l.getWorld().strikeLightningEffect(l);
/*     */     }
/*     */ 
/* 338 */     if ((getActivePlayers() <= this.c.getInt("endgame.players")) && (this.c.getBoolean("endgame.fire-lighting.enabled")) && (!this.endgameRunning)) {
/* 339 */       this.endgameRunning = true;
/*     */ 
/* 341 */       new EndgameManager().start();
/*     */     }
/*     */ 
/* 346 */     if ((this.activePlayers.size() < 2) && (this.mode != GameMode.WAITING)) {
/* 347 */       playerWin(p);
/* 348 */       endGame();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String EntDmgMsg(Player p, EntityDamageEvent e) {
/* 353 */     if (e.getEntityType() == EntityType.PLAYER) {
/*     */       try {
/* 355 */         Player e1 = p.getKiller();
/* 356 */         this.sm.addKill(e1, p, this.gameID);
/* 357 */         Material m = e1.getItemInHand().getType();
/* 358 */         return (SurvivalGames.auth.contains(e1.getName()) ? ChatColor.DARK_RED + ChatColor.BOLD : ChatColor.YELLOW) + e1.getName() + ChatColor.RESET + ChatColor.DARK_AQUA + " Killed {player} with " + m + ". ";
/*     */       }
/*     */       catch (Exception e7) {
/* 361 */         return "{player} was killed by ";
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 366 */     String msg = "";
/* 367 */     switch ($SWITCH_TABLE$org$bukkit$entity$EntityType()[e.getEntityType().ordinal()]) {
/*     */     case 15:
/* 369 */       msg = "{player} was Creeper bombed!";
/* 370 */       break;
/*     */     case 21:
/* 372 */       msg = "{player} was fireballed by a ghast!";
/* 373 */       break;
/*     */     case 4:
/* 375 */       Player p5 = (Player)e;
/* 376 */       msg = p5.getName() + " shot {player}!";
/* 377 */       break;
/*     */     case 44:
/* 379 */       msg = "{player} was electrocuted!";
/* 380 */       break;
/*     */     case 24:
/* 382 */       msg = "{player} was killed by a Cave Spider!";
/* 383 */       break;
/*     */     default:
/* 385 */       msg = "{player} was killed by a " + e.getEntityType().toString().toLowerCase() + "!";
/*     */     }
/*     */ 
/* 388 */     return msg;
/*     */   }
/*     */ 
/*     */   public void playerWin(Player p)
/*     */   {
/* 394 */     if (GameMode.DISABLED == this.mode) return;
/* 395 */     Player win = (Player)this.activePlayers.get(0);
/*     */ 
/* 397 */     win.teleport(SettingsManager.getInstance().getLobbySpawn());
/*     */ 
/* 399 */     restoreInv(win);
/*     */ 
/* 401 */     Bukkit.getServer().broadcastMessage(ChatColor.DARK_AQUA + win.getName() + " won the Survival Games on arena " + this.gameID);
/* 402 */     LobbyManager.getInstance().showMessage(new String[] { win.getName(), "", "Won the ", "Survival Games!" });
/*     */ 
/* 404 */     this.mode = GameMode.FINISHING;
/*     */ 
/* 406 */     setRBStatus("Clearing Specs");
/* 407 */     clearSpecs();
/* 408 */     setRBStatus("Sw player;");
/* 409 */     this.sm.playerWin(win, this.gameID, new Date().getTime() - this.startTime);
/* 410 */     setRBStatus("Saving Game");
/* 411 */     this.sm.saveGame(this.gameID, win, getActivePlayers() + getInactivePlayers(), new Date().getTime() - this.startTime);
/* 412 */     setRBStatus("Clear active");
/*     */ 
/* 414 */     this.activePlayers.clear();
/* 415 */     setRBStatus("clear in");
/*     */ 
/* 417 */     this.inactivePlayers.clear();
/* 418 */     setRBStatus("clearing spawns");
/*     */ 
/* 420 */     this.spawns.clear();
/* 421 */     setRBStatus("loading spawns");
/*     */ 
/* 423 */     loadspawns();
/*     */   }
/*     */ 
/*     */   public void endGame() {
/* 427 */     this.mode = GameMode.WAITING;
/* 428 */     resetArena();
/* 429 */     LobbyManager.getInstance().clearSigns();
/* 430 */     this.endgameRunning = false;
/*     */   }
/*     */ 
/*     */   public void disable() {
/* 434 */     this.mode = GameMode.DISABLED;
/* 435 */     this.disabled = true;
/* 436 */     this.spawns.clear();
/*     */ 
/* 438 */     for (int a = 0; a < this.activePlayers.size(); a = 0)
/*     */       try
/*     */       {
/* 441 */         Player p = (Player)this.activePlayers.get(a);
/* 442 */         p.sendMessage(ChatColor.RED + "Game disabled");
/* 443 */         removePlayer(p, false);
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/*     */       }
/* 448 */     for (int a = 0; a < this.inactivePlayers.size(); a = 0)
/*     */       try
/*     */       {
/* 451 */         Player p = (Player)this.inactivePlayers.remove(a);
/* 452 */         p.sendMessage(ChatColor.RED + "Game disabled");
/*     */       }
/*     */       catch (Exception localException1)
/*     */       {
/*     */       }
/*     */     try
/*     */     {
/* 459 */       for (int a = 0; a < this.spectators.size(); a = 0) {
/* 460 */         String p = (String)this.spectators.get(a);
/* 461 */         Bukkit.getPlayer(p).sendMessage(ChatColor.RED + "Game disabled");
/* 462 */         removeSpectator(Bukkit.getPlayer(p));
/*     */       } } catch (Exception localException2) {
/*     */     }
/* 464 */     this.queue.clear();
/* 465 */     resetArena();
/* 466 */     GameManager.getInstance().clearPlayerCache();
/*     */   }
/*     */ 
/*     */   public void resetArena() {
/* 470 */     this.vote = 0;
/* 471 */     this.voted.clear();
/*     */ 
/* 473 */     this.mode = GameMode.RESETING;
/* 474 */     setRBStatus("starting");
/* 475 */     this.endgameRunning = false;
/* 476 */     Bukkit.getScheduler().cancelTask(this.endgameTaskID);
/* 477 */     GameManager.getInstance().gameEndCallBack(this.gameID);
/* 478 */     setRBStatus("starting...");
/* 479 */     GameReset r = new GameReset(this);
/* 480 */     r.resetArena();
/*     */   }
/*     */ 
/*     */   public void resetCallback() {
/* 484 */     if (!this.disabled)
/* 485 */       enable();
/*     */     else
/* 487 */       this.mode = GameMode.DISABLED;
/*     */   }
/*     */ 
/*     */   public void messageAll(String msg)
/*     */   {
/* 494 */     for (Player p : getAllPlayers())
/* 495 */       p.sendMessage(msg);
/*     */   }
/*     */ 
/*     */   public void saveInv(Player p)
/*     */   {
/* 502 */     ItemStack[][] store = new ItemStack[2][1];
/*     */ 
/* 504 */     store[0] = p.getInventory().getContents();
/* 505 */     store[1] = p.getInventory().getArmorContents();
/*     */ 
/* 507 */     this.inv_store.put(p, store);
/*     */   }
/*     */ 
/*     */   public void restoreInvOffline(String p)
/*     */   {
/* 512 */     restoreInv(Bukkit.getPlayer(p));
/*     */   }
/*     */ 
/*     */   public void addSpectator(Player p)
/*     */   {
/* 517 */     if (this.mode != GameMode.INGAME) {
/* 518 */       p.sendMessage(ChatColor.RED + "You Can only spectate running games!");
/* 519 */       return;
/*     */     }
/* 521 */     saveInv(p);
/* 522 */     clearInv(p);
/* 523 */     p.teleport(SettingsManager.getInstance().getSpawnPoint(this.gameID, 1).add(0.0D, 10.0D, 0.0D));
/*     */ 
/* 526 */     for (Player pl : Bukkit.getOnlinePlayers()) {
/* 527 */       pl.hidePlayer(p);
/*     */     }
/* 529 */     for (int a = 0; a < 9; a++) {
/* 530 */       p.getInventory().setItem(a, new ItemStack(59, 1));
/*     */     }
/* 532 */     p.updateInventory();
/*     */ 
/* 534 */     p.setAllowFlight(true);
/* 535 */     p.setFlying(true);
/* 536 */     this.spectators.add(p.getName());
/* 537 */     p.sendMessage(ChatColor.GREEN + "You are now spectating! /sg spectate again to return to lobby");
/* 538 */     p.sendMessage(ChatColor.GREEN + "Right click while holding shift to teleport to the next ingame player, left click to go back!");
/*     */ 
/* 540 */     this.nextspec.put(p, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   public void removeSpectator(Player p)
/*     */   {
/* 545 */     ArrayList players = new ArrayList();
/* 546 */     players.addAll(this.activePlayers);
/* 547 */     players.addAll(this.inactivePlayers);
/*     */ 
/* 549 */     for (Player pl : Bukkit.getOnlinePlayers()) {
/* 550 */       pl.showPlayer(p);
/*     */     }
/*     */ 
/* 553 */     restoreInv(p);
/* 554 */     p.setAllowFlight(false);
/* 555 */     p.setFlying(false);
/* 556 */     p.setFallDistance(0.0F);
/* 557 */     p.setHealth(20);
/* 558 */     p.setFoodLevel(20);
/* 559 */     p.setSaturation(20.0F);
/* 560 */     p.teleport(SettingsManager.getInstance().getLobbySpawn());
/*     */ 
/* 562 */     for (String pl : (String[])this.spectators.toArray(new String[0])) {
/* 563 */       if (pl.equals(p.getName())) {
/* 564 */         this.spectators.remove(pl);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 569 */     this.nextspec.remove(p);
/*     */   }
/*     */ 
/*     */   public void clearSpecs() {
/* 573 */     Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("SurvivalGames"), new Runnable() {
/*     */       public void run() {
/* 575 */         ArrayList players = new ArrayList();
/* 576 */         players.addAll(Game.this.activePlayers);
/* 577 */         players.addAll(Game.this.inactivePlayers);
/*     */ 
/* 579 */         for (String p : Game.this.spectators)
/*     */           try {
/* 581 */             for (Player pl : players) {
/* 582 */               pl.showPlayer(Bukkit.getPlayer(p));
/*     */             }
/*     */ 
/* 585 */             Player player2 = Bukkit.getPlayer(p);
/* 586 */             Game.this.restoreInv(player2);
/* 587 */             player2.setAllowFlight(false);
/* 588 */             player2.setFlying(false);
/* 589 */             player2.setFallDistance(0.0F);
/* 590 */             player2.setHealth(20);
/* 591 */             player2.setFoodLevel(20);
/* 592 */             player2.setSaturation(20.0F);
/* 593 */             player2.teleport(SettingsManager.getInstance().getLobbySpawn());
/*     */           } catch (Exception localException) {
/*     */           }
/* 596 */         Game.this.spectators.clear();
/* 597 */         Game.this.nextspec.clear();
/*     */       }
/*     */     }
/*     */     , 100L);
/*     */   }
/*     */ 
/*     */   public HashMap<Player, Integer> getNextSpec() {
/* 603 */     return this.nextspec;
/*     */   }
/*     */ 
/*     */   public void restoreInv(Player p)
/*     */   {
/*     */     try {
/* 609 */       clearInv(p);
/* 610 */       p.getInventory().setContents(((ItemStack[][])this.inv_store.get(p))[0]);
/* 611 */       p.getInventory().setArmorContents(((ItemStack[][])this.inv_store.get(p))[1]);
/* 612 */       this.inv_store.remove(p);
/* 613 */       p.updateInventory();
/*     */     } catch (Exception localException) {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void clearInv(Player p) {
/* 619 */     ItemStack[] inv = p.getInventory().getContents();
/* 620 */     for (int i = 0; i < inv.length; i++) {
/* 621 */       inv[i] = null;
/*     */     }
/* 623 */     p.getInventory().setContents(inv);
/* 624 */     inv = p.getInventory().getArmorContents();
/* 625 */     for (int i = 0; i < inv.length; i++) {
/* 626 */       inv[i] = null;
/*     */     }
/* 628 */     p.getInventory().setArmorContents(inv);
/* 629 */     p.updateInventory();
/*     */   }
/*     */ 
/*     */   public void startGame()
/*     */   {
/* 634 */     if (this.mode == GameMode.INGAME) {
/* 635 */       return;
/*     */     }
/*     */ 
/* 638 */     if (this.activePlayers.size() <= 1) {
/* 639 */       for (Player pl : this.activePlayers) {
/* 640 */         pl.sendMessage(ChatColor.RED + "Not Enought Players!");
/*     */       }
/* 642 */       return;
/*     */     }
/*     */ 
/* 645 */     this.startTime = new Date().getTime();
/* 646 */     for (Player pl : this.activePlayers) {
/* 647 */       pl.setHealth(20);
/* 648 */       pl.setHealth(20);
/* 649 */       clearInv(pl);
/* 650 */       pl.sendMessage(ChatColor.AQUA + "Good Luck!");
/*     */     }
/* 652 */     if (SettingsManager.getInstance().getConfig().getBoolean("restock-chest")) {
/* 653 */       SettingsManager.getGameWorld(this.gameID).setTime(0L);
/* 654 */       this.gcount += 1;
/* 655 */       new NightChecker().start();
/*     */     }
/* 657 */     if (SettingsManager.getInstance().getConfig().getInt("grace-period") != 0) {
/* 658 */       for (Player play : this.activePlayers) {
/* 659 */         play.sendMessage(ChatColor.LIGHT_PURPLE + "You have a " + SettingsManager.getInstance().getConfig().getInt("grace-period") + " second grace period!");
/*     */       }
/* 661 */       Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(GameManager.getInstance().getPlugin(), new Runnable() {
/*     */         public void run() {
/* 663 */           for (Player play : Game.this.activePlayers)
/* 664 */             play.sendMessage(ChatColor.LIGHT_PURPLE + "Grace period has ended!");
/*     */         }
/*     */       }
/*     */       , SettingsManager.getInstance().getConfig().getInt("grace-period") * 20);
/*     */     }
/*     */ 
/* 671 */     this.mode = GameMode.INGAME;
/*     */   }
/*     */ 
/*     */   public int getCountdownTime()
/*     */   {
/* 677 */     return this.counttime;
/*     */   }
/*     */ 
/*     */   public void countdown(int time)
/*     */   {
/* 683 */     this.threadsync += 1;
/* 684 */     this.mode = GameMode.STARTING;
/* 685 */     this.countdownRunning = true;
/* 686 */     this.counttime = time;
/* 687 */     if (time < 11) {
/* 688 */       for (Player p : this.activePlayers) {
/* 689 */         p.sendMessage(ChatColor.DARK_BLUE + "Game Starting in " + time);
/*     */       }
/*     */     }
/* 692 */     if ((SurvivalGames.isActive()) && (time > 0)) {
/* 693 */       new CountdownThread(time).start();
/*     */     }
/* 695 */     else if ((SurvivalGames.isActive()) && (time <= 0)) {
/* 696 */       this.countdownRunning = false;
/* 697 */       startGame();
/*     */     }
/*     */     else {
/* 700 */       return;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isBlockInArena(Location v)
/*     */   {
/* 759 */     return this.arena.containsBlock(v);
/*     */   }
/*     */ 
/*     */   public boolean isProtectionOn() {
/* 763 */     long t = this.startTime / 1000L;
/* 764 */     long l = SettingsManager.getInstance().getConfig().getLong("grace-period");
/* 765 */     long d = new Date().getTime() / 1000L;
/*     */ 
/* 767 */     return d - t < l;
/*     */   }
/*     */ 
/*     */   public int getID()
/*     */   {
/* 773 */     return this.gameID;
/*     */   }
/*     */ 
/*     */   public int getActivePlayers()
/*     */   {
/* 778 */     return this.activePlayers.size();
/*     */   }
/*     */ 
/*     */   public int getInactivePlayers() {
/* 782 */     return this.inactivePlayers.size();
/*     */   }
/*     */ 
/*     */   public Player[][] getPlayers() {
/* 786 */     return new Player[][] { (Player[])this.activePlayers.toArray(new Player[0]), (Player[])this.inactivePlayers.toArray(new Player[0]) };
/*     */   }
/*     */ 
/*     */   public ArrayList<Player> getAllPlayers() {
/* 790 */     ArrayList all = new ArrayList();
/* 791 */     all.addAll(this.activePlayers);
/* 792 */     all.addAll(this.inactivePlayers);
/* 793 */     return all;
/*     */   }
/*     */ 
/*     */   public boolean isSpectator(Player p) {
/* 797 */     return this.spectators.contains(p.getName());
/*     */   }
/*     */ 
/*     */   public boolean isInQueue(Player p) {
/* 801 */     return this.queue.contains(p);
/*     */   }
/*     */ 
/*     */   public boolean isPlayerActive(Player player) {
/* 805 */     return this.activePlayers.contains(player);
/*     */   }
/*     */   public boolean isPlayerinactive(Player player) {
/* 808 */     return this.inactivePlayers.contains(player);
/*     */   }
/*     */   public boolean hasPlayer(Player p) {
/* 811 */     return (this.activePlayers.contains(p)) || (this.inactivePlayers.contains(p));
/*     */   }
/*     */   public GameMode getMode() {
/* 814 */     return this.mode;
/*     */   }
/*     */ 
/*     */   public synchronized void setRBPercent(double d) {
/* 818 */     this.rbpercent = d;
/*     */   }
/*     */ 
/*     */   public double getRBPercent()
/*     */   {
/* 823 */     return this.rbpercent;
/*     */   }
/*     */ 
/*     */   public void setRBStatus(String s) {
/* 827 */     this.rbstatus = s;
/*     */   }
/*     */ 
/*     */   public String getRBStatus() {
/* 831 */     return this.rbstatus;
/*     */   }
/*     */ 
/*     */   class CountdownThread extends Thread
/*     */   {
/*     */     int time;
/* 706 */     int trun = Game.this.threadsync;
/*     */ 
/* 708 */     public CountdownThread(int t) { this.time = t; }
/*     */ 
/*     */     public void run() {
/* 711 */       this.time -= 1;
/*     */       try { Thread.sleep(1000L); } catch (Exception localException) {
/* 713 */       }if (this.trun == Game.this.threadsync)
/* 714 */         Game.this.countdown(this.time);
/*     */     }
/*     */   }
/*     */ 
/*     */   class EndgameManager extends Thread
/*     */   {
/*     */     EndgameManager()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 744 */       while (Game.this.endgameRunning) {
/* 745 */         for (Player player : (Player[])Game.this.activePlayers.toArray(new Player[0])) {
/* 746 */           Location l = player.getLocation();
/* 747 */           l.add(0.0D, 5.0D, 0.0D);
/* 748 */           player.getWorld().strikeLightningEffect(l);
/*     */         }try {
/* 750 */           Thread.sleep(SettingsManager.getInstance().getConfig().getInt("endgame.fire-lighting.interval") * 1000);
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum GameMode
/*     */   {
/*  31 */     DISABLED, LOADING, INACTIVE, WAITING, 
/*  32 */     STARTING, INGAME, FINISHING, RESETING, ERROR;
/*     */   }
/*     */ 
/*     */   class NightChecker extends Thread
/*     */   {
/* 721 */     boolean reset = false;
/* 722 */     int tgc = Game.this.gcount;
/*     */ 
/*     */     NightChecker() {  } 
/* 724 */     public void run() { while ((!this.reset) && (Game.this.mode == Game.GameMode.INGAME) && (this.tgc == Game.this.gcount)) {
/*     */         try { Thread.sleep(5000L); } catch (Exception localException) {
/* 726 */         }if (SettingsManager.getGameWorld(Game.this.gameID).getTime() > 14000L) {
/* 727 */           for (Player pl : Game.this.activePlayers) {
/* 728 */             pl.sendMessage(ChatColor.AQUA + "Chest have been restocked!");
/*     */           }
/* 730 */           ((HashSet)GameManager.openedChest.get(Integer.valueOf(Game.this.gameID))).clear();
/* 731 */           this.reset = true;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.Game
 * JD-Core Version:    0.6.0
 */