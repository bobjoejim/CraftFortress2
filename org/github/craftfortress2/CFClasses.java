package org.github.craftfortress2;
import org.bukkit.Material;
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
	@EventHandler
    	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) { // This probably won't work, but it's worth a shot.
        if (!event.isSneaking()) {
            return;
        }
        Player player = event.getPlayer();
        if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR && CFCommandExecutor.isPlaying(player) && CFCommandExecutor.getClass(player).equals("scout")){
        	player.setVelocity(player.getVelocity().setY(1));
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