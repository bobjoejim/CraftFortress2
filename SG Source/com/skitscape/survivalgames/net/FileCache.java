/*    */ package com.skitscape.survivalgames.net;
/*    */ 
/*    */ import com.skitscape.survivalgames.SurvivalGames;
/*    */ import java.io.File;
/*    */ import java.io.PrintStream;
/*    */ import java.util.HashMap;
/*    */ import java.util.Scanner;
/*    */ 
/*    */ public class FileCache
/*    */ {
/* 12 */   private static FileCache instance = new FileCache();
/* 13 */   private static HashMap<String, String> html = new HashMap();
/*    */ 
/*    */   public static FileCache getInstance()
/*    */   {
/* 19 */     return instance;
/*    */   }
/*    */ 
/*    */   public static String getHTML(String pagename, boolean template) {
/* 23 */     if (html.get(pagename) == null) {
/* 24 */       loadPage(pagename, template);
/*    */     }
/*    */ 
/* 27 */     return (String)html.get(pagename);
/*    */   }
/*    */ 
/*    */   public static void loadPage(String pagename, boolean template)
/*    */   {
/* 33 */     Scanner scan = null;
/* 34 */     File f = new File(SurvivalGames.getPluginDataFolder() + (template ? "/www/template.html" : new StringBuilder("/www/pages/").append(pagename).append(".html").toString()));
/*    */     try
/*    */     {
/* 37 */       scan = new Scanner(f); } catch (Exception e) {
/* 38 */       System.out.println("Survival Games webstats - Could not load page: " + pagename + "    " + f.getAbsolutePath());
/* 39 */     }String data = "";
/*    */ 
/* 41 */     if (scan == null) {
/* 42 */       html.put(pagename, "404 - Not found");
/* 43 */       return;
/*    */     }
/*    */     do
/* 46 */       data = data + scan.nextLine();
/* 45 */     while (scan.hasNext());
/*    */ 
/* 48 */     html.put(pagename, data);
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.net.FileCache
 * JD-Core Version:    0.6.0
 */