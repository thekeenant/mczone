package co.mczone.nexus.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import co.mczone.api.players.Gamer;
import co.mczone.nexus.Nexus;
import co.mczone.nexus.api.Team;

public class ConnectEvents implements Listener {

	public ConnectEvents() {
		Bukkit.getPluginManager().registerEvents(this, Nexus.getPlugin());
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
		
		g.setVariable("spectator", true);
		g.setVariable("killstreak", 0);
		
		Nexus.getRotary().getSidebar().add(event.getPlayer());
		
		g.setAllowFlight(true);
		g.setFlying(true);
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
