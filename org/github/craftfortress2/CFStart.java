package org.github.craftfortress2;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.Random;
public class CFStart extends CFCommandExecutor {
	public CFStart(CraftFortress2 cf2) {
		super(cf2);
	}
	public static void startGame(){
		//Player[] players = (Player[]) CFCommandExecutor.players.toArray();
		ArrayList<Player> players = CFCommandExecutor.players;
		//Server server = player.getServer();
		Server server = Bukkit.getServer();
		server.broadcastMessage("Craft Fortress 2 is starting!");
		World ctf2fort = server.getWorld("ctf_2fort");
		Location blue1 = new Location(ctf2fort, 78.52184, 70, 209.57359);
		Location blue2 = new Location(ctf2fort, 31.80711, 70, 201.99570);
		Location red1 = new Location(ctf2fort, 37.71015, 70, 256.51407);
		Location red2 = new Location(ctf2fort, 84.70609, 70, 264.01575);
		Random rand = new Random();
		int spawn = rand.nextInt(2);
		for (int i=0;i<players.size();i++)	{
			players.get(i).setGameMode(GameMode.ADVENTURE);
			if (CFCommandExecutor.getTeam(players.get(i)).equals("blue")) {
				if(spawn == 0){
					players.get(i).teleport(blue1);
				}else{
					players.get(i).teleport(blue2);
				}
			}else if(CFCommandExecutor.getTeam(players.get(i)).equals("red")){
				if(spawn == 0){
					players.get(i).teleport(red1);
				}else{
					players.get(i).teleport(red2);
				}
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