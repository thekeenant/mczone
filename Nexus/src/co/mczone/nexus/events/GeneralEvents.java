package co.mczone.nexus.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import co.mczone.api.players.Gamer;
import co.mczone.events.custom.PlayerDamageEvent;
import co.mczone.nexus.Nexus;

public class GeneralEvents implements Listener {

	public GeneralEvents() {
		Bukkit.getPluginManager().registerEvents(this, Nexus.getPlugin());
	}
	
	@EventHandler
	public void onPlayerDamage(PlayerDamageEvent event) {
		if (event.getDamager() instanceof Player == false)
			return;
		
		Gamer g = Gamer.get((Player) event.getDamager());

		if (g.getVariable("spectator") != null)
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Gamer g = Gamer.get(event.getPlayer());

		if (g.getVariable("spectator") != null)
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onDropItem(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}
	
}
