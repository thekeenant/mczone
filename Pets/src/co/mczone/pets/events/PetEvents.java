package co.mczone.pets.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import co.mczone.pets.api.PetInstance;
import co.mczone.util.Chat;

public class PetEvents implements Listener {
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		PetInstance pet = null;
		for (PetInstance p : PetInstance.getList()) {
			if (p.getEntity() == null)
				continue;
			if (p.getEntity().getUniqueId().equals(event.getEntity().getUniqueId())) {
				pet = p;
				break;
			}
		}
		
		if (pet != null && event instanceof EntityDamageByEntityEvent == false)
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player == false)
			return;
		
		PetInstance pet = PetInstance.get(event.getEntity());
		
		if (pet == null)
			return;
		
		event.setCancelled(true);
		
		if (pet.getOwner().equalsIgnoreCase(((Player) event.getDamager()).getName())) {
			Chat.player((Player) event.getDamager(), "&2[Pets] &aYou have removed your pet. Spawn it with &b/pet spawn");
			pet.despawn();
			pet.setToSpawn(false);
		}
		else {
			Chat.player((Player) event.getDamager(), "&2[Pets] &cBuy pets at www.mczone.co/shop");
		}
	}
	
	@EventHandler
	public void onEntityTarget(EntityTargetEvent event) {
		if (event.getReason() == TargetReason.TARGET_DIED)
			return;
		
		PetInstance pet = null;
		for (PetInstance p : PetInstance.getList()) {
			if (p.getEntity() == null)
				continue;
			if (p.getEntity().getUniqueId().equals(event.getEntity().getUniqueId())) {
				pet = p;
				break;
			}
		}
		
		if (pet != null)
			event.setCancelled(true);
	}
}
