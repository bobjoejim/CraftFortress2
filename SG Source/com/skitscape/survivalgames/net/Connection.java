/*    */ package com.skitscape.survivalgames.net;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.OutputStream;
/*    */ import java.io.PrintStream;
/*    */ import java.net.Socket;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class Connection extends Thread
/*    */ {
/*    */   BufferedReader in;
/*    */   DataOutputStream out;
/*    */   Socket skt;
/* 25 */   HashMap<String, String> html = new HashMap();
/*    */ 
/*    */   public Connection(Socket skt) {
/*    */     try {
/* 29 */       this.in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
/* 30 */       this.out = new DataOutputStream(skt.getOutputStream());
/* 31 */       this.skt = skt; } catch (Exception localException) {
/*    */     }
/*    */   }
/*    */ 
/*    */   public void run() {
/*    */     try {
/* 37 */       write("ADFSADFDSAF", this.out, this.in.readLine());
/* 38 */       this.skt.close(); } catch (Exception e) {
/* 39 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   public void getHTML(String pageName)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void parseHTML(String page)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void write(String str, OutputStream out, String header)
/*    */   {
/* 57 */     String s = "HTTP/1.0 ";
/* 58 */     s = s + "200 OK";
/* 59 */     s = s + "\r\n";
/* 60 */     s = s + "Connection: close\r\n";
/* 61 */     s = s + "Server: SurvivalGames v0\r\n";
/* 62 */     s = s + "Content-Type: text/html\r\n";
/* 63 */     s = s + "\r\n";
/*    */ 
/* 65 */     String template = FileCache.getHTML("template", true);
/*    */ 
/* 69 */     String[] args = header.split(" ")[1].trim().split("/");
/* 70 */     System.out.print(args[1]);
/*    */ 
/* 72 */     String page = template.replace("{#page}", FileCache.getHTML(args[1], false));
/*    */ 
/* 75 */     page = parse(page);
/*    */ 
/* 77 */     str = s + page;
/*    */     try
/*    */     {
/* 80 */       out.write(str.getBytes());
/*    */     }
/*    */     catch (IOException e) {
/* 83 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   public String parse(String page)
/*    */   {
/* 89 */     return page;
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.net.Connection
 * JD-Core Version:    0.6.0
 */