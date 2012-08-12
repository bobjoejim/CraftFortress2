package org.github.craftfortress2;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
public class CFCommandExecutor implements CommandExecutor {
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
			if (cmd.getName().equalsIgnoreCase("cfstart")) {
				CFStart.startGame();
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("cfend")) {
				CFEnd.endGame();
				return true;
			} //in future: add /cfjoin [TEAM], /cfspectate, etc.
			//we also need some way to set arena boundaries, use worldedit?
			if(cmd.getName().equalsIgnoreCase("cfhelp")){
				sender.sendMessage("CRAFT FORTRESS 2 HELP");
				sender.sendMessage("/cfstart - start a game of CraftFortress");
				sender.sendMessage("/cfend - end a game of CraftFortress");
				return true;
			}
			if(args.length>1){
				sender.sendMessage("Too many arguments!");
				return false;
			}
			return false;
		}
}