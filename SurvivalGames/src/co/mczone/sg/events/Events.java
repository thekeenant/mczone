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
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import co.mczone.api.players.Gamer;
import co.mczone.events.custom.PlayerDamageEvent;
import co.mczone.sg.Scheduler;
import co.mczone.sg.api.Game;
import co.mczone.sg.api.Map;
import co.mczone.sg.api.State;

public class Events implements Listener {
	@Getter static Events instance;
		
	@EventHandler
	public void onPlayerDamage(PlayerDamageEvent event) {
		Gamer g = Gamer.get(event.getTarget().getName());
		if (g.isInvisible())
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Gamer g = Gamer.get(event.getPlayer().getName());
		if (g.isInvisible()) {
			event.getRecipients().clear();
			for (Gamer t : Gamer.getList())
				if (t.isInvisible())
					event.getRecipients().add(t.getPlayer());
		}
		else {
			event.getRecipients().clear();
			for (Gamer t : Game.getTributes())
				event.getRecipients().add(t.getPlayer());
		}
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		event.blockList().clear();
	}
	

	@EventHandler
	public void onDropItem(PlayerDropItemEvent event) {
		if (Scheduler.getState() != State.PVP || Gamer.get(event.getPlayer().getName()).isInvisible())
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onPickupItem(PlayerPickupItemEvent event) {
		if (Scheduler.getState() != State.PVP || Gamer.get(event.getPlayer().getName()).isInvisible())
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onSpectatorInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		Gamer g = Gamer.get(p.getName());
		if (!g.isInvisible())
			return;
		
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.PHYSICAL) {
			event.setCancelled(true);
			return;
		}
		
		
		int cur = 0;
		if (g.getVariable("spectating") != null)
			cur = (Integer) g.getVariable("spectating");
		int next = cur + 1;
		if (next + 1 >= Game.getTributes().size())
			next = 0;
		g.setVariable("spectating", next);
		Gamer t = Game.getTributes().get(next);
		g.getPlayer().teleport(t.getPlayer().getLocation());
		Chat.player(g.getPlayer(), "&2[SG] &eCurrently spectating " + g.getPrefix() + g.getName());
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Gamer g = Gamer.get(((Player) event.getEntity()).getName());
			if (g.isInvisible())
				event.setCancelled(true);
		}
		if (Scheduler.getState() != State.PVP)
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player))
			return;
		
		Gamer g = Gamer.get(((Player) event.getDamager()).getName());
		if (g.isInvisible())
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityTarget(EntityTargetEvent event) {
		if (Scheduler.getState() != State.PVP)
			event.setCancelled(true);
		if (event.getTarget() instanceof Player) {
			Gamer g = Gamer.get(((Player) event.getTarget()).getName());
			if (g == null) {
				((Player) event.getTarget()).kickPlayer("ERROR");
				return;
			}
			if (g.isInvisible())
				event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Gamer g = Gamer.get(event.getPlayer().getName());
		if (Scheduler.getState() == State.WAITING && !g.getBoolean("moveable")) {

			Location to = g.getLocation("spawn-block");
			if (to == null)
				return;
			
			if (Map.getCurrent().getWorld() != g.getPlayer().getWorld()) {
				event.getPlayer().teleport(to);
				return;
			}

			to.setYaw(event.getPlayer().getLocation().getYaw());
			to.setPitch(event.getPlayer().getLocation().getPitch());
			if (to.getBlock().getX() != event.getTo().getBlock().getX())
				event.getPlayer().teleport(to);
			else if (to.getBlock().getZ() != event.getTo().getBlock().getZ())
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
