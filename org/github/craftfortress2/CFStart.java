package org.github.craftfortress2;
import org.bukkit.Server;
import org.bukkit.entity.Player;
public class CFStart {
		public static void startGame(){
			Player player = null;
			Server server = player.getServer();
			server.broadcastMessage("A new CraftFortress2 game is starting!");
		}
}