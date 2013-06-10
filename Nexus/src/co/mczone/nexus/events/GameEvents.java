package co.mczone.nexus.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.api.players.Gamer;
import co.mczone.api.server.Hive;
import co.mczone.events.custom.PlayerDamageEvent;
import co.mczone.events.custom.PlayerKilledEvent;
import co.mczone.nexus.Nexus;
import co.mczone.nexus.api.Map;
import co.mczone.nexus.api.Team;
import co.mczone.util.Chat;

public class GameEvents implements Listener {

	public GameEvents() {
		Bukkit.getPluginManager().registerEvents(this, Nexus.getPlugin());
	}
	
	@EventHandler
	public void onPlayerKilled(PlayerKilledEvent event) {
		Gamer g = Gamer.get(event.getPlayer());
		Gamer t = Gamer.get(event.getTarget());
		
		Team targetTeam = Nexus.getRotary().getCurrentMap().getTeam(t);
		
		if (t.getVariable("spectator") != null)
			return;

		if (event.isPlayerKill()) {
			Team killerTeam = Nexus.getRotary().getCurrentMap().getTeam(g);
			g.kill(t, Nexus.getRotary().getGameID());
			
			// Set killstreaks
			int streak = Nexus.getMatchStats().getKills(g);
			if (streak > 1)
				Chat.player(g, "&eYou have reached a killstreak of &6" + streak + " &ekills");
			
			// Add points
			killerTeam.addPoints(1);
			Nexus.getMatchStats().addKill(g);
			
			String message = event.getDeathMessage();
			message.replace(t.getName(), targetTeam.getColor().getChatColor() + t.getName() + "&7");
			message.replace(g.getName(), targetTeam.getColor().getChatColor() + g.getName());
			event.setDeathMessage(message);
		}
		else {
			Hive.getInstance().kill(t.getPlayer(), "natural", Nexus.getRotary().getGameID());
			
			String message = event.getDeathMessage();
			message.replace(t.getName(), targetTeam.getColor().getChatColor() + t.getName() + "&7");
			event.setDeathMessage(message);
		}
		
		// There goes his KD...
		Nexus.getMatchStats().addDeath(t);
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		final Gamer g = Gamer.get(event.getPlayer());
		
		new BukkitRunnable() {

			@Override
			public void run() {
				Team t = Nexus.getRotary().getCurrentMap().getTeam(g);
				
				if (t == null) {
					g.teleport(Nexus.getRotary().getCurrentMap().getSpawnLocation());
					return;
				}
				
				g.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20 * 5, 3));
				
				g.teleport(t.getSpawnLocation());
				g.run("give-kit");
			}
			
		}.runTaskLater(Nexus.getPlugin(), 1);
	}
	
	@EventHandler
	public void onPlayerDamage(PlayerDamageEvent event) {
		if (event.isDamageByEntity() == false || event.getDamager() instanceof Player == false)
			return;
		
		Gamer g = Gamer.get((Player) event.getDamager());
		Gamer t = Gamer.get(event.getTarget());
		
		Map map = Nexus.getRotary().getCurrentMap();
		if (map.getTeam(g) == map.getTeam(t))
			event.setCancelled(true);
	}
	
}
