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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import co.mczone.events.custom.PlayerDamageEvent;
import co.mczone.events.custom.PlayerKilledEvent;
import co.mczone.util.Chat;

public class DamageEvents implements Listener {
	HashMap<String, List<EntityDamageEvent>> damages = new HashMap<String, List<EntityDamageEvent>>();
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent)
			return;
		
		if (event.getEntity() instanceof Player == false)
			return;

		Player t = (Player) event.getEntity();
		
		if (!damages.containsKey(t.getName()))
			damages.put(t.getName(), new ArrayList<EntityDamageEvent>());
		damages.get(t.getName()).add(event);
		
		PlayerDamageEvent callMe = new PlayerDamageEvent(t, event.getCause());
		Bukkit.getPluginManager().callEvent(callMe);
		event.setCancelled(callMe.isCancelled());
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		
		
		Entity e = event.getDamager();
		Player p = null;
		Player t = (Player) event.getEntity();
		
		if (e instanceof Player) {
			p = (Player) e;
		}
		else if (e instanceof Projectile) {
			Projectile proj = (Projectile) e;
			if (proj.getShooter() instanceof Player)
				p = (Player) proj.getShooter();
		}
		else if (e.hasMetadata("Player")) {
			String s = e.getMetadata("Player").get(0).asString();
			Player test = Bukkit.getPlayerExact(s);
			if (test != null && test.isOnline()) {
				p = test;
			}
		}
		
		if (!damages.containsKey(t.getName()))
			damages.put(t.getName(), new ArrayList<EntityDamageEvent>());
		damages.get(t.getName()).add(event);
		
		if (p == null)
			return;
		
		PlayerDamageEvent callMe = new PlayerDamageEvent(p, t, event.getCause());
		Bukkit.getPluginManager().callEvent(callMe);
		event.setCancelled(callMe.isCancelled());		
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		
		PlayerKilledEvent callMe = null;
		Player t = (Player) event.getEntity();
		if (damages.containsKey(t.getName())) {
			EntityDamageEvent ev = damages.get(t.getName()).get(damages.get(t.getName()).size() - 1);
			if (ev instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent ede = (EntityDamageByEntityEvent) ev;
				Entity e = ede.getDamager();
				
				if (e instanceof Player)
					callMe = new PlayerKilledEvent((Player) e, t, event.getDeathMessage());
				else
					callMe = new PlayerKilledEvent(e, t, event.getDeathMessage());
			}
			else {
				if (ev.getCause() == DamageCause.FALL)
					event.setDeathMessage(event.getDeathMessage() + " &7(" + t.getFallDistance() + " blocks)");
				
				callMe = new PlayerKilledEvent(t, event.getDeathMessage());
			}
			
		}
		
		if (callMe == null) {
			Chat.log("Error handling PlayerKilledEvent! " + event.getEntity().getLastDamageCause().getCause().name());
			new PlayerKilledEvent(t, event.getDeathMessage());
		}
		
		Bukkit.getPluginManager().callEvent(callMe);
		event.setDeathMessage(callMe.getDeathMessage());
	}
}
