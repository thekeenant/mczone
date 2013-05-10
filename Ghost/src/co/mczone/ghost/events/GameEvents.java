package co.mczone.ghost.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import co.mczone.api.players.Gamer;
import co.mczone.events.custom.PlayerDamageEvent;
import co.mczone.events.custom.PlayerKilledEvent;
import co.mczone.events.custom.PlayerModifyWorldEvent;
import co.mczone.ghost.Ghost;
import co.mczone.ghost.api.Arena;
import co.mczone.ghost.api.ArenaState;
import co.mczone.util.Chat;

public class GameEvents implements Listener {

	public GameEvents() {
		Ghost.getInstance().getServer().getPluginManager().registerEvents(this, Ghost.getInstance());
	}
	
	@EventHandler
	public void onPlayerModifyWorld(PlayerModifyWorldEvent event) {
		Gamer g = Gamer.get(event.getPlayer());
		event.setCancelled(true);
		if (g.getVariable("edit") == null)
			return;
		
		boolean editMode = (boolean) g.getVariable("edit");
		if (editMode) 
			event.setCancelled(false);
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

		t.setInvisible(true);
		m.updateScoreboard();
		m.join(event.getPlayer(), "red");
		m.join(event.getPlayer(), "blue");
		
		String broadcast = "&4[Ghost] &6" + event.getDeathMessage();
		if (event.isPlayerKill()) {
			Gamer p = Gamer.get(event.getPlayer());
			p.giveCredits(2);
			for (Player player : m.getPlayers()) {
				if (player.getName().equals(p.getName()))
					Chat.player(player, broadcast + " &8[&72 credit&8]");
				else
					Chat.player(player, broadcast);
			}
		}
		else {
			for (Player player : m.getPlayers()) {
				Chat.player(player, broadcast);
			}
		}
		event.setDeathMessage(null);
	}
	
	@EventHandler
	public void onPlayerDamage(PlayerDamageEvent event) {
		Player p = event.getPlayer();
		Player t = event.getTarget();
		event.setCancelled(true);
		
		if (Gamer.get(t).getVariable("arena") != null) {
			Arena targetMatch = (Arena) Gamer.get(t).getVariable("arena");
			if (targetMatch.getState() != ArenaState.STARTED)
				return;
			
			// Natural damage?
			if (!event.isDamageByEntity()) {
				event.setCancelled(false);
				return;
			}
			
			// Player damage?
			if (event.isDamageByEntity()) {
				Arena playerMatch = (Arena) Gamer.get(p).getVariable("arena");
				
				if (playerMatch == targetMatch) {
					// Allow different team damage
					if (playerMatch.getTeam(p) != playerMatch.getTeam(t))
						event.setCancelled(false);
				}
			}
		}
	}
}
