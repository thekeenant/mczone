package co.mczone.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import co.mczone.MCZone;
import co.mczone.events.custom.ModifyWorldAction;
import co.mczone.events.custom.PlayerModifyWorldEvent;

public class ModifyWorldEvents implements Listener {
	public ModifyWorldEvents() {
		MCZone.getInstance().getServer().getPluginManager().registerEvents(this, MCZone.getInstance());
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		PlayerModifyWorldEvent ev = new PlayerModifyWorldEvent(event.getPlayer(), ModifyWorldAction.PLAYER_INTERACT, event);
		Bukkit.getServer().getPluginManager().callEvent(ev);
		event.setCancelled(ev.isCancelled());
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		PlayerModifyWorldEvent ev = new PlayerModifyWorldEvent(event.getPlayer(), ModifyWorldAction.ITEM_DROP, event);
		Bukkit.getServer().getPluginManager().callEvent(ev);
		event.setCancelled(ev.isCancelled());
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerPickupItemEvent event) {
		PlayerModifyWorldEvent ev = new PlayerModifyWorldEvent(event.getPlayer(), ModifyWorldAction.ITEM_PICKUP, event);
		Bukkit.getServer().getPluginManager().callEvent(ev);
		event.setCancelled(ev.isCancelled());
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		PlayerModifyWorldEvent ev = new PlayerModifyWorldEvent(event.getPlayer(), ModifyWorldAction.BLOCK_PLACE, event);
		Bukkit.getServer().getPluginManager().callEvent(ev);
		event.setCancelled(ev.isCancelled());
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		PlayerModifyWorldEvent ev = new PlayerModifyWorldEvent(event.getPlayer(), ModifyWorldAction.BLOCK_BREAK, event);
		Bukkit.getServer().getPluginManager().callEvent(ev);
		event.setCancelled(ev.isCancelled());
	}
}
