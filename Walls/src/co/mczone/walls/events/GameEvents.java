package co.mczone.walls.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import co.mczone.api.players.Gamer;
import co.mczone.api.server.Hive;
import co.mczone.events.custom.PlayerDamageEvent;
import co.mczone.events.custom.PlayerKilledEvent;
import co.mczone.util.Chat;
import co.mczone.walls.Walls;
import co.mczone.walls.api.Arena;
import co.mczone.walls.api.ArenaState;

public class GameEvents implements Listener {

	public GameEvents() {
		Walls.getInstance().getServer().getPluginManager().registerEvents(this, Walls.getInstance());
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Gamer g = Gamer.get((Player) event.getEntity());
			if (g.isInvisible())
				event.setCancelled(true);
		}
	}
	
	@EventHandler 
	public void onPlayerKilled(PlayerKilledEvent event) {
		Gamer t = Gamer.get(event.getTarget());
		event.getTarget().setHealth(20);
		Arena m = (Arena) t.getVariable("arena");
		if (m == null)
			return;

		t.setAllowFlight(true);
		t.setFlying(true);
		t.setInvisible(true);
		m.updateScoreboard();
		
		String broadcast = "&4[Walls] &6" + event.getDeathMessage();
		if (event.isPlayerKill()) {
			Gamer p = Gamer.get(event.getPlayer());
			p.giveCredits(2);
			p.kill(event.getTarget(), 0);
			
			for (Player player : m.getPlayers()) {
				if (player.getName().equals(p.getName()))
					Chat.player(player, broadcast + " &8[&72 credits&8]");
				else
					Chat.player(player, broadcast);
			}
		}
		else {
			for (Player player : m.getPlayers()) {
				Chat.player(player, broadcast);
			}
			Hive.getInstance().kill(event.getTarget(), "natural", 0);
			if (event.getDeathMessage().contains("fell out")) {
				t.teleport(m.getSpawn());
			}
		}
		event.setDeathMessage(null);
	}
	
	@EventHandler
	public void onPlayerDamage(PlayerDamageEvent event) {
		Player p = event.getPlayer();
		Player t = event.getTarget();
		event.setCancelled(true);
		
		if (Gamer.get(t).isInvisible())
			return;
		
		if (Gamer.get(t).getVariable("arena") != null) {
			
			Arena targetMatch = (Arena) Gamer.get(t).getVariable("arena");
			
			if (targetMatch.getState() != ArenaState.STARTED)
				return;

			// Is natural damage
			if (!event.isDamageByEntity()) {
				event.setCancelled(false);
				return;
			}
			
			// Is player damage
			else {
				Arena playerMatch = (Arena) Gamer.get(p).getVariable("arena");
				
				if (playerMatch == targetMatch) {
					// Allow different team damage
					if (playerMatch.getTeam(p) != playerMatch.getTeam(t))
						event.setCancelled(false);
				}
				
				if (Gamer.get(p).isInvisible())
					event.setCancelled(true);
			}
		}
	}
}
