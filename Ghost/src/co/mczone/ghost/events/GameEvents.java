package co.mczone.ghost.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import co.mczone.api.players.Gamer;
import co.mczone.events.custom.PlayerModifyWorldEvent;
import co.mczone.ghost.Ghost;
import co.mczone.ghost.api.Match;
import co.mczone.ghost.api.MatchState;

public class GameEvents implements Listener {

	public GameEvents() {
		Ghost.getInstance().getServer().getPluginManager().registerEvents(this, Ghost.getInstance());
	}
	
	@EventHandler
	public void onPlayerModifyWorld(PlayerModifyWorldEvent event) {
		Gamer g = Gamer.get(event.getPlayer());
		boolean editMode = (boolean) g.getVariable("edit");
		if (editMode) {
			event.setCancelled(false);
			return;
		}
		event.setCancelled(true);
		if (g.getVariable("match") != null) {
			Match m = (Match) g.getVariable("match");
			if (m.getState() == MatchState.STARTED)
				event.setCancelled(false);
		}
	}
}
