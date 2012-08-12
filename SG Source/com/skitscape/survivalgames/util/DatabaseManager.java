/*     */ package com.skitscape.survivalgames.util;
/*     */ 
/*     */ import com.skitscape.survivalgames.SettingsManager;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.logging.Logger;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ 
/*     */ public class DatabaseManager
/*     */ {
/*     */   private Connection conn;
/*     */   private Plugin p;
/*     */   private Logger log;
/*  21 */   private static DatabaseManager instance = new DatabaseManager();
/*     */ 
/*     */   public static DatabaseManager getInstance()
/*     */   {
/*  28 */     return instance;
/*     */   }
/*     */ 
/*     */   public void setup(Plugin p)
/*     */   {
/*  33 */     this.log = p.getLogger();
/*  34 */     connect();
/*     */   }
/*     */ 
/*     */   public Connection getMysqlConnection()
/*     */   {
/*  40 */     return this.conn;
/*     */   }
/*     */ 
/*     */   public boolean connectToDB(String host, int port, String db, String user, String pass)
/*     */   {
/*     */     try
/*     */     {
/*  47 */       Class.forName("com.mysql.jdbc.Driver");
/*  48 */       this.conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, user, pass);
/*  49 */       return true;
/*     */     }
/*     */     catch (ClassNotFoundException e)
/*     */     {
/*  53 */       this.log.warning("Couldn't start MySQL Driver. Stopping...\n" + e.getMessage());
/*     */ 
/*  55 */       return false;
/*     */     }
/*     */     catch (SQLException e)
/*     */     {
/*  59 */       this.log.warning("Couldn't connect to MySQL database. Stopping...\n" + e.getMessage());
/*  60 */     }return false;
/*     */   }
/*     */ 
/*     */   public PreparedStatement createStatement(String query)
/*     */   {
/*  66 */     boolean created = false;
/*  67 */     int times = 0;
/*  68 */     PreparedStatement p = null;
/*     */     try
/*     */     {
/*  71 */       times++;
/*  72 */       p = this.conn.prepareStatement(query, 1);
/*  73 */       created = true;
/*     */     }
/*     */     catch (SQLException e)
/*     */     {
/*  77 */       if (times == 5)
/*     */       {
/*  79 */         return null;
/*     */       }
/*  81 */       connect();
/*     */     }
/*     */ 
/*  86 */     return p;
/*     */   }
/*     */ 
/*     */   public Statement createStatement()
/*     */   {
/*     */     try
/*     */     {
/*  95 */       return this.conn.createStatement();
/*     */     }
/*     */     catch (SQLException e) {
/*     */     }
/*  99 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean connect()
/*     */   {
/* 107 */     FileConfiguration c = SettingsManager.getInstance().getConfig();
/* 108 */     String host = c.getString("sql.host", "localhost");
/* 109 */     int port = c.getInt("sql.port", 3306);
/* 110 */     String db = c.getString("sql.database", "SurvivalGames");
/* 111 */     String user = c.getString("sql.user", "root");
/* 112 */     String pass = c.getString("sql.pass", "");
/* 113 */     return connectToDB(host, port, db, user, pass);
/*     */   }
/*     */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.util.DatabaseManager
 * JD-Core Version:    0.6.0
 */