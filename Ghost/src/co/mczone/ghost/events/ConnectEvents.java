package co.mczone.ghost.events;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import co.mczone.api.players.Gamer;
import co.mczone.ghost.Ghost;
import co.mczone.ghost.api.Match;

public class ConnectEvents implements Listener {
	
	public ConnectEvents() {
		Ghost.getInstance().getServer().getPluginManager().registerEvents(this, Ghost.getInstance());
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		final Gamer g = Gamer.get(event.getPlayer());
		g.teleport(Ghost.getLobby().getSpawn());
		g.removePotionEffects();
		g.getPlayer().setGameMode(GameMode.SURVIVAL);
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		Match match = Match.getMatch(event.getPlayer());
		if (match == null)
			return;
		
		match.leave(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		event.setLeaveMessage(null);
	}
}
