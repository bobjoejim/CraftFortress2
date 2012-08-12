/*     */ package com.skitscape.survivalgames.util;
/*     */ 
/*     */ import com.skitscape.survivalgames.Arena;
/*     */ import com.skitscape.survivalgames.Game;
/*     */ import com.skitscape.survivalgames.GameManager;
/*     */ import com.skitscape.survivalgames.SettingsManager;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.OperatingSystemMXBean;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.craftbukkit.CraftChunk;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ public class ArenaDuplicator
/*     */ {
/*     */   background background;
/*     */ 
/*     */   public void startDupe(Vector v1, Vector v2)
/*     */   {
/*  24 */     int factor = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
/*  25 */     factor = 4;
/*  26 */     int xspan = v2.getBlockX() - v1.getBlockX();
/*  27 */     int maxx = GameManager.getInstance().getGame(1).getArena().getMax().getBlockX();
/*  28 */     for (Game g : GameManager.getInstance().getGames()) {
/*  29 */       Location a1 = g.getArena().getMin();
/*  30 */       Location a2 = g.getArena().getMax();
/*     */ 
/*  32 */       if (a1.getBlockX() > maxx) {
/*  33 */         maxx = a1.getBlockX();
/*     */       }
/*  35 */       if (a2.getBlockX() > maxx) {
/*  36 */         maxx = a2.getBlockX();
/*     */       }
/*     */     }
/*     */ 
/*  40 */     int divf = xspan / factor;
/*  41 */     this.background = new background(Math.abs(v2.getBlockX() - v1.getBlockX()) * Math.abs(v2.getBlockY() - v1.getBlockY()) * Math.abs(v1.getBlockZ() - v2.getBlockZ()));
/*  42 */     this.background.start();
/*  43 */     for (int a = 1; a <= factor; a++) {
/*  44 */       System.out.println(xspan);
/*  45 */       int sp1 = divf * a + v1.getBlockX();
/*  46 */       int sp2 = divf * (a + 1) + v1.getBlockX();
/*  47 */       int y1 = v1.getBlockY();
/*  48 */       int y2 = v2.getBlockY();
/*  49 */       int z1 = v1.getBlockZ();
/*  50 */       int z2 = v2.getBlockZ();
/*     */ 
/*  53 */       Vector s1 = new Vector(sp1 < sp2 ? sp1 : sp2, y1 < y2 ? y1 : y2, z1 < z2 ? z1 : z2);
/*  54 */       Vector s2 = new Vector(sp1 > sp2 ? sp1 : sp2, y1 > y2 ? y1 : y2, z1 > z2 ? z1 : z2);
/*  55 */       System.out.println(s1);
/*  56 */       System.out.println(s2);
/*  57 */       new DupeThread(s1, s2, maxx - v1.getBlockX(), 0, a).start(); }  } 
/*  66 */   class DupeThread extends Thread { World w = SettingsManager.getGameWorld(1);
/*     */     Vector min;
/*     */     Vector max;
/*     */     int xoff;
/*     */     int zoff;
/*     */     int id;
/*     */ 
/*  73 */     public DupeThread(Vector min, Vector max, int xoff, int yoff, int id) { this.min = min;
/*  74 */       this.max = max;
/*     */ 
/*  76 */       this.xoff = xoff;
/*  77 */       this.zoff = yoff;
/*  78 */       this.id = id;
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/*  85 */       Bukkit.getServer().broadcastMessage("Starting " + this.id);
/*  86 */       for (int x = this.min.getBlockX(); x < this.max.getBlockX(); x++) {
/*  87 */         for (int y = this.min.getBlockY(); y < this.max.getBlockY(); y++) {
/*  88 */           for (int z = this.min.getBlockZ(); z < this.max.getBlockZ(); z++) {
/*  89 */             Location l1 = new Location(this.w, x, y, z);
/*  90 */             Location l2 = new Location(this.w, x + this.xoff, y, z + this.zoff);
/*     */             try
/*     */             {
/*  96 */               if (l1.getBlock().getTypeId() == l2.getBlock().getTypeId())
/*     */                 continue;
/*  98 */               org.bukkit.Chunk c = l2.getChunk();
/*  99 */               net.minecraft.server.Chunk chunk = ((CraftChunk)c).getHandle();
/*     */ 
/* 101 */               chunk.a(l2.getBlockX() & 0xF, l2.getBlockY(), l2.getBlockZ() & 0xF, l1.getBlock().getTypeId(), l1.getBlock().getData());
/*     */ 
/* 105 */               ArenaDuplicator.this.background.inc();
/*     */             } catch (Exception e) {
/* 107 */               e.printStackTrace();
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 114 */       Bukkit.getServer().broadcastMessage("Ending " + this.id);
/*     */     }
/*     */   }
/*     */ 
/*     */   class background extends Thread
/*     */   {
/* 120 */     int x = 0;
/* 121 */     int fin = 0;
/* 122 */     int prev = 0;
/*     */ 
/* 124 */     public background(int x) { this.x = x; }
/*     */ 
/*     */     public synchronized void inc()
/*     */     {
/* 128 */       this.fin += 1;
/*     */     }
/*     */ 
/*     */     public void run() {
/*     */       while (true) {
/* 133 */         System.out.println(this.fin + "/" + this.x + " " + (this.fin - this.prev) / 2 + " " + (this.fin + 0.0D) / (this.x + 0.0D) * 100.0D);
/* 134 */         this.prev = this.fin;
/*     */         try { sleep(2000L);
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.util.ArenaDuplicator
 * JD-Core Version:    0.6.0
 */