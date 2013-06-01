package co.mczone.walls.events;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.server.ServerListPingEvent;

import co.mczone.walls.Kit;
import co.mczone.walls.State;
import co.mczone.walls.Team;
import co.mczone.walls.Walls;
import co.mczone.walls.utils.Chat;
import co.mczone.walls.utils.Utils;

public class Events implements Listener {
	public Events() {
		Bukkit.getPluginManager().registerEvents(this, Walls.instance);
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
	    if (!State.PRE) {
	        event.disallow(Result.KICK_OTHER, ChatColor.RED + "Game has already started!");
	    }
	}

	@EventHandler
	public void onServerPingEvent(ServerListPingEvent event) {
		if (State.PRE) {
			event.setMotd(ChatColor.GREEN + "" + Utils.getPlayers().length + "/" + Bukkit.getMaxPlayers() + " ready to play!");
		}
		else {
			event.setMotd(Utils.getPlayers().length + " players remaining...");
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		event.setJoinMessage(null);
		
		if (Team.getTeam(p) == null) {
			Team team = Walls.BLUE;
			if (Walls.RED.getMembers().size() < team.getMembers().size())
				team = Walls.RED;
			if (Walls.GREEN.getMembers().size() < team.getMembers().size())
				team = Walls.GREEN;
			if (Walls.YELLOW.getMembers().size() < team.getMembers().size())
				team = Walls.YELLOW;

			team.join(p);
		}

		Team team = Team.getTeam(p);
		Kit.giveKit(p);
		p.setDisplayName(team.getColor() + p.getName());
        Chat.setPlayerListName(p, team.getColor() + p.getName());
        p.teleport(team.getSpawn());
        
		Chat.simple(p, "&bChoose your kit with &o/kit [kit name]&b!");
        Kit.list(p);
	}
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
		if (Walls.getSpectators().contains(event.getPlayer().getName())) {
			event.setCancelled(true);
			return;
		}
    	
        if (State.PVP)
            return;
        
        Block b = event.getBlock();
        if (b == null)
            return;
        Team team = Team.getTeam(event.getPlayer());
        if (b.getX() < team.getMin().getX() + 1 || b.getZ() < team.getMin().getZ() + 1) {
            event.setCancelled(true);
        }
        if (b.getX() > team.getMax().getX() - 1 || b.getZ() > team.getMax().getZ() - 1) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
		if (Walls.getSpectators().contains(event.getPlayer().getName())) {
			event.setCancelled(true);
			return;
		}
		
        if (event.getItem()==null) 
            return;
        
        if (!State.PVP && (event.getItem().getType()==Material.FLINT_AND_STEEL || event.getItem().getType()==Material.FIREBALL || event.getItem().getType()==Material.LAVA_BUCKET)) {
            Chat.player(event.getPlayer(), "&cYou are not allowed to use fire before the walls drop!");
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
		if (Walls.getSpectators().contains(event.getPlayer().getName())) {
			event.setCancelled(true);
			return;
		}
		
        if (State.PVP)
            return;
        
        Block b = event.getBlock();
        if (b.getType()==Material.TNT && !State.PVP) {
            Chat.player(event.getPlayer(), "&cYou are not allowed to use TNT before the walls drop!");
            event.setCancelled(true);
        }
        
        
        Team team = Team.getTeam(event.getPlayer());
        if (b.getX() < team.getMin().getX() + 1 || b.getZ() < team.getMin().getZ() + 1) {
            event.setCancelled(true);
        }
        if (b.getX() > team.getMax().getX() - 1 || b.getZ() > team.getMax().getZ() - 1) {
            event.setCancelled(true);
        }
    }
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
	    if (State.PVP)
	        return;
	    
		Block b = event.getTo().getBlock();
		Team team = Team.getTeam(event.getPlayer());
		if (team == null)
			return;
		
        if (b.getX() < team.getMin().getX() + 1 || b.getZ() < team.getMin().getZ() + 1) {
            Chat.player(event.getPlayer(), "&cYou cannot leave your team's quadrant!");
            event.getPlayer().teleport(event.getFrom());
        }
        if (b.getX() > team.getMax().getX() - 1 || b.getZ() > team.getMax().getZ() - 1) {
            Chat.player(event.getPlayer(), "&cYou cannot leave your team's quadrant!");
            event.getPlayer().teleport(event.getFrom());
        }
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
	    event.setLeaveMessage(null);
	}
	
	
	@EventHandler
	public void Compass(PlayerInteractEvent event) {
		if (event.getItem() != null && event.getItem().getType() != Material.AIR && event.getItem().getType()==Material.COMPASS) {
			Player p = event.getPlayer();
			for (int i = 0; i < 500; i+=20) {
				List<Entity> entities = p.getNearbyEntities(i, 256, i);
				for (Entity e : entities) {
					if (e instanceof Player) {
						Player found = (Player) e;
						if (Team.getTeam(p)!=Team.getTeam(found)) {
							Chat.player(p, "&aCompass now points towards " + found.getDisplayName() + "&a!");
							p.setCompassTarget(e.getLocation());
							return;
						}
					}
				}
			}
			Chat.player(p, "&cNo enemy players found near you!");
		}
	}
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (event.getSpawnReason()!=SpawnReason.SPAWNER_EGG)
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityTargetEvent(EntityTargetEvent event) {
		if (event.getTarget() instanceof Player && Walls.getSpectators().contains(((Player) event.getTarget()).getName())) {
			event.setCancelled(true);
		}
	}
}
