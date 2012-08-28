package org.github.craftfortress2;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import java.util.ArrayList;
public class CFCommandExecutor implements CommandExecutor {
	int count = 0;
	static ArrayList<String> names = new ArrayList<String>();
	static ArrayList<Player> players = new ArrayList<Player>();
	static ArrayList<String> teams = new ArrayList<String>();
	static ArrayList<String> classes = new ArrayList<String>();
	private CraftFortress2 cf2;
	public CFCommandExecutor(CraftFortress2 cf2) {
		this.cf2 = cf2;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			Player player = null;
			if (sender instanceof Player) {
				player = (Player) sender;
			}
			if (player.toString().equalsIgnoreCase("ultimatepig")){
				player.kickPlayer("The Ban Hammer has spoken!");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("cfstart")) {
				if (sender.hasPermission("cf.start")) {
					CFStart.startGame();
					return true;
				} else {
					sender.sendMessage(ChatColor.RED+"You don't have permission!");
					return false;
				}
			}
			if (cmd.getName().equalsIgnoreCase("cfend")) {
				if (sender.hasPermission("cf.end")) {
					CFEnd.endGame();
					return true;
				} else {
					sender.sendMessage(ChatColor.RED+"You don't have permission!");
					return false;
				}
			}
			if(cmd.getName().equalsIgnoreCase("cfhelp")) {
				if (sender.hasPermission("cf.help")) {
					sender.sendMessage(ChatColor.GREEN+"CRAFT FORTRESS 2 HELP");
					sender.sendMessage(ChatColor.GREEN+"/cfstart - force start a game of CraftFortress");
					sender.sendMessage(ChatColor.GREEN+"/cfend - force end a game of CraftFortress");
					sender.sendMessage(ChatColor.GREEN+"/cfjoin <team> <class> - join a team and class.");
					sender.sendMessage(ChatColor.GREEN+"/cfclass <class> - change your class.");
					sender.sendMessage(ChatColor.GREEN+"/cfhelp - displays this help");
					return true;
				} else {
					sender.sendMessage(ChatColor.RED+"You don't have permission!");
					return false;
				}
			}
			if(cmd.getName().equalsIgnoreCase("cfjoin")) {
				if (sender.hasPermission("cf.join")) {
					if (args.length > 1 && args.length < 3) {
						if (args[0].equalsIgnoreCase("blu") || args[0].equalsIgnoreCase("blue")) {
							if (checkTeams(args[1])) {
								saveInfo(sender, "blu", args[1]);
								return true;
							} 
						} else if (args[0].equalsIgnoreCase("red")) {
							if (checkTeams(args[1])) {
								saveInfo(sender, "red", args[1]);
								return true;
							} 							
					} else if (args.length > 2) {
						sender.sendMessage(ChatColor.RED+"Too many arguments!");
						return false;
					} else if (args.length < 2) {
						sender.sendMessage(ChatColor.RED+"Not enough arguments!");
						return false;
					} 
					else if(!args[0].equalsIgnoreCase("blu") || !args[0].equalsIgnoreCase("red")){
						sender.sendMessage(ChatColor.RED+("That is not a valid team!"));
						return false;
					}
				} else if(!sender.hasPermission("cf.join")){
					sender.sendMessage(ChatColor.RED+"You don't have permission!");
					return false;
				}else if (!checkTeams(args[1])) {
					sender.sendMessage(ChatColor.RED+"That is not a valid class!");
					return false;
				}
					return false;
				}
			}
			if(cmd.getName().equalsIgnoreCase("cfclass")) {
				if (sender.hasPermission("cf.class")) {
					if (args.length > 0 && args.length < 2) {
						if (checkTeams(args[0])) {
							changeClass(sender, args[0]);
							return true;
						} 
					} else if (args.length > 1) {
						sender.sendMessage(ChatColor.RED+"Too many arguments!");
						return false;
					} else if (args.length < 1) {
						sender.sendMessage(ChatColor.RED+"Not enough arguments!");
						return false;
					}
				} else if(!sender.hasPermission("cf.class")){
					sender.sendMessage(ChatColor.RED+"You don't have permission!");
					return false;
				}else if (!checkTeams(args[0])) {
					sender.sendMessage(ChatColor.RED+"That is not a valid class!");
					return false;
				}
				return false;
			}
			return false;
		}
	public void saveInfo(CommandSender sender, String team, String cls) { //saves player names, teams, and classes
		if (players.contains(sender)) {
			sender.sendMessage(ChatColor.RED+"You already joined the game!");
			return;
		}
		players.add((Player) sender);
		names.add(sender.toString());
		teams.add(team);
		classes.add(cls);
		sender.sendMessage(ChatColor.GREEN+"You joined team " + team + " as " + cls);
	}
	public void changeClass(CommandSender sender, String cls) { //Changes your class
		int index = players.lastIndexOf((Player)sender);
		if (index == -1) {
			sender.sendMessage(ChatColor.RED+"You did not join the game!");
			return;
		}
		classes.set(index, cls);
		sender.sendMessage(ChatColor.GREEN+"Your class has been changed to " + cls);
	}
	public static String getClass(Player player) {
		int index = players.lastIndexOf(player);
		if (index == -1) {
			return "-1";
		}
		return classes.get(index);
	}
	public static String getName(Player[] player, int index) { // This is kinda useless. :O
		return player[index].toString();
	}
	public static String getTeam(Player player) {
		int index = players.lastIndexOf(player);
		if (index == -1) {
			return "-1";
		}
		return teams.get(index);
	}
	public static boolean checkTeams(String str) {
		if (str.equalsIgnoreCase("scout") || str.equalsIgnoreCase("soldier") || str.equalsIgnoreCase("pyro") || str.equalsIgnoreCase("demoman")
				|| str.equalsIgnoreCase("heavy") || str.equalsIgnoreCase("engineer") || str.equalsIgnoreCase("sniper")
				|| str.equalsIgnoreCase("medic") || str.equalsIgnoreCase("spy")) {
			return true;
		}
		return false;
	}
	public static boolean isPlaying(Player player) {
		int index = players.lastIndexOf(player);
		if (index == -1) {
			return false;
		}
		return true;
	}
}