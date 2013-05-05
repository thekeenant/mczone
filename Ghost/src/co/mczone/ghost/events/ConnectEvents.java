package co.mczone.ghost.events;

import org.bukkit.event.player.PlayerJoinEvent;

import co.mczone.api.players.Gamer;

public class ConnectEvents {
	public void onPlayerJoin(PlayerJoinEvent event) {
		Gamer g = Gamer.get(event.getPlayer());
		g.setInvisible(true);
	}
}
