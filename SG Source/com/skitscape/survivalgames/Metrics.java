/*     */ package com.skitscape.survivalgames;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.configuration.InvalidConfigurationException;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.configuration.file.YamlConfigurationOptions;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.PluginDescriptionFile;
/*     */ import org.bukkit.scheduler.BukkitScheduler;
/*     */ 
/*     */ public class Metrics
/*     */ {
/*     */   private static final int REVISION = 5;
/*     */   private static final String BASE_URL = "http://mcstats.org";
/*     */   private static final String REPORT_URL = "/report/%s";
/*     */   private static final String CUSTOM_DATA_SEPARATOR = "~~";
/*     */   private static final int PING_INTERVAL = 10;
/*     */   private final Plugin plugin;
/* 105 */   private final Set<Graph> graphs = Collections.synchronizedSet(new HashSet());
/*     */ 
/* 110 */   private final Graph defaultGraph = new Graph("Default", null);
/*     */   private final YamlConfiguration configuration;
/*     */   private final File configurationFile;
/*     */   private final String guid;
/* 130 */   private final Object optOutLock = new Object();
/*     */ 
/* 135 */   private volatile int taskId = -1;
/*     */ 
/*     */   public Metrics(Plugin plugin) throws IOException {
/* 138 */     if (plugin == null) {
/* 139 */       throw new IllegalArgumentException("Plugin cannot be null");
/*     */     }
/*     */ 
/* 142 */     this.plugin = plugin;
/*     */ 
/* 145 */     this.configurationFile = getConfigFile();
/* 146 */     this.configuration = YamlConfiguration.loadConfiguration(this.configurationFile);
/*     */ 
/* 149 */     this.configuration.addDefault("opt-out", Boolean.valueOf(false));
/* 150 */     this.configuration.addDefault("guid", UUID.randomUUID().toString());
/*     */ 
/* 153 */     if (this.configuration.get("guid", null) == null) {
/* 154 */       this.configuration.options().header("http://mcstats.org").copyDefaults(true);
/* 155 */       this.configuration.save(this.configurationFile);
/*     */     }
/*     */ 
/* 159 */     this.guid = this.configuration.getString("guid");
/*     */   }
/*     */ 
/*     */   public Graph createGraph(String name)
/*     */   {
/* 170 */     if (name == null) {
/* 171 */       throw new IllegalArgumentException("Graph name cannot be null");
/*     */     }
/*     */ 
/* 175 */     Graph graph = new Graph(name, null);
/*     */ 
/* 178 */     this.graphs.add(graph);
/*     */ 
/* 181 */     return graph;
/*     */   }
/*     */ 
/*     */   public void addGraph(Graph graph)
/*     */   {
/* 190 */     if (graph == null) {
/* 191 */       throw new IllegalArgumentException("Graph cannot be null");
/*     */     }
/*     */ 
/* 194 */     this.graphs.add(graph);
/*     */   }
/*     */ 
/*     */   public void addCustomData(Plotter plotter)
/*     */   {
/* 203 */     if (plotter == null) {
/* 204 */       throw new IllegalArgumentException("Plotter cannot be null");
/*     */     }
/*     */ 
/* 208 */     this.defaultGraph.addPlotter(plotter);
/*     */ 
/* 211 */     this.graphs.add(this.defaultGraph);
/*     */   }
/*     */ 
/*     */   public boolean start()
/*     */   {
/* 222 */     synchronized (this.optOutLock)
/*     */     {
/* 224 */       if (isOptOut()) {
/* 225 */         return false;
/*     */       }
/*     */ 
/* 229 */       if (this.taskId >= 0) {
/* 230 */         return true;
/*     */       }
/* 232 */       System.out.println("METRICS STARTING");
/*     */ 
/* 234 */       this.taskId = this.plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(this.plugin, new Runnable()
/*     */       {
/* 236 */         private boolean firstPost = true;
/*     */ 
/*     */         public void run()
/*     */         {
/*     */           try {
/* 241 */             synchronized (Metrics.this.optOutLock)
/*     */             {
/* 243 */               if ((Metrics.this.isOptOut()) && (Metrics.this.taskId > 0)) {
/* 244 */                 Metrics.this.plugin.getServer().getScheduler().cancelTask(Metrics.this.taskId);
/* 245 */                 Metrics.this.taskId = -1;
/*     */ 
/* 247 */                 for (Metrics.Graph graph : Metrics.this.graphs) {
/* 248 */                   graph.onOptOut();
/*     */                 }
/*     */ 
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/* 256 */             Metrics.this.postPlugin(!this.firstPost);
/*     */ 
/* 260 */             this.firstPost = false;
/*     */           } catch (IOException e) {
/* 262 */             Bukkit.getLogger().log(Level.INFO, "[Metrics] " + e.getMessage());
/*     */           }
/*     */         }
/*     */       }
/*     */       , 0L, 12000L);
/*     */ 
/* 267 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isOptOut()
/*     */   {
/* 277 */     synchronized (this.optOutLock)
/*     */     {
/*     */       try {
/* 280 */         this.configuration.load(getConfigFile());
/*     */       } catch (IOException ex) {
/* 282 */         Bukkit.getLogger().log(Level.INFO, "[Metrics] " + ex.getMessage());
/* 283 */         return true;
/*     */       } catch (InvalidConfigurationException ex) {
/* 285 */         Bukkit.getLogger().log(Level.INFO, "[Metrics] " + ex.getMessage());
/* 286 */         return true;
/*     */       }
/* 288 */       return this.configuration.getBoolean("opt-out", false);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void enable()
/*     */     throws IOException
/*     */   {
/* 299 */     synchronized (this.optOutLock)
/*     */     {
/* 301 */       if (isOptOut()) {
/* 302 */         this.configuration.set("opt-out", Boolean.valueOf(false));
/* 303 */         this.configuration.save(this.configurationFile);
/*     */       }
/*     */ 
/* 307 */       if (this.taskId < 0)
/* 308 */         start();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void disable()
/*     */     throws IOException
/*     */   {
/* 320 */     synchronized (this.optOutLock)
/*     */     {
/* 322 */       if (!isOptOut()) {
/* 323 */         this.configuration.set("opt-out", Boolean.valueOf(true));
/* 324 */         this.configuration.save(this.configurationFile);
/*     */       }
/*     */ 
/* 328 */       if (this.taskId > 0) {
/* 329 */         this.plugin.getServer().getScheduler().cancelTask(this.taskId);
/* 330 */         this.taskId = -1;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public File getConfigFile()
/*     */   {
/* 346 */     File pluginsFolder = this.plugin.getDataFolder().getParentFile();
/*     */ 
/* 349 */     return new File(new File(pluginsFolder, "PluginMetrics"), "config.yml");
/*     */   }
/*     */ 
/*     */   private void postPlugin(boolean isPing)
/*     */     throws IOException
/*     */   {
/* 357 */     PluginDescriptionFile description = this.plugin.getDescription();
/*     */ 
/* 360 */     StringBuilder data = new StringBuilder();
/* 361 */     data.append(encode("guid")).append('=').append(encode(this.guid));
/* 362 */     encodeDataPair(data, "version", description.getVersion());
/* 363 */     encodeDataPair(data, "server", Bukkit.getVersion());
/* 364 */     encodeDataPair(data, "players", Integer.toString(Bukkit.getServer().getOnlinePlayers().length));
/* 365 */     encodeDataPair(data, "revision", String.valueOf(5));
/*     */ 
/* 368 */     if (isPing) {
/* 369 */       encodeDataPair(data, "ping", "true");
/*     */     }
/*     */ 
/* 374 */     synchronized (this.graphs) {
/* 375 */       Iterator iter = this.graphs.iterator();
/*     */ 
/* 377 */       while (iter.hasNext()) {
/* 378 */         Graph graph = (Graph)iter.next();
/*     */ 
/* 380 */         for (Plotter plotter : graph.getPlotters())
/*     */         {
/* 384 */           String key = String.format("C%s%s%s%s", new Object[] { "~~", graph.getName(), "~~", plotter.getColumnName() });
/*     */ 
/* 388 */           String value = Integer.toString(plotter.getValue());
/*     */ 
/* 391 */           encodeDataPair(data, key, value);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 397 */     URL url = new URL("http://mcstats.org" + String.format("/report/%s", new Object[] { encode(this.plugin.getDescription().getName()) }));
/*     */     URLConnection connection;
/*     */     URLConnection connection;
/* 404 */     if (isMineshafterPresent())
/* 405 */       connection = url.openConnection(Proxy.NO_PROXY);
/*     */     else {
/* 407 */       connection = url.openConnection();
/*     */     }
/*     */ 
/* 410 */     connection.setDoOutput(true);
/*     */ 
/* 413 */     OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
/* 414 */     writer.write(data.toString());
/* 415 */     writer.flush();
/*     */ 
/* 418 */     BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
/* 419 */     String response = reader.readLine();
/*     */ 
/* 422 */     writer.close();
/* 423 */     reader.close();
/*     */ 
/* 425 */     if ((response == null) || (response.startsWith("ERR"))) {
/* 426 */       throw new IOException(response);
/*     */     }
/*     */ 
/* 429 */     if (response.contains("OK This is your first update this hour"))
/* 430 */       synchronized (this.graphs) {
/* 431 */         Iterator iter = this.graphs.iterator();
/*     */ 
/* 433 */         while (iter.hasNext()) {
/* 434 */           Graph graph = (Graph)iter.next();
/*     */ 
/* 436 */           for (Plotter plotter : graph.getPlotters())
/* 437 */             plotter.reset();
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   private boolean isMineshafterPresent()
/*     */   {
/*     */     try
/*     */     {
/* 452 */       Class.forName("mineshafter.MineServer");
/* 453 */       return true; } catch (Exception e) {
/*     */     }
/* 455 */     return false;
/*     */   }
/*     */ 
/*     */   private static void encodeDataPair(StringBuilder buffer, String key, String value)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 474 */     buffer.append('&').append(encode(key)).append('=').append(encode(value));
/*     */   }
/*     */ 
/*     */   private static String encode(String text)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 484 */     return URLEncoder.encode(text, "UTF-8");
/*     */   }
/*     */ 
/*     */   public static class Graph
/*     */   {
/*     */     private final String name;
/* 501 */     private final Set<Metrics.Plotter> plotters = new LinkedHashSet();
/*     */ 
/*     */     private Graph(String name) {
/* 504 */       this.name = name;
/*     */     }
/*     */ 
/*     */     public String getName()
/*     */     {
/* 513 */       return this.name;
/*     */     }
/*     */ 
/*     */     public void addPlotter(Metrics.Plotter plotter)
/*     */     {
/* 522 */       this.plotters.add(plotter);
/*     */     }
/*     */ 
/*     */     public void removePlotter(Metrics.Plotter plotter)
/*     */     {
/* 531 */       this.plotters.remove(plotter);
/*     */     }
/*     */ 
/*     */     public Set<Metrics.Plotter> getPlotters()
/*     */     {
/* 540 */       return Collections.unmodifiableSet(this.plotters);
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 545 */       return this.name.hashCode();
/*     */     }
/*     */ 
/*     */     public boolean equals(Object object)
/*     */     {
/* 550 */       if (!(object instanceof Graph)) {
/* 551 */         return false;
/*     */       }
/*     */ 
/* 554 */       Graph graph = (Graph)object;
/* 555 */       return graph.name.equals(this.name);
/*     */     }
/*     */ 
/*     */     protected void onOptOut()
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract class Plotter
/*     */   {
/*     */     private final String name;
/*     */ 
/*     */     public Plotter()
/*     */     {
/* 579 */       this("Default");
/*     */     }
/*     */ 
/*     */     public Plotter(String name)
/*     */     {
/* 588 */       this.name = name;
/*     */     }
/*     */ 
/*     */     public abstract int getValue();
/*     */ 
/*     */     public String getColumnName()
/*     */     {
/* 604 */       return this.name;
/*     */     }
/*     */ 
/*     */     public void reset()
/*     */     {
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 615 */       return getColumnName().hashCode();
/*     */     }
/*     */ 
/*     */     public boolean equals(Object object)
/*     */     {
/* 620 */       if (!(object instanceof Plotter)) {
/* 621 */         return false;
/*     */       }
/*     */ 
/* 624 */       Plotter plotter = (Plotter)object;
/* 625 */       return (plotter.name.equals(this.name)) && (plotter.getValue() == getValue());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.Metrics
 * JD-Core Version:    0.6.0
 */