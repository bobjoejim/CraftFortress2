/*     */ package com.skitscape.survivalgames.stats;
/*     */ 
/*     */ import com.skitscape.survivalgames.Game;
/*     */ import com.skitscape.survivalgames.GameManager;
/*     */ import com.skitscape.survivalgames.SettingsManager;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ public class PlayerStatsSession
/*     */ {
/*     */   public Player player;
/*  18 */   public int kills = 0; public int death = 0;
/*     */   public int gameno;
/*     */   public int arenaid;
/*  18 */   public int points = 0;
/*  19 */   public int finish = 0;
/*  20 */   long time = 0L;
/*  21 */   int ksbon = 0;
/*  22 */   long lastkill = 0L;
/*  23 */   int kslevel = 0;
/*  24 */   int score = 0;
/*  25 */   int position = 0;
/*  26 */   int pppoints = 0;
/*     */ 
/*  28 */   private ArrayList<Player> killed = new ArrayList();
/*     */ 
/*  31 */   private HashMap<Integer, Integer> kslist = new HashMap();
/*     */ 
/*     */   public PlayerStatsSession(Player p, int arenaid)
/*     */   {
/*  40 */     this.player = p;
/*  41 */     this.arenaid = arenaid;
/*     */ 
/*  44 */     this.kslist.put(Integer.valueOf(1), Integer.valueOf(0));
/*  45 */     this.kslist.put(Integer.valueOf(2), Integer.valueOf(0));
/*  46 */     this.kslist.put(Integer.valueOf(3), Integer.valueOf(0));
/*  47 */     this.kslist.put(Integer.valueOf(4), Integer.valueOf(0));
/*  48 */     this.kslist.put(Integer.valueOf(5), Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   public void setGameID(int gameid)
/*     */   {
/*  54 */     this.gameno = gameid;
/*     */   }
/*     */ 
/*     */   public int addKill(Player p)
/*     */   {
/*  59 */     this.killed.add(p);
/*  60 */     this.kills += 1;
/*  61 */     checkKS();
/*  62 */     this.lastkill = new Date().getTime();
/*  63 */     return this.kslevel;
/*     */   }
/*     */ 
/*     */   public void win(long time) {
/*  67 */     this.position = 1;
/*  68 */     this.time = time;
/*     */   }
/*     */ 
/*     */   public void died(int pos, long time) {
/*  72 */     this.time = time;
/*  73 */     this.death = 1;
/*  74 */     this.position = pos;
/*  75 */     this.pppoints = GameManager.getInstance().getGame(this.arenaid).getInactivePlayers();
/*     */   }
/*     */ 
/*     */   public void setTime(long time) {
/*  79 */     this.time = time;
/*     */   }
/*     */ 
/*     */   public void addkillStreak(int ks)
/*     */   {
/*  84 */     this.ksbon += SettingsManager.getInstance().getConfig().getInt("stats.points.killstreak.base") * (SettingsManager.getInstance().getConfig().getInt("stats.points.killstreak.multiplier") + ks);
/*  85 */     int level = ks;
/*  86 */     if (level > 5) level = 5;
/*  87 */     this.kslist.put(Integer.valueOf(level), Integer.valueOf(((Integer)this.kslist.get(Integer.valueOf(level))).intValue() + 1));
/*  88 */     if (level < 4) {
/*  89 */       for (Player p : GameManager.getInstance().getGame(this.arenaid).getAllPlayers()) {
/*  90 */         p.sendMessage(SettingsManager.getInstance().getConfig().getString("stats.killstreak.level" + level).replace("{player}", this.player.getName()).replaceAll("(&([a-fk-or0-9]))", "§$2"));
/*     */       }
/*     */     }
/*     */     else {
/*  94 */       Bukkit.getServer().broadcastMessage(SettingsManager.getInstance().getConfig().getString("stats.killstreak.level" + level).replace("{player}", this.player.getName()).replaceAll("(&([a-fk-or0-9]))", "§$2"));
/*     */     }
/*  96 */     this.lastkill = new Date().getTime();
/*     */   }
/*     */ 
/*     */   public void calcPoints()
/*     */   {
/* 104 */     FileConfiguration c = SettingsManager.getInstance().getConfig();
/* 105 */     int kpoints = this.kills * c.getInt("stats.points.kill");
/* 106 */     int ppoints = this.pppoints * c.getInt("stats.points.position");
/* 107 */     int kspoints = this.ksbon;
/*     */ 
/* 109 */     this.points = (kpoints + ppoints + kspoints + this.ksbon);
/*     */ 
/* 112 */     if (this.position == 1)
/* 113 */       this.points += c.getInt("stats.points.win");
/*     */   }
/*     */ 
/*     */   public boolean checkKS()
/*     */   {
/* 119 */     if (15000L > new Date().getTime() - this.lastkill) {
/* 120 */       this.kslevel += 1;
/* 121 */       addkillStreak(this.kslevel);
/*     */ 
/* 123 */       return true;
/*     */     }
/*     */ 
/* 126 */     this.kslevel = 0;
/* 127 */     return false;
/*     */   }
/*     */ 
/*     */   public String createQuery()
/*     */   {
/* 132 */     calcPoints();
/* 133 */     String query = "INSERT INTO " + SettingsManager.getSqlPrefix() + "playerstats VALUES(NULL,";
/* 134 */     query = query + this.gameno + "," + this.arenaid + ",'" + this.player.getName() + "'," + this.points + "," + this.position + "," + this.kills + "," + this.death + ",";
/* 135 */     String killeds = "'";
/* 136 */     for (Player p : this.killed) {
/* 137 */       killeds = killeds + (killeds.length() > 2 ? ":" : "") + p.getName();
/*     */     }
/*     */ 
/* 140 */     query = query + killeds + "'," + this.time;
/* 141 */     query = query + "," + this.kslist.get(Integer.valueOf(1)) + "," + this.kslist.get(Integer.valueOf(2)) + "," + this.kslist.get(Integer.valueOf(3)) + "," + this.kslist.get(Integer.valueOf(4)) + "," + this.kslist.get(Integer.valueOf(5)) + ")";
/*     */ 
/* 146 */     return query;
/*     */   }
/*     */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.stats.PlayerStatsSession
 * JD-Core Version:    0.6.0
 */