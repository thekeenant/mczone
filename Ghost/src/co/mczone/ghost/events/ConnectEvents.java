package co.mczone.ghost.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.api.players.Gamer;
import co.mczone.ghost.Ghost;

public class ConnectEvents implements Listener {
	public ConnectEvents() {
		Ghost.getInstance().getServer().getPluginManager().registerEvents(this, Ghost.getInstance());
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		final Gamer g = Gamer.get(event.getPlayer());
		new BukkitRunnable() {

			@Override
			public void run() {
				Ghost.getMatches().get(0).join(g.getPlayer(), "red");
			}
			
		}.runTaskLater(Ghost.getInstance(), 100);
		for (PotionEffect p : g.getPlayer().getActivePotionEffects())
			g.getPlayer().removePotionEffect(p.getType());
	}
}
