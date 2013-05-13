package co.mczone.pets.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import co.mczone.pets.api.PetInstance;
import co.mczone.util.Chat;

public class PlayerEvents implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		boolean spawned = false;
		for (PetInstance p : PetInstance.getList()) {
			if (p.getOwner().equalsIgnoreCase(event.getPlayer().getName())) {
				if (spawned) {
					p.setToSpawn(false);
				}
				else if (p.isShouldSpawn()) {
					p.spawn();
					spawned = true;
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		for (PetInstance p : PetInstance.getList()) {
			if (p.getOwner().equalsIgnoreCase(event.getPlayer().getName())) {
				p.despawn();
			}
		}
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		for (PetInstance p : PetInstance.getList()) {
			if (p.getOwner().equalsIgnoreCase(event.getPlayer().getName())) {
				if (p.getEntity() != null) {
					p.setToSpawn(true);
					p.getEntity().remove();
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Player p = event.getPlayer();
		Entity t = event.getRightClicked();
		PetInstance pet = PetInstance.get(t);
		if (pet == null)
			return;
		
		if (pet.getOwner().equalsIgnoreCase(p.getName())) {
			if (t.getPassenger() != null) {
				t.eject();
				t.setPassenger(null);
			}
			else {
				if (!p.isInsideVehicle())
					t.setPassenger(p);
			}
		}
		else {
			Chat.player(p, "&2[Pets] &cBuy pets at www.mczone.co/shop");
		}
	}
}
