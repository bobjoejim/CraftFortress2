package org.github.craftfortress2;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class CFClasses {

	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
				player.setExhaustion(0);
				event.setFoodLevel(19);

				event.setCancelled(true);
		}
	}
	 @EventHandler // THIS IS THE CODE OF NOREGEN, NOT OURS
	  public void onHeal(EntityRegainHealthEvent event) {
		 if (event.getEntityType().equals(EntityType.PLAYER)) {
			 Player p = (Player) event.getEntity();
			 if (event.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.SATIATED)) {
				 event.setCancelled(true);
			 }
	 		} else if (event.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.SATIATED)) {
	 			event.setCancelled(true);
	        }
	 }
}
