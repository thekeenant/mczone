package co.mczone.ghost.events;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import co.mczone.ghost.Ghost;
import co.mczone.ghost.api.Match;
import co.mczone.ghost.api.MatchState;

public class PlayerEvents implements Listener {
	
	public PlayerEvents() {
		Ghost.getInstance().getServer().getPluginManager().registerEvents(this, Ghost.getInstance());
	}
	
	// Because the setAllowFriendlyFire() doesn't work.
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
