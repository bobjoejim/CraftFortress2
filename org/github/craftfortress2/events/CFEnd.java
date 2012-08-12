package org.github.craftfortress2;
public class CFEnd {
	public static void endGame(){
		Player player = null;
		Server server = player.getServer();
		server.broadcastMessage("A game of Craft Fortress 2 has just ended!");	
	}
}