package co.mczone.ghost.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import co.mczone.api.players.Gamer;
import co.mczone.ghost.Ghost;
import co.mczone.ghost.api.Arena;

public class GeneralEvents implements Listener {
	
	public GeneralEvents() {
		Ghost.getInstance().getServer().getPluginManager().registerEvents(this, Ghost.getInstance());
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		event.setFoodLevel(20);
		((Player) event.getEntity()).setHealth(20);
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Gamer g = Gamer.get(event.getPlayer());
		if (g.isInvisible()) {
			Arena a = (Arena) g.getVariable("arena");
			event.getRecipients().clear();
			event.getRecipients().addAll(a.getSpectators());
		}
	}
}
