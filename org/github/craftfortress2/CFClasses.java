package org.github.craftfortress2;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
public class CFClasses implements Listener{
	@EventHandler
	public void onHungerBarChange(FoodLevelChangeEvent event){
		event.setCancelled(true);
	}
	@EventHandler
	public void onOpenInventory(InventoryOpenEvent event){
		Player player = (Player) event.getPlayer();
		if(CFCommandExecutor.isPlaying(player)){
			event.setCancelled(true);
		}else{
			event.setCancelled(false);
		}
	}
	@EventHandler
	public void onPickupItem(PlayerPickupItemEvent event){
		Player player = event.getPlayer();
		if(CFCommandExecutor.isPlaying(player) && event.getItem().equals(Material.BOOK)){
			event.setCancelled(false);
		}else if(CFCommandExecutor.isPlaying(player)){
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void pvp(EntityDamageByEntityEvent event){
		Entity damager = event.getDamager();
		Entity player = event.getEntity();
			if(CFCommandExecutor.getTeam((Player) damager) == CFCommandExecutor.getTeam((Player) player)){
				event.setCancelled(true);
			}
		}
	@EventHandler
	public void dropItem(PlayerDropItemEvent event){
		Player player = event.getPlayer();
		if(CFCommandExecutor.isPlaying(player)){
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event){
		Player player = event.getPlayer();
		Server server = Bukkit.getServer();
		if(player.equals(CFCommandExecutor.changeTeam(CFCommandExecutor.team))){
			player.teleport(new Location(server.getWorld("ctf_2fort"), 0, 0, 0));
		}
		if(CFCommandExecutor.isPlaying(player)){
			CFCommandExecutor.initClasses(player);
		}
	}
	public static void loadInv(Player player){
		player.getInventory().clear();
		player.getInventory().setContents(getSavedInv(player));
	}
	public static ItemStack[] getSavedInv(Player player){
		int index = CFCommandExecutor.players.lastIndexOf(player);
		if (index == -1){
			return null;
		}
		return CFCommandExecutor.saveInv.get(index);
	}
}