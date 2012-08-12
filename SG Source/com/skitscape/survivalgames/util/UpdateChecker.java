/*    */ package com.skitscape.survivalgames.util;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.OutputStreamWriter;
/*    */ import java.io.PrintStream;
/*    */ import java.net.URL;
/*    */ import java.net.URLConnection;
/*    */ import java.net.URLEncoder;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ import org.bukkit.plugin.PluginDescriptionFile;
/*    */ 
/*    */ public class UpdateChecker
/*    */ {
/*    */   public void check(Player player, Plugin p)
/*    */   {
/* 30 */     String response = "";
/* 31 */     String data = "";
/*    */ 
/* 33 */     String v = p.getDescription().getVersion();
/* 34 */     String ip = Bukkit.getIp();
/* 35 */     int port = Bukkit.getPort();
/*    */     try
/*    */     {
/* 44 */       data = URLEncoder.encode("version", "UTF-8") + "=" + URLEncoder.encode(v, "UTF-8");
/* 45 */       data = data + "&" + URLEncoder.encode("ip", "UTF-8") + "=" + URLEncoder.encode(ip, "UTF-8");
/* 46 */       data = data + "&" + URLEncoder.encode("port", "UTF-8") + "=" + URLEncoder.encode(new StringBuilder().append(port).toString(), "UTF-8");
/*    */ 
/* 49 */       URL url = new URL("http://curlybrace.org/sg/updatecheck.php");
/* 50 */       URLConnection conn = url.openConnection();
/* 51 */       conn.setDoOutput(true);
/* 52 */       OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
/* 53 */       wr.write(data);
/* 54 */       wr.flush();
/*    */ 
/* 57 */       BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
/*    */       String line;
/* 59 */       while ((line = rd.readLine()) != null)
/*    */       {
/*    */         String line;
/* 60 */         response = line;
/*    */       }
/*    */ 
/* 64 */       String[] in = response.split("~");
/*    */ 
/* 67 */       Boolean b = Boolean.valueOf(Boolean.parseBoolean(in[0]));
/*    */ 
/* 70 */       if (b.booleanValue()) {
/* 71 */         player.sendMessage(ChatColor.DARK_BLUE + "--------------------------------------");
/* 72 */         player.sendMessage(ChatColor.DARK_RED + "[SurvivalGames] Updated Avaliable!");
/* 73 */         player.sendMessage(ChatColor.DARK_AQUA + "Your version: " + ChatColor.GOLD + v + ChatColor.DARK_AQUA + " Latest: " + ChatColor.GOLD + in[1]);
/* 74 */         player.sendMessage(ChatColor.DARK_AQUA + in[2]);
/* 75 */         player.sendMessage(ChatColor.AQUA + ChatColor.UNDERLINE + in[3]);
/* 76 */         player.sendMessage(ChatColor.DARK_BLUE + "--------------------------------------");
/*    */       } else {
/* 78 */         System.out.println("[info]No updates found!");
/*    */       }
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 83 */       e.printStackTrace();
/* 84 */       System.out.println("[SurvivalGames] could not check for updates.");
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.util.UpdateChecker
 * JD-Core Version:    0.6.0
 */