package co.mczone.walls.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import co.mczone.walls.Walls;

public class PreEvents implements Listener {
	public PreEvents() {
		Bukkit.getPluginManager().registerEvents(this, Walls.instance);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onEntityTarget(EntityTargetEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onShootBow(EntityShootBowEvent event) {
		event.setCancelled(true);
	}
	
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		event.setCancelled(true);
	}

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }
}
