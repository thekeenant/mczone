package co.mczone.skywars.events;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import co.mczone.api.players.Gamer;
import co.mczone.api.server.Hive;
import co.mczone.skywars.SkyWars;
import co.mczone.skywars.api.Arena;
import co.mczone.skywars.api.Kit;

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
		
		List<Kit> kits = new ArrayList<Kit>();
		ResultSet r = Hive.getInstance().getDatabase().query("SELECT * FROM skywars_donations WHERE username='" + g.getName()  + "'");
		try {
			while (r.next()) {
				if (r.getInt("free") == 1) {
					// Just a vote
					
					Kit kit = Kit.get(r.getString("kit"));
					if (kit == null)
						continue;
					g.setVariable("skywars-vote", kit);
				}
				else {
					Kit kit = Kit.get(r.getString("kit"));
					if (kit == null)
						continue;
					
					kits.add(kit);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		g.setVariable("skywars-kits", kits);
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
