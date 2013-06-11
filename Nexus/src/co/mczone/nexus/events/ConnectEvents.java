package co.mczone.nexus.events;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

import co.mczone.api.players.Gamer;
import co.mczone.nexus.Nexus;
import co.mczone.nexus.api.Team;
import co.mczone.nexus.enums.GameState;

public class ConnectEvents implements Listener {
	
	public ConnectEvents() {
		Bukkit.getPluginManager().registerEvents(this, Nexus.getPlugin());
	}
	
	@Getter List<String> dbPlayers = new ArrayList<String>();
	
	@EventHandler
	public void serverListPing(ServerListPingEvent event) {
		event.setMotd(Nexus.getRotary().getCurrentMap().getTitle());
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		
		Gamer g = Gamer.get(event.getPlayer());
		g.teleport(Nexus.getRotary().getCurrentMap().getSpawnLocation());
		g.clearInventory();
		g.setHealth(20);
		g.setFoodLevel(20);
		g.setSaturation(99F);
		g.removePotionEffects();
		
		g.run("give-book");
		
		g.setVariable("spectator", true);
		
		Nexus.getRotary().getSidebar().add(event.getPlayer());
		
		g.setAllowFlight(true);
		g.setFlying(true);
		
		if (Nexus.getRotary().getState() == GameState.PLAYING)
			Nexus.getDatabase().update(
					"INSERT INTO nexus_players (game_id,username,team,date) VALUES " +
					"(" + Nexus.getRotary().getGameID() + ", '" + g.getName() + "','spec',now())");
		
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		
		Gamer g = Gamer.get(event.getPlayer());
		Team team = Nexus.getRotary().getCurrentMap().getTeam(g);
		
		if (team != null)
			team.remove(g);
		
	}
	
}
