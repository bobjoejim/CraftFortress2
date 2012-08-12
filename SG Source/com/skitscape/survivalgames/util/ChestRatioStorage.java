/*     */ package com.skitscape.survivalgames.util;
/*     */ 
/*     */ import com.skitscape.survivalgames.SurvivalGames;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ public class ChestRatioStorage
/*     */ {
/*  29 */   HashMap<Integer, ArrayList<ItemStore>> lvlstore = new HashMap();
/*  30 */   public static ChestRatioStorage instance = new ChestRatioStorage();
/*  31 */   int ratio = 2;
/*     */ 
/*     */   public void setup()
/*     */   {
/*  40 */     File f = new File(SurvivalGames.getPluginDataFolder() + "/chest.yml");
/*  41 */     if (!f.exists()) try {
/*  42 */         f.createNewFile();
/*  43 */         FileWriter out = new FileWriter(f);
/*  44 */         InputStream is = getClass().getResourceAsStream("chest.yml");
/*  45 */         InputStreamReader isr = new InputStreamReader(is);
/*  46 */         BufferedReader br = new BufferedReader(isr);
/*     */         String line;
/*  48 */         while ((line = br.readLine()) != null)
/*     */         {
/*     */           String line;
/*  49 */           out.write(line + "\n");
/*     */         }
/*     */ 
/*  54 */         is.close();
/*  55 */         isr.close();
/*  56 */         br.close();
/*  57 */         out.close();
/*     */       } catch (IOException e) {
/*  59 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */ 
/*  63 */     FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
/*     */ 
/*  65 */     for (int a = 1; a < 5; a++) {
/*  66 */       ArrayList lvl = new ArrayList();
/*  67 */       List list = conf.getStringList("chest.lvl" + a);
/*     */ 
/*  69 */       for (int b = 0; b < list.size(); b++) {
/*  70 */         String[] arg = ((String)list.get(b)).split(",");
/*     */ 
/*  72 */         ItemStore i = new ItemStore(Integer.parseInt(arg[0]), Integer.parseInt(arg[1]));
/*     */ 
/*  75 */         lvl.add(i);
/*     */       }
/*     */ 
/*  79 */       this.lvlstore.put(Integer.valueOf(a), lvl);
/*     */     }
/*     */ 
/*  83 */     this.ratio = (conf.getInt("chest.ratio") + 1);
/*     */   }
/*     */ 
/*     */   public static ChestRatioStorage getInstance()
/*     */   {
/*  88 */     return instance;
/*     */   }
/*     */ 
/*     */   public ArrayList<ItemStack> getItems()
/*     */   {
/* 138 */     Random r = new Random();
/* 139 */     ArrayList items = new ArrayList();
/* 140 */     for (int a = 0; a < r.nextInt(7) + 5; a++) {
/* 141 */       if (r.nextBoolean()) {
/* 142 */         int i = 1;
/* 143 */         while ((i < 6) && (r.nextInt(this.ratio) == 1)) {
/* 144 */           i++;
/*     */         }
/*     */ 
/* 147 */         ArrayList lvl = (ArrayList)this.lvlstore.get(Integer.valueOf(i));
/* 148 */         ItemStore item = (ItemStore)lvl.get(r.nextInt(lvl.size()));
/* 149 */         i = 0;
/* 150 */         ItemStack stack = new ItemStack(item.getId(), r.nextInt(item.getMax()) + 1);
/* 151 */         if (r.nextBoolean()) item.isEnchant();
/*     */ 
/* 155 */         items.add(stack);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 161 */     return items;
/*     */   }
/*     */ 
/*     */   class ItemStore
/*     */   {
/*     */     int id;
/*     */     int max;
/* 106 */     boolean enchant = false;
/*     */ 
/*     */     public int getId()
/*     */     {
/*  94 */       return this.id;
/*     */     }
/*     */ 
/*     */     public int getMax() {
/*  98 */       return this.max;
/*     */     }
/*     */ 
/*     */     public boolean isEnchant() {
/* 102 */       return this.enchant;
/*     */     }
/*     */ 
/*     */     public ItemStore(int id, int max, boolean enchant)
/*     */     {
/* 109 */       setup(id, max, enchant);
/*     */     }
/*     */ 
/*     */     public ItemStore(int id, int max) {
/* 113 */       setup(id, max, false);
/*     */     }
/*     */ 
/*     */     public ItemStore(int id) {
/* 117 */       setup(id, 1, false);
/*     */     }
/*     */ 
/*     */     public ItemStore(int id, boolean enchant) {
/* 121 */       setup(id, 1, enchant);
/*     */     }
/*     */ 
/*     */     public void setup(int id, int max, boolean enchant) {
/* 125 */       this.id = id;
/* 126 */       this.max = max;
/* 127 */       this.enchant = enchant;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 132 */       return "ID: " + this.id + " AMOUNT: " + this.max;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.util.ChestRatioStorage
 * JD-Core Version:    0.6.0
 */