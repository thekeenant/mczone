package co.mczone.ghost.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import co.mczone.ghost.Ghost;
import co.mczone.ghost.api.Match;

public class PlayerEvents implements Listener {
	
	public PlayerEvents() {
		Ghost.getInstance().getServer().getPluginManager().registerEvents(this, Ghost.getInstance());
	}
	
	// Because the setAllowFriendlyFire() doesn't work.
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		Entity entity = event.getEntity();
		Entity damagerEntity = event.getDamager();
		
		Player attacked = null;
		Player damager = null;
		
		if (entity instanceof Player) {
			
			attacked = (Player) entity;
			if (damagerEntity instanceof Player) {
				damager = (Player) damagerEntity;
			} else if (damagerEntity instanceof Projectile) {
				Projectile projectile = (Projectile) damagerEntity;
				Entity shooter = projectile.getShooter();
				
				if (shooter instanceof Player) {
					damager = (Player) damager;
				}
			}
			
		}
		
		if (attacked != null && damager != null) {
			Match match = Ghost.getMatches().get(0);
			Scoreboard board = match.getScoreboard();
			Team attackedTeam = board.getPlayerTeam(attacked);
			Team damagerTeam = board.getPlayerTeam(damager);
			
			if (attackedTeam == damagerTeam) {
				event.setCancelled(true);
			} else if (attackedTeam.getName().equalsIgnoreCase("spec") || damagerTeam.getName().equalsIgnoreCase("spec")) {
				event.setCancelled(true);
			} else if (!match.isRunning()) {
				event.setCancelled(true);
			}
		}
	}
	
}
