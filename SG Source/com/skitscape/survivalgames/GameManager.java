/*     */ package com.skitscape.survivalgames;
/*     */ 
/*     */ import com.sk89q.worldedit.bukkit.WorldEditPlugin;
/*     */ import com.sk89q.worldedit.bukkit.selections.Selection;
/*     */ import com.skitscape.survivalgames.stats.StatsManager;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ 
/*     */ public class GameManager
/*     */ {
/*  24 */   static GameManager instance = new GameManager();
/*  25 */   private ArrayList<Game> games = new ArrayList();
/*     */   private SurvivalGames p;
/*  27 */   public static HashMap<Integer, HashSet<Block>> openedChest = new HashMap();
/*  28 */   private ArrayList<Integer> gamemap = new ArrayList();
/*  29 */   private HashMap<Player, Integer> PGRC = new HashMap();
/*     */ 
/*     */   public static GameManager getInstance()
/*     */   {
/*  36 */     return instance;
/*     */   }
/*     */ 
/*     */   public void setup(SurvivalGames plugin) {
/*  40 */     this.p = plugin;
/*  41 */     LoadGames();
/*  42 */     for (Game g : getGames())
/*  43 */       openedChest.put(Integer.valueOf(g.getID()), new HashSet());
/*     */   }
/*     */ 
/*     */   public Plugin getPlugin()
/*     */   {
/*  48 */     return this.p;
/*     */   }
/*     */ 
/*     */   public void reloadGames() {
/*  52 */     LoadGames();
/*     */   }
/*     */ 
/*     */   public void LoadGames()
/*     */   {
/*  57 */     FileConfiguration c = SettingsManager.getInstance().getSystemConfig();
/*  58 */     this.games.clear();
/*  59 */     int no = c.getInt("sg-system.arenano", 0);
/*  60 */     int loaded = 0;
/*  61 */     int a = 1;
/*  62 */     while (loaded < no) {
/*  63 */       if (c.isSet("sg-system.arenas." + a + ".x1"))
/*     */       {
/*  65 */         if (c.getBoolean("sg-system.arenas." + a + ".enabled"))
/*     */         {
/*  68 */           System.out.println("Loading Arena: " + a);
/*  69 */           loaded++;
/*  70 */           this.games.add(new Game(a));
/*  71 */           StatsManager.getInstance().addArena(a);
/*     */         }
/*     */       }
/*  74 */       a++;
/*     */     }
/*  76 */     LobbyManager.getInstance().clearSigns();
/*     */   }
/*     */ 
/*     */   public int getBlockGameId(Location v)
/*     */   {
/*  81 */     for (Game g : this.games) {
/*  82 */       if (g.isBlockInArena(v)) {
/*  83 */         return g.getID();
/*     */       }
/*     */     }
/*  86 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getPlayerGameId(Player p)
/*     */   {
/*  93 */     for (Game g : this.games) {
/*  94 */       if (g.isPlayerActive(p)) {
/*  95 */         this.PGRC.put(p, Integer.valueOf(g.getID()));
/*  96 */         return g.getID();
/*     */       }
/*     */     }
/*  99 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getPlayerSpectateId(Player p) {
/* 103 */     for (Game g : this.games) {
/* 104 */       if (g.isSpectator(p)) {
/* 105 */         return g.getID();
/*     */       }
/*     */     }
/* 108 */     return -1;
/*     */   }
/*     */ 
/*     */   public boolean isPlayerActive(Player player) {
/* 112 */     for (Game g : this.games) {
/* 113 */       if (g.isPlayerActive(player)) {
/* 114 */         return true;
/*     */       }
/*     */     }
/* 117 */     return false;
/*     */   }
/*     */   public boolean isPlayerInactive(Player player) {
/* 120 */     for (Game g : this.games) {
/* 121 */       if (g.isPlayerActive(player)) {
/* 122 */         return true;
/*     */       }
/*     */     }
/* 125 */     return false;
/*     */   }
/*     */   public boolean isSpectator(Player player) {
/* 128 */     for (Game g : this.games) {
/* 129 */       if (g.isSpectator(player)) {
/* 130 */         return true;
/*     */       }
/*     */     }
/* 133 */     return false;
/*     */   }
/*     */ 
/*     */   public void removeFromOtherQueues(Player p, int id) {
/* 137 */     for (Game g : getGames())
/* 138 */       if ((g.isInQueue(p)) && (g.getID() != id)) {
/* 139 */         g.removeFromQueue(p);
/* 140 */         p.sendMessage(ChatColor.AQUA + "Removed from the queue in arena " + g.getID());
/*     */       }
/*     */   }
/*     */ 
/*     */   public int getGameCount()
/*     */   {
/* 146 */     return this.games.size();
/*     */   }
/*     */ 
/*     */   public Game getGame(int a)
/*     */   {
/* 152 */     for (Game g : this.games) {
/* 153 */       if (g.getID() == a) {
/* 154 */         return g;
/*     */       }
/*     */     }
/* 157 */     return null;
/*     */   }
/*     */ 
/*     */   public void removePlayer(Player p, boolean b) {
/* 161 */     getGame(getPlayerGameId(p)).removePlayer(p, b);
/* 162 */     removePlayerRefrence(p);
/*     */   }
/*     */   public void removeSpectator(Player p) {
/* 165 */     getGame(getPlayerSpectateId(p)).removeSpectator(p);
/*     */   }
/*     */ 
/*     */   public void disableGame(int id) {
/* 169 */     getGame(id).disable();
/*     */   }
/*     */ 
/*     */   public void enableGame(int id) {
/* 173 */     getGame(id).enable();
/*     */   }
/*     */ 
/*     */   public ArrayList<Game> getGames()
/*     */   {
/* 178 */     return this.games;
/*     */   }
/*     */   public Game.GameMode getGameMode(int a) {
/* 181 */     for (Game g : this.games) {
/* 182 */       if (g.getID() == a) {
/* 183 */         return g.getMode();
/*     */       }
/*     */     }
/* 186 */     return null;
/*     */   }
/*     */ 
/*     */   public void startGame(int a)
/*     */   {
/* 191 */     getGame(a).countdown(10);
/*     */   }
/*     */ 
/*     */   public void addPlayer(Player p, int g) {
/* 195 */     Game game = getGame(g);
/* 196 */     if (game == null) {
/* 197 */       p.sendMessage(ChatColor.RED + "Game does not exist");
/* 198 */       return;
/*     */     }
/* 200 */     getGame(g).addPlayer(p);
/*     */   }
/*     */ 
/*     */   public void autoAddPlayer(Player pl) {
/* 204 */     ArrayList qg = new ArrayList(5);
/* 205 */     for (Game g : this.games) {
/* 206 */       if (g.getMode() == Game.GameMode.WAITING) {
/* 207 */         qg.add(g);
/*     */       }
/*     */     }
/*     */ 
/* 211 */     if (qg.size() == 0) {
/* 212 */       pl.sendMessage(ChatColor.RED + "No games to join");
/* 213 */       return;
/*     */     }
/*     */ 
/* 216 */     ((Game)qg.get(0)).addPlayer(pl);
/*     */   }
/*     */ 
/*     */   public WorldEditPlugin getWorldEdit()
/*     */   {
/* 221 */     return this.p.getWorldEdit();
/*     */   }
/*     */ 
/*     */   public void createArenaFromSelection(Player pl) {
/* 225 */     FileConfiguration c = SettingsManager.getInstance().getSystemConfig();
/* 226 */     SettingsManager s = SettingsManager.getInstance();
/*     */ 
/* 228 */     WorldEditPlugin we = this.p.getWorldEdit();
/* 229 */     Selection sel = we.getSelection(pl);
/* 230 */     if (sel == null) {
/* 231 */       pl.sendMessage(ChatColor.RED + "You must make a WorldEdit Selection first");
/* 232 */       return;
/*     */     }
/* 234 */     Location max = sel.getMaximumPoint();
/* 235 */     Location min = sel.getMinimumPoint();
/*     */ 
/* 243 */     int no = c.getInt("sg-system.arenano") + 1;
/* 244 */     c.set("sg-system.arenano", Integer.valueOf(no));
/* 245 */     if (this.games.size() == 0) {
/* 246 */       no = 1;
/*     */     }
/*     */     else
/* 249 */       no = ((Game)this.games.get(this.games.size() - 1)).getID() + 1;
/* 250 */     SettingsManager.getInstance().getSpawns().set("spawns." + no, null);
/* 251 */     c.set("sg-system.arenas." + no + ".world", max.getWorld().getName());
/* 252 */     c.set("sg-system.arenas." + no + ".x1", Integer.valueOf(max.getBlockX()));
/* 253 */     c.set("sg-system.arenas." + no + ".y1", Integer.valueOf(max.getBlockY()));
/* 254 */     c.set("sg-system.arenas." + no + ".z1", Integer.valueOf(max.getBlockZ()));
/* 255 */     c.set("sg-system.arenas." + no + ".x2", Integer.valueOf(min.getBlockX()));
/* 256 */     c.set("sg-system.arenas." + no + ".y2", Integer.valueOf(min.getBlockY()));
/* 257 */     c.set("sg-system.arenas." + no + ".z2", Integer.valueOf(min.getBlockZ()));
/* 258 */     c.set("sg-system.arenas." + no + ".enabled", Boolean.valueOf(true));
/*     */ 
/* 260 */     SettingsManager.getInstance().saveSystemConfig();
/*     */ 
/* 262 */     hotAddArena(no);
/*     */ 
/* 265 */     pl.sendMessage(ChatColor.GREEN + "Arena ID " + no + " Succesfully added");
/*     */   }
/*     */ 
/*     */   private void hotAddArena(int no)
/*     */   {
/* 270 */     Game game = new Game(no);
/* 271 */     this.games.add(game);
/* 272 */     StatsManager.getInstance().addArena(no);
/*     */   }
/*     */ 
/*     */   public void hotRemoveArena(int no)
/*     */   {
/* 278 */     for (Game g : (Game[])this.games.toArray(new Game[0]))
/* 279 */       if (g.getID() == no)
/* 280 */         this.games.remove(getGame(no));
/*     */   }
/*     */ 
/*     */   public void gameEndCallBack(int id)
/*     */   {
/* 286 */     getGame(id).setRBStatus("clearing chest");
/* 287 */     clearPlayerCache();
/* 288 */     openedChest.put(Integer.valueOf(id), new HashSet());
/*     */   }
/*     */ 
/*     */   public void clearPlayerCache()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void removePlayerRefrence(Player p)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.GameManager
 * JD-Core Version:    0.6.0
 */