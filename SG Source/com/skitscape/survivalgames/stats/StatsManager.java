/*     */ package com.skitscape.survivalgames.stats;
/*     */ 
/*     */ import com.skitscape.survivalgames.Game;
/*     */ import com.skitscape.survivalgames.GameManager;
/*     */ import com.skitscape.survivalgames.SettingsManager;
/*     */ import com.skitscape.survivalgames.util.DatabaseManager;
/*     */ import java.io.PrintStream;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ 
/*     */ public class StatsManager
/*     */ {
/*  29 */   private static StatsManager instance = new StatsManager();
/*     */ 
/*  31 */   private ArrayList<PreparedStatement> queue = new ArrayList();
/*     */ 
/*  33 */   private DatabaseDumper dumper = new DatabaseDumper();
/*     */ 
/*  35 */   private DatabaseManager dbman = DatabaseManager.getInstance();
/*     */ 
/*  39 */   private HashMap<Integer, HashMap<Player, PlayerStatsSession>> arenas = new HashMap();
/*     */   private Plugin plugin;
/*  43 */   private boolean enabled = true;
/*     */ 
/*     */   public static StatsManager getInstance()
/*     */   {
/*  50 */     return instance;
/*     */   }
/*     */ 
/*     */   public void setup(Plugin p, boolean b) {
/*  54 */     this.enabled = b;
/*  55 */     if (b) {
/*  56 */       this.plugin = p;
/*     */       try {
/*  58 */         PreparedStatement s = this.dbman.createStatement(" CREATE TABLE " + SettingsManager.getSqlPrefix() + 
/*  59 */           "playerstats(id int NOT NULL AUTO_INCREMENT PRIMARY KEY, gameno int,arenaid int, player text, points int,position int,  kills int, death int, killed text,time int, ks1 int, ks2 int,ks3 int, ks4 int, ks5 int)");
/*     */ 
/*  61 */         PreparedStatement s1 = this.dbman.createStatement(" CREATE TABLE " + SettingsManager.getSqlPrefix() + 
/*  62 */           "gamestats(gameno int NOT NULL AUTO_INCREMENT PRIMARY KEY, arenaid int, players int, winner text, time int )");
/*     */ 
/*  65 */         DatabaseMetaData dbm = this.dbman.getMysqlConnection().getMetaData();
/*  66 */         ResultSet tables = dbm.getTables(null, null, SettingsManager.getSqlPrefix() + "playerstats", null);
/*  67 */         ResultSet tables1 = dbm.getTables(null, null, SettingsManager.getSqlPrefix() + "gamestats", null);
/*     */ 
/*  69 */         if (!tables.next())
/*     */         {
/*  73 */           s.execute();
/*     */         }
/*  75 */         if (!tables1.next())
/*     */         {
/*  79 */           s1.execute();
/*     */         }
/*     */       } catch (Exception e) {
/*  81 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addArena(int arenaid)
/*     */   {
/*  90 */     this.arenas.put(Integer.valueOf(arenaid), new HashMap());
/*     */   }
/*     */ 
/*     */   public void addPlayer(Player p, int arenaid)
/*     */   {
/*  96 */     ((HashMap)this.arenas.get(Integer.valueOf(arenaid))).put(p, new PlayerStatsSession(p, arenaid));
/*     */   }
/*     */ 
/*     */   public void removePlayer(Player p, int id) {
/* 100 */     ((HashMap)this.arenas.get(Integer.valueOf(id))).remove(p);
/*     */   }
/*     */ 
/*     */   public void playerDied(Player p, int pos, int arenaid, long time)
/*     */   {
/* 108 */     ((PlayerStatsSession)((HashMap)this.arenas.get(Integer.valueOf(arenaid))).get(p)).died(pos, time);
/*     */   }
/*     */ 
/*     */   public void playerWin(Player p, int arenaid, long time) {
/* 112 */     ((PlayerStatsSession)((HashMap)this.arenas.get(Integer.valueOf(arenaid))).get(p)).win(time);
/*     */   }
/*     */ 
/*     */   public void addKill(Player p, Player killed, int arenaid)
/*     */   {
/* 119 */     PlayerStatsSession s = (PlayerStatsSession)((HashMap)this.arenas.get(Integer.valueOf(arenaid))).get(p);
/*     */ 
/* 121 */     int kslevel = s.addKill(killed);
/* 122 */     if (kslevel > 0) {
/* 123 */       String msg = SettingsManager.getInstance().getConfig().getString("stats.killstreaks.level" + (kslevel > 5 ? 5 : kslevel));
/* 124 */       msg.replace("{player}", p.getName());
/* 125 */       GameManager.getInstance().getGame(arenaid).messageAll(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void saveGame(int arenaid, Player winner, int players, long time)
/*     */   {
/* 134 */     if (!this.enabled) return;
/* 135 */     int gameno = 0;
/* 136 */     Game g = GameManager.getInstance().getGame(arenaid);
/*     */     try
/*     */     {
/* 139 */       g.setRBStatus("Geting no");
/* 140 */       long time1 = new Date().getTime();
/* 141 */       PreparedStatement s2 = this.dbman.createStatement("SELECT * FROM " + SettingsManager.getSqlPrefix() + 
/* 142 */         "gamestats ORDER BY gameno DESC LIMIT 1");
/* 143 */       ResultSet rs = s2.executeQuery();
/* 144 */       rs.next();
/* 145 */       gameno = rs.getInt(1) + 1;
/* 146 */       g.setRBStatus("Got no");
/*     */ 
/* 148 */       if (time1 + 5000L < new Date().getTime()) System.out.println("Your database took a long time to respond. Check the connection between the server and database"); 
/*     */     }
/*     */     catch (SQLException e)
/*     */     {
/* 151 */       e.printStackTrace();
/* 152 */       g.setRBStatus("Error: getno");
/*     */     }
/* 154 */     g.setRBStatus("save GS");
/*     */ 
/* 156 */     addSQL("INSERT INTO " + SettingsManager.getSqlPrefix() + "gamestats VALUES(NULL," + arenaid + "," + players + ",'" + winner.getName() + "'," + time + ")");
/*     */ 
/* 158 */     g.setRBStatus("save PS");
/*     */ 
/* 160 */     for (PlayerStatsSession s : ((HashMap)this.arenas.get(Integer.valueOf(arenaid))).values()) {
/* 161 */       s.setGameID(gameno);
/* 162 */       addSQL(s.createQuery());
/*     */     }
/* 164 */     ((HashMap)this.arenas.get(Integer.valueOf(arenaid))).clear();
/*     */   }
/*     */ 
/*     */   private void addSQL(String query)
/*     */   {
/* 177 */     addSQL(this.dbman.createStatement(query));
/*     */   }
/*     */ 
/*     */   private void addSQL(PreparedStatement s) {
/* 181 */     this.queue.add(s);
/* 182 */     if (!this.dumper.isAlive()) {
/* 183 */       this.dumper = new DatabaseDumper();
/* 184 */       this.dumper.start();
/*     */     }
/*     */   }
/*     */ 
/*     */   class DatabaseDumper extends Thread {
/*     */     DatabaseDumper() {
/*     */     }
/*     */ 
/*     */     public void run() {
/* 192 */       while (StatsManager.this.queue.size() > 0) {
/* 193 */         PreparedStatement s = (PreparedStatement)StatsManager.this.queue.remove(0);
/*     */         try
/*     */         {
/* 197 */           s.execute(); } catch (Exception e) {
/* 198 */           StatsManager.this.dbman.connect();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.stats.StatsManager
 * JD-Core Version:    0.6.0
 */