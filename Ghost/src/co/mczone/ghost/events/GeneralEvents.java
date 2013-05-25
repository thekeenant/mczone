package co.mczone.ghost.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import co.mczone.api.players.Gamer;
import co.mczone.ghost.Ghost;
import co.mczone.ghost.api.Arena;
import co.mczone.ghost.api.ArenaState;

public class GeneralEvents implements Listener {
	
	public GeneralEvents() {
		Ghost.getInstance().getServer().getPluginManager().registerEvents(this, Ghost.getInstance());
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		Gamer g = Gamer.get(event.getEntity().getName());
		if (g.getVariable("arena") != null) {
			Arena a = (Arena) g.getVariable("arena");
			if (a.getState() == ArenaState.STARTED)
				return;
		}
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
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {		
		Gamer g = Gamer.get(event.getPlayer());
		event.setCancelled(true);
		
		if (g.getVariable("arena") != null) {
			Arena a = (Arena) g.getVariable("arena");
			if (a.getState() == ArenaState.STARTED)
				event.setCancelled(false);
		}
		
		if (g.getVariable("edit") != null) {
			if ((boolean) g.getVariable("edit"))
				event.setCancelled(false);
		}
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Gamer g = Gamer.get(event.getPlayer());
		event.setCancelled(true);
		if (g.getVariable("edit") != null) {
			if ((boolean) g.getVariable("edit"))
				event.setCancelled(false);
		}
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerPickupItemEvent event) {
		Gamer g = Gamer.get(event.getPlayer());
		event.setCancelled(true);
		if (g.getVariable("edit") != null) {
			if ((boolean) g.getVariable("edit"))
				event.setCancelled(false);
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Gamer g = Gamer.get(event.getPlayer());
		event.setCancelled(true);
		if (g.getVariable("edit") != null) {
			if ((boolean) g.getVariable("edit"))
				event.setCancelled(false);
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Gamer g = Gamer.get(event.getPlayer());
		event.setCancelled(true);
		if (g.getVariable("edit") != null) {
			if ((boolean) g.getVariable("edit"))
				event.setCancelled(false);
		}
	}
}
