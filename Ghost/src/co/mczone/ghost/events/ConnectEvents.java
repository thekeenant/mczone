package co.mczone.ghost.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import co.mczone.api.players.Gamer;
import co.mczone.ghost.Ghost;

public class ConnectEvents implements Listener {
	public ConnectEvents() {
		Ghost.getInstance().getServer().getPluginManager().registerEvents(this, Ghost.getInstance());
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Gamer g = Gamer.get(event.getPlayer());

		g.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10000, 1));
		Ghost.getMatches().get(0).join(event.getPlayer(), "red");
		
		g.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10000, 1));
	}
}
