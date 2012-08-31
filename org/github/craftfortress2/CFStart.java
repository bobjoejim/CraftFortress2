package org.github.craftfortress2;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import java.util.Random;
public class CFStart extends CFCommandExecutor {
	public CFStart(CraftFortress2 cf2) {
		super(cf2);
	}
	public static void startGame(){
		Server server = Bukkit.getServer();
		server.broadcastMessage("Craft Fortress 2 is starting!");
		World ctf2fort = server.getWorld("ctf_2fort");
		Location blu1 = new Location(ctf2fort, 78.52184, 70, 209.57359);
		Location blu2 = new Location(ctf2fort, 31.80711, 70, 201.99570);
		Location red1 = new Location(ctf2fort, 37.71015, 70, 256.51407);
		Location red2 = new Location(ctf2fort, 84.70609, 70, 264.01575);
		Random rand = new Random();
		int spawn = rand.nextInt(2);
		for (int i=0;i<players.size();i++){
			players.get(i).setGameMode(GameMode.ADVENTURE);
			players.get(i).setHealth(20);
			if (players.get(i).toString().equals(changeTeam(CFCommandExecutor.team))){
				players.get(i).teleport(new Location(ctf2fort, 0, 0, 0));
			}
			if (getTeam(players.get(i)).equals("blu")) {
				if(spawn == 0){
					players.get(i).teleport(blu1);
				}else{
					players.get(i).teleport(blu2);
				}
			}else if(getTeam(players.get(i)).equals("red")){
				if(spawn == 0){
					players.get(i).teleport(red1);
				}else{
					players.get(i).teleport(red2);
				}
			}
			initClasses(players.get(i));
		}
	}
}