package co.mczone.lobby.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import co.mczone.api.players.Gamer;
import co.mczone.api.players.RankType;
import co.mczone.lobby.Lobby;
import co.mczone.util.Chat;
import co.mczone.util.RandomUtil;

public class Events implements Listener {
	public Events() {
		Lobby.getInstance().getServer().getPluginManager().registerEvents(this, Lobby.getInstance());
	}
	
	@Getter static HashMap<String, Date> joinTimes = new HashMap<String, Date>();
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		joinTimes.put(event.getPlayer().getName(), new Date());
	}


	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		joinTimes.put(event.getPlayer().getName(), new Date());
		event.setJoinMessage(null);
		p.setHealth(20);
		p.setFoodLevel(20);
		p.getInventory().clear();
		
		Location l = event.getPlayer().getWorld().getSpawnLocation();
		l.setX(14.5);
		l.setY(10.5);
		l.setZ(-795.5);
		l.setPitch(0.5F);
		l.setYaw(90 * RandomUtil.between(1, 4));
		
		
		
		p.teleport(l);
		p.setGameMode(GameMode.SURVIVAL);
		p.getInventory().addItem(Lobby.book);
		p.getInventory().addItem(Lobby.compass);
		Lobby.addPotionEffect(event.getPlayer(), new PotionEffect(PotionEffectType.SPEED, 2147483647, 2));
		
		if (Gamer.get(p.getName()).getRank().getLevel() >= RankType.ADMIN.getLevel())
			event.getPlayer().addAttachment(Lobby.getInstance(), "bukkit.command.gamemode", true);
	}

	@Getter static List<String> movement = new ArrayList<String>();
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (!movement.contains(event.getPlayer().getName())) {
			Date now = new Date();
			Date last = joinTimes.get(event.getPlayer().getName());
			int delay = 5;
			if (now.getTime() - last.getTime() > delay * 1000) {
				movement.add(event.getPlayer().getName());
			}
		}
	}
	
	@EventHandler
	public void onEntitySpawn(CreatureSpawnEvent event) {
		if (event.getSpawnReason() == SpawnReason.CUSTOM || event.getSpawnReason() == SpawnReason.EGG)
			return;
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityCombust(EntityCombustEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		event.setFoodLevel(20);
		if (event.getEntity() instanceof Player)
			((Player) event.getEntity()).setHealth(20);
	}
	
	@EventHandler
	public void onDropItem(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		event.setCancelled(false);
		
		if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		event.setCancelled(false);
		
		if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
			event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		event.setLeaveMessage(null);
	}

	
	HashMap<String, Date> chatTimes = new HashMap<String, Date>();
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		//event.getRecipients().clear();
		if (!chatTimes.containsKey(event.getPlayer().getName())) {
			chatTimes.put(event.getPlayer().getName(), new Date());
			return;
		}
		
		
		if (Gamer.get(event.getPlayer()).getRank().getLevel() >= 5)
			return;
		
		Date now = new Date();
		Date before = chatTimes.get(event.getPlayer().getName());
		
		if (now.getTime() - before.getTime() < 5000) {
			Chat.player(event.getPlayer(), "&cPlease wait 5 seconds between sending messages.");
			event.getRecipients().clear();
			event.setCancelled(true);
			return;
		}
		chatTimes.put(event.getPlayer().getName(), now);
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		event.setCancelled(true);
		
		if (event.getCause()==DamageCause.VOID) {
			Location l = event.getEntity().getWorld().getSpawnLocation();
			l.setX(14.5);
			l.setY(10.5);
			l.setZ(-795.5);
			l.setPitch(0.5F);
			l.setYaw(90 * RandomUtil.between(1, 4));
			event.getEntity().teleport(l);
		}
	}
	
	/*
    @EventHandler
    public void join(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setAllowFlight(true);
        player.setFlying(true);
    }
   
    @EventHandler
    public void setFlyOnJump(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
       
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
           
            player.setFlying(false);
            Vector v = event.getPlayer().getLocation().getDirection().multiply(0.5).setY(1.5);
            player.setVelocity(v);
           
            event.setCancelled(true);
        }
    }
    */
    
}