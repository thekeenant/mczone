package co.mczone.nexus.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import co.mczone.api.players.Gamer;
import co.mczone.events.custom.PlayerDamageEvent;
import co.mczone.nexus.Nexus;

public class GeneralEvents implements Listener {

	public GeneralEvents() {
		Bukkit.getPluginManager().registerEvents(this, Nexus.getPlugin());
	}
	
	@EventHandler
	public void onPlayerDamage(PlayerDamageEvent event) {
		Gamer g = Gamer.get(event.getTarget());

		if (g.getVariable("spectator") != null)
			event.setCancelled(true);
		
	}
	
}
