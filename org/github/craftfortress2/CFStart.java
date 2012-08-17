package org.github.craftfortress2;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
public class CFStart extends CFCommandExecutor {
	public CFStart(CraftFortress2 cf2) {
		super(cf2);
	}
	static Player[] players = (Player[]) CFCommandExecutor.players.toArray();
		public static void startGame(){
			Player player = null;
			Server server = player.getServer();
			server.broadcastMessage("Craft Fortress 2 is starting!");
			Server svr = player.getServer();
			World dustbowl = svr.getWorld("AT_Dustbowl");
			Location blue = new Location(dustbowl, 1, 90, 1);
			Location red = new Location(dustbowl, 2, 90, 2);
			for (int i=0;i<players.length;i++)	{
				if (CFCommandExecutor.getTeam(players[i]) == "blue") {
					players[i].teleport(blue);
				}else{
					players[i].teleport(red);
			}
	}
}