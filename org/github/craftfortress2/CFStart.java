package org.github.craftfortress2;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
public class CFStart extends CFCommandExecutor {
	static Player[] players = (Player[]) CFCommandExecutor.players.toArray();
	public CFStart(CraftFortress2 cf2) {
		super(cf2);
	}
		public static void startGame(){
			Player player = null;
			Server server = player.getServer();
			server.broadcastMessage("Craft Fortress 2 is starting!");
			Server svr = player.getServer();
			World dustbowl = svr.getWorld("Dustbowl");
			Location blue = new Location(dustbowl, 1, 1, 1);
			Location red = new Location(dustbowl, 2, 2, 2);
			if(CFCommandExecutor.getTeam(players[0]) == "blue"){
				players[0].teleport(blue);
			}else{
				players[0].teleport(red);
			}
		}
}