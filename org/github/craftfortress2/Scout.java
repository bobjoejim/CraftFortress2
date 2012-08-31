package org.github.craftfortress2;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.*;
import org.bukkit.inventory.*;
public class Scout extends CFClasses {

	public static void init(Player player){
		PlayerInventory inv = player.getInventory();
		inv.clear();
		player.setFoodLevel(17);
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999999, 1));
		inv.setHelmet(new ItemStack(Material.IRON_HELMET, 1));
		inv.setBoots(new ItemStack(Material.IRON_BOOTS, 1));
		inv.setItem(2, new ItemStack(Material.STICK, 1));
	}
	@EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		int jumpcount = 0;
        if (!event.isSneaking()) {
            return;
        }
        Player player = event.getPlayer();
        if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR && CFCommandExecutor.isPlaying(player)
        		&& CFCommandExecutor.getClass(player).equals("scout") && jumpcount == 0){
        	if (player.toString().equals("Bobjoejim") || player.toString().equals("ultimatepig")){
        		player.setVelocity(player.getVelocity().setY(999999));
        		jumpcount++;
        	}else{
        		player.setVelocity(player.getVelocity().setY(1));
        		jumpcount++;
        	}
        }else{
        	jumpcount = 0;
        	event.setCancelled(true);
        }
	}
}