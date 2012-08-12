/*     */ package com.skitscape.survivalgames.logging;
/*     */ 
/*     */ import com.skitscape.survivalgames.Game;
/*     */ import com.skitscape.survivalgames.GameManager;
/*     */ import com.skitscape.survivalgames.SettingsManager;
/*     */ import com.skitscape.survivalgames.util.DatabaseManager;
/*     */ import com.skitscape.survivalgames.util.GameReset;
/*     */ import java.io.PrintStream;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.BlockState;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.Item;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.scheduler.BukkitScheduler;
/*     */ 
/*     */ public class QueueManager
/*     */ {
/*  34 */   private static QueueManager instance = new QueueManager();
/*  35 */   private DatabaseDumper dumper = new DatabaseDumper();
/*  36 */   private ArrayList<BlockData> queue = new ArrayList();
/*     */   private Plugin p;
/*     */   private Logger log;
/*  39 */   private DatabaseManager dbman = DatabaseManager.getInstance();
/*  40 */   private boolean sqlmode = true;
/*     */ 
/*     */   public static QueueManager getInstance()
/*     */   {
/*  46 */     return instance;
/*     */   }
/*     */ 
/*     */   public void setup(Plugin p, boolean sqlmode) throws SQLException
/*     */   {
/*  51 */     this.sqlmode = sqlmode;
/*  52 */     this.p = p;
/*     */ 
/*  54 */     if (sqlmode) {
/*  55 */       PreparedStatement s = this.dbman.createStatement(" CREATE TABLE " + SettingsManager.getSqlPrefix() + "blocks(gameid int, world varchar(255),previd int,prevdata int,newid int, newdata int, x int, y int, z int, time long)");
/*  56 */       DatabaseMetaData dbm = this.dbman.getMysqlConnection().getMetaData();
/*  57 */       ResultSet tables = dbm.getTables(null, null, SettingsManager.getSqlPrefix() + "blocks", null);
/*  58 */       if (!tables.next())
/*     */       {
/*  62 */         s.execute();
/*     */       }
/*     */ 
/*  66 */       this.log = p.getLogger();
/*  67 */       this.log.info("Connected to database.");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void rollback(GameReset r, int id)
/*     */   {
/*  74 */     GameManager.getInstance().getGame(id).setRBStatus("starting rollback");
/*     */ 
/*  76 */     new preRollback(r, id, null).start();
/*     */   }
/*     */ 
/*     */   public void add(BlockData data) {
/*  80 */     this.queue.add(data);
/*  81 */     if ((!this.dumper.isAlive()) && (this.sqlmode)) {
/*  82 */       this.dumper = new DatabaseDumper();
/*  83 */       this.dumper.start();
/*     */     }
/*     */   }
/*     */   class DatabaseDumper extends Thread {
/*     */     PreparedStatement s;
/*     */ 
/*     */     DatabaseDumper() {  }
/*     */ 
/*  90 */     public void run() { this.s = QueueManager.this.dbman.createStatement("INSERT INTO " + SettingsManager.getSqlPrefix() + "blocks VALUES (?,?,?,?,?,?,?,?,?,?)");
/*  91 */       while (QueueManager.this.queue.size() > 0) {
/*  92 */         BlockData b = (BlockData)QueueManager.this.queue.remove(0);
/*     */         try {
/*  94 */           this.s.setInt(1, GameManager.getInstance().getBlockGameId(new Location(Bukkit.getWorld(b.getWorld()), b.getX(), b.getY(), b.getZ())));
/*  95 */           this.s.setString(2, b.getWorld());
/*  96 */           this.s.setInt(3, b.getPrevid());
/*  97 */           this.s.setByte(4, b.getPrevdata());
/*  98 */           this.s.setInt(5, b.getNewid());
/*  99 */           this.s.setByte(6, b.getNewdata());
/* 100 */           this.s.setInt(7, b.getX());
/* 101 */           this.s.setInt(8, b.getY());
/* 102 */           this.s.setInt(9, b.getZ());
/* 103 */           this.s.setLong(10, new Date().getTime());
/* 104 */           this.s.execute();
/*     */         } catch (Exception e) {
/* 106 */           QueueManager.this.queue.add(b);
/*     */           try {
/* 108 */             QueueManager.this.dbman.getMysqlConnection().close();
/*     */           }
/*     */           catch (SQLException e1) {
/* 111 */             e1.printStackTrace();
/*     */           }
/* 113 */           QueueManager.this.dbman.connect();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   class Rollback extends Thread
/*     */   {
/*     */     int id;
/*     */     Statement s;
/*     */     ResultSet result;
/*     */     GameReset r;
/*     */     Game game;
/* 196 */     int taskID = 0;
/*     */ 
/* 209 */     int rbblocks = 0;
/* 210 */     int total = 0;
/* 211 */     int run = 0;
/*     */ 
/*     */     private Rollback(ResultSet rs, GameReset r, int game)
/*     */     {
/* 198 */       this.result = rs;
/* 199 */       this.r = r;
/* 200 */       this.id = game;
/* 201 */       boolean done = false;
/* 202 */       this.game = GameManager.getInstance().getGame(this.id);
/*     */     }
/*     */ 
/*     */     public void setTaskId(int t) {
/* 206 */       this.taskID = t;
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 214 */       if (QueueManager.this.sqlmode) {
/*     */         try {
/* 216 */           if (this.run == 0) {
/* 217 */             this.result.last();
/* 218 */             this.total = this.result.getRow();
/* 219 */             this.result.beforeFirst();
/* 220 */             this.run += 1;
/*     */           }
/*     */ 
/* 223 */           int i = 1;
/* 224 */           boolean done = false;
/*     */           try
/*     */           {
/* 227 */             while ((i != 100) && (!done)) {
/* 228 */               this.game.setRBStatus("rollback 1");
/*     */ 
/* 230 */               if (!this.result.next())
/*     */                 break;
/* 232 */               Location l = new Location(QueueManager.this.p.getServer().getWorld(this.result.getString(2)), this.result.getInt(7), this.result.getInt(8), this.result.getInt(9));
/* 233 */               Block b = l.getBlock();
/* 234 */               b.setTypeId(this.result.getInt(3));
/* 235 */               b.setData(this.result.getByte(4));
/* 236 */               b.getState().update();
/* 237 */               i++;
/* 238 */               this.rbblocks += 1;
/*     */             }
/* 240 */             this.game.setRBPercent((0.0D + this.rbblocks) / (0.0D + this.total) * 100.0D);
/* 241 */             if (i == 100) return; this.game.setRBStatus("finish rollback");
/*     */ 
/* 244 */             done = true;
/* 245 */             Bukkit.getScheduler().cancelTask(this.taskID);
/* 246 */             this.game.setRBStatus("rollback stoppped");
/*     */ 
/* 248 */             this.r.rollbackFinishedCallback();
/* 249 */             this.game.setRBStatus("clearing table");
/*     */ 
/* 251 */             Statement s1 = QueueManager.this.dbman.createStatement();
/* 252 */             s1.execute("DELETE FROM " + SettingsManager.getSqlPrefix() + "blocks WHERE gameid=" + this.id);
/* 253 */             System.out.println("Arena " + this.id + " reset. Rolled back " + this.rbblocks + " blocks");
/* 254 */             this.game.setRBStatus("");
/*     */ 
/* 256 */             this.result.close();
/* 257 */             this.result = null;
/*     */           } catch (Exception e) {
/* 259 */             e.printStackTrace();
/*     */           } } catch (Exception e) {
/* 260 */           e.printStackTrace();
/*     */         }
/*     */       } else {
/* 263 */         int a = QueueManager.this.queue.size() - 1;
/* 264 */         int rbblocks = 0;
/* 265 */         while (a >= 0) {
/* 266 */           BlockData result = (BlockData)QueueManager.this.queue.get(a);
/* 267 */           if (result.getGameId() == this.game.getID()) {
/* 268 */             QueueManager.this.queue.remove(a);
/* 269 */             Location l = new Location(Bukkit.getWorld(result.getWorld()), result.getX(), result.getY(), result.getZ());
/* 270 */             Block b = l.getBlock();
/* 271 */             b.setTypeId(result.getPrevid());
/* 272 */             b.setData(result.getPrevdata());
/* 273 */             b.getState().update();
/* 274 */             rbblocks++;
/*     */           }
/*     */ 
/* 277 */           a--;
/*     */         }
/* 279 */         System.out.println("Arena " + this.id + " reset. Rolled back " + rbblocks + " blocks. Save containts " + QueueManager.this.queue.size() + " blocks");
/*     */ 
/* 281 */         Bukkit.getScheduler().cancelTask(this.taskID);
/* 282 */         this.r.rollbackFinishedCallback();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   class preRollback extends Thread
/*     */   {
/*     */     int id;
/*     */     Statement s;
/*     */     ResultSet result;
/*     */     GameReset r;
/*     */     Game game;
/* 127 */     int taskId = 0;
/*     */ 
/* 129 */     private preRollback(GameReset r, int game) { this.r = r;
/* 130 */       this.id = game;
/* 131 */       boolean done = false;
/* 132 */       this.game = GameManager.getInstance().getGame(this.id); }
/*     */ 
/*     */     public void run() {
/* 135 */       System.out.println(LoggingManager.i);
/* 136 */       this.game.setRBStatus("save queue");
/*     */ 
/* 138 */       while ((QueueManager.this.queue.size() > 10) && (QueueManager.this.sqlmode)) {
/* 139 */         this.game.setRBPercent(QueueManager.this.queue.size());
/*     */         try { sleep(10L); } catch (Exception localException1) {
/*     */         }
/*     */       }
/*     */       try {
/* 144 */         this.game.setRBStatus("querying");
/* 145 */         if (QueueManager.this.sqlmode) {
/* 146 */           String query = "SELECT * FROM " + SettingsManager.getSqlPrefix() + "blocks WHERE gameid=" + this.id + " ORDER BY time DESC";
/* 147 */           Statement s = QueueManager.this.dbman.createStatement();
/* 148 */           this.game.setRBStatus("query result");
/*     */ 
/* 150 */           this.result = s.executeQuery(query);
/* 151 */           this.game.setRBStatus("clearing entities");
/*     */         }
/*     */         try
/*     */         {
/* 155 */           List list = SettingsManager.getGameWorld(this.id).getEntities();
/* 156 */           for (Iterator entities = list.iterator(); entities.hasNext(); )
/* 157 */             if (entities.hasNext()) {
/* 158 */               Entity entity = (Entity)entities.next();
/* 159 */               if ((entity instanceof Item)) {
/* 160 */                 Item iteme = (Item)entity;
/* 161 */                 Location loce = entity.getLocation();
/* 162 */                 if (GameManager.getInstance().getBlockGameId(loce) == this.id)
/* 163 */                   iteme.remove();
/*     */               }
/*     */             }
/*     */         }
/*     */         catch (Exception localException2) {
/*     */         }
/* 169 */         this.game.setRBStatus("starting rollback");
/* 170 */         QueueManager.Rollback rb = new QueueManager.Rollback(QueueManager.this, this.result, this.r, this.id, null);
/* 171 */         int taskid = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(QueueManager.this.p, rb, 0L, 1L);
/* 172 */         rb.setTaskId(taskid);
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 176 */         e.printStackTrace(); QueueManager.this.dbman.connect();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.logging.QueueManager
 * JD-Core Version:    0.6.0
 */