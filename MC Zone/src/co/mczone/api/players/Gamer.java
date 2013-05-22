package co.mczone.api.players;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.server.v1_5_R3.Packet41MobEffect;
import net.minecraft.server.v1_5_R3.Packet42RemoveMobEffect;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import co.mczone.MCZone;
import co.mczone.api.infractions.Infraction;
import co.mczone.api.server.Hive;
import co.mczone.util.Chat;

import lombok.Getter;
import lombok.Setter;

public class Gamer {
	@Getter static List<Gamer> list = new ArrayList<Gamer>();
	
	private static HashMap<String, GamerRunnable> functions = new HashMap<String, GamerRunnable>();
	private HashMap<String, Object> settings = new HashMap<String, Object>();
	
	@Getter String name;
	@Getter @Setter Rank rank;
	@Getter boolean invisible = false;
	
	@Getter int credits;
	
	@Getter @Setter boolean online;
	@Getter List<Infraction> infractions = new ArrayList<Infraction>();
	
	// For potion effects via packets
	List<PotionEffectType> effects = new ArrayList<PotionEffectType>();
	
	public Gamer(String name) {
		this.name = name;
		list.add(this);
	}
	
	public Gamer(String name, Rank rank) {
		this.name = name;
		this.rank = rank;
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
	
	public static Gamer get(CommandSender sender) {
		if (sender instanceof Player == false) {
			return new Gamer("CONSOLE", new Rank(RankType.CONSOLE));
		}
		
		for (Gamer g : list)
			if (g.getName().equalsIgnoreCase(sender.getName()))
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
	
	public void addPotionEffect(PotionEffect effect) {
		addPotionEffect(effect, true);
	}
	
	public void addPotionEffect(PotionEffect effect, boolean particles) {
		if (particles) {
			getPlayer().addPotionEffect(effect);
		}
		else {
			Player p = getPlayer();
			Packet41MobEffect pm = new Packet41MobEffect();
			pm.a = p.getEntityId();
			pm.b = (byte) effect.getType().getId();
			pm.c = (byte) effect.getAmplifier();
			
			// 32767 means xx:xx
			if (effect.getDuration() > 32767)
				pm.d = 32767;
			else
				pm.d = (short) effect.getDuration();
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(pm);
			pm = null;
		}
		
		effects.add(effect.getType());
	}
	
	public void removePotionEffects() {
		for (PotionEffect effect : getPlayer().getActivePotionEffects()) {
			removePotionEffect(effect.getType());
		}
		
		Iterator<PotionEffectType> list = effects.iterator();
		while (list.hasNext()) {
			removePotionEffect(list.next());
			list.remove();
		}
	}
	
	public void removePotionEffect(PotionEffectType effect) {
		getPlayer().removePotionEffect(effect);
		
		Player p = getPlayer();
		Packet42RemoveMobEffect pm = new Packet42RemoveMobEffect();
		pm.a = p.getEntityId();
		pm.b = (byte) effect.getId();
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(pm);
		pm = null;
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
		settings.put(key, value);
	}

	public Object getVariable(String key) {
		return settings.get(key);
	}

	public void clearVariable(String string) {
		if (settings.containsKey(string))
			settings.remove(string);
	}
	
	public boolean getBoolean(String key) {
		if (!settings.containsKey(key))
			return false;
		return (Boolean) settings.get(key);
	}
	
	public String getString(String key) {
		if (!settings.containsKey(key))
			return null;
		return (String) settings.get(key);
	}

	public int getInt(String key) {
		if (!settings.containsKey(key))
			return 0;
		return (Integer) settings.get(key);
	}

	public Location getLocation(String key) {
		if (!settings.containsKey(key))
			return null;
		return (Location) settings.get(key);
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
	
	public void run(String function) {
		GamerRunnable r = functions.get(function).newInstance();
		r.setGamer(this);
		
		if (r.isSync())
			r.runTask(MCZone.getInstance());
		else
			r.runTaskAsynchronously(MCZone.getInstance());
	}
	
	public static void addFunction(String key, GamerRunnable function) {
		functions.put(key, function);
	}

	public void clearScoreboard() {
		getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());	
	}
	
	public void sendMessage(String msg) {
		Chat.player(this, msg);
	}
	
	public void sendToHub() {
		sendToHub(null);
	}
	
	public void sendToHub(String msg) {
		if (msg != null)
			sendMessage(msg);

		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
		    out.writeUTF("Connect");
		    out.writeUTF("login"); // Target Server
		} catch (IOException e) {
		    // Can never happen
		}
		getPlayer().sendPluginMessage(MCZone.getInstance(), "BungeeCord", b.toByteArray());
	}
	
	public void sendToServer(String ip, int port) {
		sendToServer(null, ip, port);
	}
	
	public void sendToServer(String msg, String ip, int port) {
		if (msg != null)
			sendMessage(msg);

		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
		    out.writeUTF("Transfer");
		    out.writeUTF(ip + ":" + port); // Target Server
		} catch (IOException e) {
		    // Can never happen
		}
		getPlayer().sendPluginMessage(MCZone.getInstance(), "BungeeCord", b.toByteArray());
	}
}
