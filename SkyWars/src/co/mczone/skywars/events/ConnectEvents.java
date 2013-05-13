package co.mczone.skywars.events;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import co.mczone.api.players.Gamer;
import co.mczone.skywars.SkyWars;
import co.mczone.skywars.api.Arena;

public class ConnectEvents implements Listener {
	
	public ConnectEvents() {
		SkyWars.getInstance().getServer().getPluginManager().registerEvents(this, SkyWars.getInstance());
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		final Gamer g = Gamer.get(event.getPlayer());
		g.teleport(SkyWars.getLobby().getSpawn());
		g.removePotionEffects();
		if (g.getVariable("edit") == null)
			g.getPlayer().setGameMode(GameMode.SURVIVAL);
		g.getPlayer().setHealth(20);
		g.getPlayer().setFoodLevel(20);
		g.run("load-kits");
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		Arena match = Arena.getArena(event.getPlayer());
		if (match == null)
			return;
		
		match.leave(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		event.setLeaveMessage(null);
	}
}
