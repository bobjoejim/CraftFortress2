package org.github.craftfortress2;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
public class CFClasses implements Listener{
	@EventHandler
	public void onHungerBarChange(FoodLevelChangeEvent event){
		event.setCancelled(true);
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
			if(CFCommandExecutor.getClass(player).equals("scout")){
				Scout.init(player);
				return;
			}else if(CFCommandExecutor.getClass(player).equals("soldier")){

			}else if(CFCommandExecutor.getClass(player).equals("heavy")){

			}else if(CFCommandExecutor.getClass(player).equals("demoman")){

			}else if(CFCommandExecutor.getClass(player).equals("medic")){

			}else if(CFCommandExecutor.getClass(player).equals("pyro")){

			}else if(CFCommandExecutor.getClass(player).equals("sniper")){

			}else if(CFCommandExecutor.getClass(player).equals("engineer")){

			}else if(CFCommandExecutor.getClass(player).equals("spy")){

			}
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
		return CFCommandExecutor.saveItemStack.get(index);
	}
}