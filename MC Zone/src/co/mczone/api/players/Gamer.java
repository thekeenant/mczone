package co.mczone.api.players;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import co.mczone.api.infractions.Infraction;
import co.mczone.api.server.Hive;

import lombok.Getter;
import lombok.Setter;

public class Gamer {
	@Getter static List<Gamer> list = new ArrayList<Gamer>();
	@Getter HashMap<String, Object> settings = new HashMap<String, Object>();
	@Getter String name;
	@Getter @Setter Rank rank;
	@Getter boolean invisible = false;
	
	@Getter int credits;
	
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
		if (p == null)
			return null;
		return get(p.getName());
	}
	
	public void giveCredits(int amount) {
		Hive.getInstance().getDatabase().update("UPDATE players SET credits=credits+" + amount + " WHERE username='" + name + "'");
		updateCredits();
	}
	
	public void updateCredits() {
		ResultSet r = Hive.getInstance().getDatabase().query("SELECT credits FROM players WHERE username='" + name + "'");
		try {
			while (r.next()) {
				credits = r.getInt("credits");
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		credits = 0;
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
				if (gt.isInvisible())
					gp.getPlayer().hidePlayer(gt.getPlayer());
				else
					gp.getPlayer().showPlayer(gt.getPlayer());
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
	
	public void giveItem(ItemStack i) {
		giveItem(i, true);
	}

	public void giveItem(ItemStack i, boolean addToArmor) {
		int id = i.getTypeId();
		Player p = getPlayer();
		if (!addToArmor) {
			p.getInventory().addItem(i);
			return;
		}
		
		if ((id < 298) || (317 < id))
			p.getInventory().addItem(i);
		else if ((id == 298) || (id == 302) || (id == 306) || (id == 310) || (id == 314))
			p.getInventory().setHelmet(new ItemStack(id, 1));
		else if ((id == 299) || (id == 303) || (id == 307) || (id == 311) || (id == 315))
			p.getInventory().setChestplate(new ItemStack(id, 1));
		else if ((id == 300) || (id == 304) || (id == 308) || (id == 312) || (id == 316))
			p.getInventory().setLeggings(new ItemStack(id, 1));
		else if ((id == 301) || (id == 305) || (id == 309) || (id == 313) || (id == 317))
			p.getInventory().setBoots(new ItemStack(id, 1));
	}
	
	public boolean hasPermission(Permissible perm) {
		return perm.hasPermission(this);
	}

	public String getPrefix() {
		return this.getRank().getPrefix();
	}
	
	public void kill(Player target) {
		Hive.getInstance().kill(target, name);
	}
	
	public void setAllowFlight(boolean flight) {
		getPlayer().setAllowFlight(flight);
	}
	
	public void setFlying(boolean flight) {
		getPlayer().setFlying(flight);
	}
}
