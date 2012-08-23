package org.github.craftfortress2;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import java.util.ArrayList;
public class CFStart extends CFCommandExecutor {
	public CFStart(CraftFortress2 cf2) {
		super(cf2);
	}
	public static void startGame(){
		//Player[] players = (Player[]) CFCommandExecutor.players.toArray();
		ArrayList<Player> players = CFCommandExecutor.players;
		Player player = null;
		//Server server = player.getServer();
		Server server = Bukkit.getServer();
		server.broadcastMessage("Craft Fortress 2 is starting!");
		World dustbowl = server.getWorld("AT_Dustbowl");
		Location blue = new Location(dustbowl, 1, 90, 1);
		Location red = new Location(dustbowl, 2, 90, 2);
		for (int i=0;i<players.size();i++)	{
			players.get(i).setGameMode(GameMode.ADVENTURE);
			if (CFCommandExecutor.getTeam(players.get(i)).equals("blue")) {
				players.get(i).teleport(blue);
			}else{
				players.get(i).teleport(red);
			}
			if(CFCommandExecutor.getClass(players.get(i)).equals("scout")){
				Scout.init();
			}
			if(CFCommandExecutor.getClass(players.get(i)).equals("soldier")){

			}
			if(CFCommandExecutor.getClass(players.get(i)).equals("demoman")){

			}
			if(CFCommandExecutor.getClass(players.get(i)).equals("heavy")){

			}
			if(CFCommandExecutor.getClass(players.get(i)).equals("medic")){

			}
			if(CFCommandExecutor.getClass(players.get(i)).equals("engineer")){

			}
			if(CFCommandExecutor.getClass(players.get(i)).equals("spy")){

			}
			if(CFCommandExecutor.getClass(players.get(i)).equals("pyro")){

			}
			if(CFCommandExecutor.getClass(players.get(i)).equals("sniper")){

			}
		}
	}
}