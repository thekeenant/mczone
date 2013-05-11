package co.mczone.ghost.events;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import co.mczone.api.players.Gamer;
import co.mczone.api.server.Hive;
import co.mczone.ghost.Ghost;
import co.mczone.ghost.api.Arena;
import co.mczone.ghost.api.Kit;

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
		g.getPlayer().setHealth(20);
		g.getPlayer().setFoodLevel(20);
		
		List<Kit> kits = new ArrayList<Kit>();
		ResultSet r = Hive.getInstance().getDatabase().query("SELECT * FROM ghost_donations WHERE username='" + g.getName()  + "'");
		try {
			while (r.next()) {
				Kit kit = Kit.get(r.getString("kit"));
				if (kit == null)
					continue;
				
				kits.add(kit);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		g.setVariable("ghost-kits", kits);
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		Arena match = Arena.getArena(event.getPlayer());
		if (match == null)
			return;
		
		match.leave(event.getPlayer());
		match.getScoreboard().resetScores(Bukkit.getOfflinePlayer(event.getPlayer().getName()));
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		event.setLeaveMessage(null);
	}
}
