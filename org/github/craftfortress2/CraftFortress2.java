package org.github.craftfortress2;
import java.util.logging.Logger;
import org.bukkit.plugin.java.*;
public class CraftFortress2 extends JavaPlugin {
Logger log = Logger.getLogger("Minecraft");
private CFCommandExecutor myExecutor;
	@Override
	public void onEnable() {
		myExecutor = new CFCommandExecutor(this);
		getCommand("cfstart").setExecutor(myExecutor);
		getCommand("cfend").setExecutor(myExecutor);
		getCommand("cfjoin").setExecutor(myExecutor);
		getCommand("cfhelp").setExecutor(myExecutor);
		getLogger().info("CraftFortress2 has been enabled.");
	}
	public void onDisable() {
		getLogger().info("CraftFortress2 has been disabled.");
	}
}