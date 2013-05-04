package co.mczone.sg.events;

import lombok.Getter;
import co.mczone.util.Chat;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import co.mczone.api.players.Gamer;
import co.mczone.events.custom.PlayerDamageEvent;
import co.mczone.sg.Scheduler;
import co.mczone.sg.api.GamerSG;
import co.mczone.sg.api.State;

public class Events implements Listener {
	@Getter static Events instance;
		
	@EventHandler
	public void onPlayerDamage(PlayerDamageEvent event) {
		GamerSG g = GamerSG.get(event.getTarget().getName());
		if (g.isSpectator())
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		GamerSG g = GamerSG.get(event.getPlayer().getName());
		if (g.isSpectator()) {
			event.getRecipients().clear();
			for (GamerSG t : GamerSG.getList())
				if (t.isSpectator())
					event.getRecipients().add(t.getPlayer());
		}
		else {
			event.getRecipients().clear();
			for (GamerSG t : GamerSG.getTributes())
				event.getRecipients().add(t.getPlayer());
		}
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		event.blockList().clear();
	}
	

	@EventHandler
	public void onDropItem(PlayerDropItemEvent event) {
		if (Scheduler.getState() != State.PVP || GamerSG.get(event.getPlayer().getName()).isSpectator())
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onPickupItem(PlayerPickupItemEvent event) {
		if (Scheduler.getState() != State.PVP || GamerSG.get(event.getPlayer().getName()).isSpectator())
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onSpectatorInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		GamerSG g = GamerSG.get(p.getName());
		if (!g.isSpectator())
			return;
		
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.PHYSICAL) {
			event.setCancelled(true);
			return;
		}
		
		int cur = g.getSpectating();
		int next = cur + 1;
		if (next + 1 >= GamerSG.getTributes().size())
			next = 0;
		g.setSpectating(next);
		Player t = GamerSG.getTributes().get(next).getPlayer();
		g.getPlayer().teleport(t);
		Chat.player(g.getPlayer(), "&2[SG] &eCurrently spectating " + Players.getPrefix(t.getName()) + t.getName());
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			GamerSG g = GamerSG.get(((Player) event.getEntity()).getName());
			if (g.isSpectator())
				event.setCancelled(true);
		}
		if (Scheduler.getState() != State.PVP)
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityTeleport(EntityTeleportEvent event) {
		GamerSG.hideSpectators();
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player))
			return;
		
		GamerSG g = GamerSG.get(((Player) event.getDamager()).getName());
		if (g.isSpectator())
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityTarget(EntityTargetEvent event) {
		if (Scheduler.getState() != State.PVP)
			event.setCancelled(true);
		if (event.getTarget() instanceof Player) {
			GamerSG g = GamerSG.get(((Player) event.getTarget()).getName());
			if (g == null) {
				((Player) event.getTarget()).kickPlayer("ERROR");
				return;
			}
			if (g.isSpectator())
				event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		GamerSG g = GamerSG.get(event.getPlayer().getName());
		if (Scheduler.getState() == State.WAITING && !g.isMoveable()) {
			Location to = g.getSpawnBlock();
			to.setYaw(event.getPlayer().getLocation().getYaw());
			to.setPitch(event.getPlayer().getLocation().getPitch());
			if (g.getSpawnBlock().getBlock().getX() != event.getTo().getBlock().getX())
				event.getPlayer().teleport(to);
			else if (g.getSpawnBlock().getBlock().getZ() != event.getTo().getBlock().getZ())
				event.getPlayer().teleport(to);
		}
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (Scheduler.getState() != State.PVP) {
			event.setFoodLevel(20);
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (Scheduler.getState() != State.PVP) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (Scheduler.getState() != State.PVP) {
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (Scheduler.getState() != State.PVP) {
			event.setCancelled(true);
			return;
		}
	}
}
