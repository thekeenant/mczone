package co.mczone.api.players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import co.mczone.api.infractions.Infraction;

import lombok.Getter;
import lombok.Setter;

public class Gamer {
	@Getter static List<Gamer> list = new ArrayList<Gamer>();
	@Getter HashMap<String, Object> settings = new HashMap<String, Object>();
	@Getter String name;
	@Getter @Setter Rank rank;
	@Getter boolean invisible = false;
	
	@Getter @Setter boolean online;
	@Getter List<Infraction> infractions = new ArrayList<Infraction>();
	
	public Gamer(String name) {
		this.name = name;
		list.add(this);
	}
	
	public Player getPlayer() {
		Player p = Bukkit.getPlayerExact(name);
		if (p == null || !p.isOnline()) {
			online = false;
			return null;
		}
		return p;
	}
	
	public static Gamer get(String name) {
		for (Gamer g : list)
			if (g.getName().equalsIgnoreCase(name))
				return g;
		return null;
	}
	
	public static Gamer get(Player p) {
		return get(p.getName());
	}
	
	public void giveCredits(int amount) {
		
	}
	
	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
		updateHidden();
	}
	
	public static void updateHidden() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			Gamer gp = Gamer.get(p);
			for (Player t : Bukkit.getOnlinePlayers()) {
				Gamer gt = Gamer.get(t);
				if (gp.getName().equals(gt.getName()))
					continue;
				/*
				if (gt.isInvisible())
					gp.getPlayer().hidePlayer(gt.getPlayer());
				else
					gp.getPlayer().showPlayer(gt.getPlayer());
					*/
			}
		}
	}
	
	public void removePotionEffects() {
		for (PotionEffect p : getPlayer().getActivePotionEffects())
			getPlayer().removePotionEffect(p.getType());
	}
	
	public void removePotionEffect(PotionEffectType t) {
		getPlayer().removePotionEffect(t);
	}
	
	public void teleport(Object o) {
		Location to = null;
		if (o instanceof Location)
			to = (Location) o;
		else if (o instanceof Entity)
			to = ((Entity) o).getLocation();
		else if (o instanceof World)
			to = ((World) o).getSpawnLocation();
		
		if (to != null)
			getPlayer().teleport(to);
	}
	
	public void clearInventory() {
		clearInventory(true);
	}
	
	public void clearInventory(boolean andArmor) {
		getPlayer().getInventory().clear();
		if (andArmor)
			getPlayer().getInventory().setArmorContents(null);
	}
	
	// Custom settings
	public void setVariable(String key, Object value) {
		getSettings().put(key, value);
	}

	public Object getVariable(String key) {
		return getSettings().get(key);
	}
}
