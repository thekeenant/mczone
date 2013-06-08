package co.mczone.nexus.api;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import co.mczone.api.players.Gamer;
import co.mczone.api.players.Permissible;
import co.mczone.api.players.RankType;
import co.mczone.nexus.Nexus;
import co.mczone.nexus.enums.GameState;
import co.mczone.util.Chat;

public class Kit implements Permissible {
	@Getter static List<Kit> list = new ArrayList<Kit>();
	@Getter static HashMap<String, List<Kit>> donations = new HashMap<String, List<Kit>>();
	@Getter String name;
	@Getter List<ItemStack> items;
	@Getter boolean free;
	
	public Kit(String name, List<ItemStack> items) {
		this.name = name;
		this.items = items;
		list.add(this);
	}
	
	public void giveKit(Gamer g) {
		g.clearInventory();
		
		List<ItemStack> items = getItems();
		
		for (ItemStack i : items) {
			g.giveItem(i);
		}
		
		g.removePotionEffects();
	}
	
	public static Kit get(String name) {
		for (Kit k : list) {
			if (k.getName().equalsIgnoreCase(name))
				return k;
		}
		return null;
	}
	
	public static void list(CommandSender p) {
		Gamer g = Gamer.get(p.getName());
		String kits = "";
		for (Kit k : Kit.list) {
			if (!(p instanceof Player) || k.hasPermission(g))
				kits += "&a" + Chat.capitalize(k.getName().toLowerCase()) + "&f, ";
			else
				kits += "&c" + Chat.capitalize(k.getName().toLowerCase()) + "&f, ";
		}
		kits = kits.substring(0, kits.length() - 2);
		Chat.player(p, "&7[Kits] " + kits);
	}
    
	public static void load() {
	    new Thread(new Runnable() {
	        @Override
	        public void run() {
        		ResultSet r = Nexus.getDatabase().query("SELECT * FROM nexus_donations");
        		Kit.donations.clear();
        		try {
        			while (r.next()) {
        				String username = r.getString("username").toLowerCase();
        				String kit = r.getString("kit").toLowerCase();
        				boolean free = (r.getInt("free")==1) ? true : false;
        				if (!free) {
        					if (!Kit.donations.containsKey(username))
        						Kit.donations.put(username, new ArrayList<Kit>());
        					Kit.donations.get(username).add(Kit.get(kit));
        				}
        			}
        		} catch (SQLException e) {
        			Chat.log("DB Error: Coulnd't load donations/votes!");
        		}
	        }
	    }).start();
	}

	public static void addEffects(Gamer g) {
		if (Nexus.getRotary().getState() != GameState.PLAYING)
			return;
		
		if (g.getVariable("spectator") != null)
			return;
		
		Kit kit = (Kit) g.getVariable("kit");
		
		if (kit.getName().equals("spy")) {
			g.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 200, 1), true);
		}
		else if (kit.getName().equals("scout")) {
			g.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 400, 1), true);
			g.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, 2), true);
		}
		else if (kit.getName().equals("tank")) {
			g.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 400, 2), true);
		}
	}

	@Override
	public boolean hasPermission(Gamer g) {
		RankType rank = g.getRank().getType();
		if (name.equalsIgnoreCase("barbarian") || name.equalsIgnoreCase("warrior") || name.equalsIgnoreCase("medic") || name.equalsIgnoreCase("archer"))
			return true;
		if (rank.getLevel() >= RankType.TITAN.getLevel())
			return true;
		if (Kit.donations.containsKey(g.getName().toLowerCase()) && Kit.donations.get(g.getName().toLowerCase()).contains(Kit.get(name)))
			return true;
		return false;
	}
}
