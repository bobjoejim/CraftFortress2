package org.github.craftfortress2;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.*;
import org.bukkit.inventory.*;
public class Scout extends CFClasses {
	Player player = null;
	PlayerInventory inv = player.getInventory();
	public void init(){
		player.setFoodLevel(17);
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 1));
		inv.setHelmet(new ItemStack(Material.IRON_HELMET, 1));
		inv.setBoots(new ItemStack(Material.IRON_BOOTS, 1));
		inv.setItem(36, new ItemStack(Material.STICK, 1));
	}

}
