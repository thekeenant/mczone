package co.mczone.ghost.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import co.mczone.api.players.Gamer;
import co.mczone.ghost.Ghost;
import co.mczone.ghost.api.Match;

public class ConnectEvents implements Listener {
	
	public ConnectEvents() {
		Ghost.getInstance().getServer().getPluginManager().registerEvents(this, Ghost.getInstance());
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		final Gamer g = Gamer.get(event.getPlayer());
		Match.getList().get(0).join(g.getPlayer(), "red");
		
		
		g.removePotionEffects();
		
	}
}
