package co.mczone.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;

import co.mczone.MCZone;
import co.mczone.events.custom.PlayerDamageEvent;
import co.mczone.events.custom.PlayerKilledEvent;
import co.mczone.util.Chat;
import co.mczone.api.players.CustomDamageCause;

public class DamageEvents implements Listener {
	HashMap<String, List<PlayerDamageEvent>> damages = new HashMap<String, List<PlayerDamageEvent>>();
	
	public DamageEvents() {
		MCZone.getInstance().getServer().getPluginManager().registerEvents(this, MCZone.getInstance());
	}
	
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent)
			return;
		
		if (event.getEntity() instanceof Player == false)
			return;

		Player t = (Player) event.getEntity();
		
		if (!damages.containsKey(t.getName()))
			damages.put(t.getName(), new ArrayList<PlayerDamageEvent>());
		
		PlayerDamageEvent callMe = new PlayerDamageEvent(t, event.getCause());
		Bukkit.getPluginManager().callEvent(callMe);
		event.setCancelled(callMe.isCancelled());
		
		if (event.isCancelled() == false)
			damages.get(t.getName()).add(callMe);
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		
		CustomDamageCause customCause = null;
		DamageCause cause = event.getCause();
		Entity e = event.getDamager();
		Player t = (Player) event.getEntity();

		if (e.hasMetadata("Cause"))
			customCause = CustomDamageCause.valueOf(e.getMetadata("Cause").get(0).asString().toUpperCase());
		
		if (e.hasMetadata("Player")) {
			e = Bukkit.getPlayerExact(e.getMetadata("Player").get(0).asString());
		}
		else if (e instanceof Projectile) {
			Projectile proj = (Projectile) e;
			if (proj.getShooter() instanceof Player)
				e = (Player) proj.getShooter();
		}
		
		if (!damages.containsKey(t.getName()))
			damages.put(t.getName(), new ArrayList<PlayerDamageEvent>());
		
		PlayerDamageEvent callMe = new PlayerDamageEvent(e, t, cause);
		callMe.setCustomCause(customCause);
		
		Bukkit.getPluginManager().callEvent(callMe);
		event.setCancelled(callMe.isCancelled());
		
		if (event.isCancelled() == false)
			damages.get(t.getName()).add(callMe);
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		PlayerDamageEvent lastDamage = null;
		PlayerKilledEvent callMe = null;
		Player t = (Player) event.getEntity();
		if (damages.containsKey(t.getName())) {
			lastDamage = damages.get(t.getName()).get(damages.get(t.getName()).size() - 1);
			
			if (lastDamage.isDamageByEntity()) {
				Entity e = lastDamage.getDamager();
				
				if (e instanceof Player)
					callMe = new PlayerKilledEvent((Player) e, t, event.getDeathMessage());
				else
					callMe = new PlayerKilledEvent(e, t, event.getDeathMessage());
			}
			else {
				callMe = new PlayerKilledEvent(t, event.getDeathMessage());
			}
		}
		
		if (callMe == null) {
			Chat.log("Error handling PlayerKilledEvent! " + event.getEntity().getLastDamageCause().getCause().name());
			callMe = new PlayerKilledEvent(t, event.getDeathMessage());
		}
		
		// Custom damage causes (TNT_MINE: "%t was blown up by %p")
		if (lastDamage.getCustomCause() != null) {
			String msg = lastDamage.getCustomCause().getDeathMessage();
			msg = msg.replace("%t", t.getName());
			
			if (lastDamage.isDamageByEntity()) {
				Entity e = lastDamage.getDamager();
				
				if (e instanceof Player)
					msg = msg.replace("%p", ((Player) e).getName());	
			}
			callMe.setDeathMessage(msg);
		}
		
		Bukkit.getPluginManager().callEvent(callMe);
		event.setDeathMessage(Chat.colors(callMe.getDeathMessage()));
	}
}
