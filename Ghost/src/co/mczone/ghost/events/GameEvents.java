package co.mczone.ghost.events;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import co.mczone.api.players.Gamer;
import co.mczone.events.custom.PlayerDamageEvent;
import co.mczone.events.custom.PlayerKilledEvent;
import co.mczone.events.custom.PlayerModifyWorldEvent;
import co.mczone.ghost.Ghost;
import co.mczone.ghost.api.Match;
import co.mczone.ghost.api.MatchState;
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
	public void onPlayerKilled(PlayerKilledEvent event) {
		Gamer t = Gamer.get(event.getTarget());
		
		Match m = (Match) t.getVariable("match");
		if (m == null)
			return;
		
		m.getDead().add(t.getName());
		m.updateScoreboard();
		
		String broadcast = "&4[Ghost] &6" + event.getDeathMessage();
		if (event.isPlayerKill()) {
			Gamer p = Gamer.get(event.getPlayer());
			p.giveCredits(3);
			for (Player player : m.getPlayers()) {
				if (player.getName().equals(p.getName()))
					Chat.player(player, broadcast + " &8[&71 credit&8]");
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
		if (!event.isEntityDamage())
			return;
		
		if (event.getPlayer() == null)
			return;
		
		Player p = event.getPlayer();
		Player t = event.getTarget();
		
		if (Gamer.get(p).getVariable("match") == Gamer.get(t).getVariable("match"))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerTeamKill(EntityDamageByEntityEvent event) {

		if (event.getEntity() instanceof Player == false)
			return;
		
		Player t = (Player) event.getEntity();
		Player p = null;
		
		
		if (event.getDamager() instanceof Player) {
			p = (Player) event.getDamager();
		} 
		else if (event.getDamager() instanceof Projectile) {
			Projectile projectile = (Projectile) event.getDamager();
			
			if (projectile.getShooter() instanceof Player)
				p = (Player) projectile.getShooter();
		}
		
		if (p != null) {
			Match match = Match.getMatch(t);
			Scoreboard board = match.getScoreboard();
			Team attackedTeam = board.getPlayerTeam(p);
			Team damagerTeam = board.getPlayerTeam(t);
			
			if (attackedTeam == damagerTeam)
				event.setCancelled(true);
			else if (attackedTeam.getName().equalsIgnoreCase("spec") || damagerTeam.getName().equalsIgnoreCase("spec"))
				event.setCancelled(true);
			else if (match.getState() != MatchState.STARTED)
				event.setCancelled(true);
		}
	}
}
