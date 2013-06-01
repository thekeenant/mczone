package co.mczone.walls;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import co.mczone.api.players.Gamer;
import co.mczone.api.players.RankType;
import co.mczone.api.server.Hive;
import co.mczone.walls.utils.Chat;

public class Kit {
	public static List<Kit> list = new ArrayList<Kit>();
	public static HashMap<Player, Kit> kits = new HashMap<Player, Kit>();
	public static HashMap<String, List<Kit>> donations = new HashMap<String, List<Kit>>();
	public static HashMap<String, Kit> votes = new HashMap<String, Kit>();
	String name;
	List<ItemStack> items;
	
	public Kit(String name, List<ItemStack> items) {
		this.name = name;
		this.items = items;
		list.add(this);
	}
	
	public static void giveKit(final Player p) {
		Kit k = getKit(p);
		
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		
		for (ItemStack i : k.getItems()) {
			int id = i.getType().getId();
			if ((id < 298) || (317 < id))
                p.getInventory().addItem(i);
	        else if ((id == 298) || (id == 302)     || (id == 306) || (id == 310)   || (id == 314))
	                p.getInventory().setHelmet(new ItemStack(id, 1));
	        else if ((id == 299) || (id == 303)     || (id == 307) || (id == 311) || (id == 315))
	                p.getInventory().setChestplate(new ItemStack(id, 1));
	        else if ((id == 300) || (id == 304) || (id == 308) || (id == 312) || (id == 316))
	                p.getInventory().setLeggings(new ItemStack(id, 1));
	        else if ((id == 301) || (id == 305) || (id == 309) || (id == 313) || (id == 317))
	                p.getInventory().setBoots(new ItemStack(id, 1));
		}
		p.getInventory().addItem(new ItemStack(Material.COMPASS));
		for (PotionEffect potion : p.getActivePotionEffects())
			p.removePotionEffect(potion.getType());
	}
	
	public boolean hasPermission(String user) {
		RankType rank = Gamer.get(user).getRank().getType();
		if (name.equalsIgnoreCase("barbarian"))
			return true;
		if (name.equalsIgnoreCase("miner"))
			return true;
		if (name.equalsIgnoreCase("cook"))
			return true;
		if (rank.getLevel() >= RankType.TITAN.getLevel())
			return true;
		if (rank == RankType.ELITE && (name.equalsIgnoreCase("archer") || name.equalsIgnoreCase("warrior") || name.equalsIgnoreCase("beastmaster")))
			return true;
		if (Kit.votes.containsKey(user.toLowerCase()) && Kit.votes.get(user.toLowerCase())==this)
			return true;
		if (Kit.donations.containsKey(user.toLowerCase()) && Kit.donations.get(user.toLowerCase()).contains(this))
			return true;
		return false;
	}
	
	public static Kit getKit(Player p) {
		if (!kits.containsKey(p))
			kits.put(p, findByName("barbarian"));
		
		return kits.get(p);
	}
	
	public static Kit findByName(String name) {
		for (Kit k : list) {
			if (k.getName().equalsIgnoreCase(name))
				return k;
		}
		return null;
	}
	
	public String getName() {
		return name;
	}
	
	public List<ItemStack> getItems() {
		return items;
	}
	
	public static void list(CommandSender p) {
		String kits = "";
		for (Kit k : Kit.list) {
			if (k.hasPermission(p.getName()))
				kits += "&a" + Chat.capitalize(k.getName().toLowerCase()) + "&f, ";
			else
				kits += "&c" + Chat.capitalize(k.getName().toLowerCase()) + "&f, ";
		}
		kits = kits.substring(0, kits.length() - 2);
		Chat.simple(p, "&7[Kits] " + kits);
	}
    
	public static void load() {
	    new Thread(new Runnable() {
	        @Override
	        public void run() {
        		ResultSet r = Hive.getInstance().getDatabase().query("SELECT * FROM walls_donations");
        		Kit.votes.clear();
        		Kit.donations.clear();
        		try {
        			while (r.next()) {
        				String username = r.getString("username").toLowerCase();
        				String kit = r.getString("kit").toLowerCase();
        				boolean free = (r.getInt("free")==1) ? true : false;
        				if (free) {
        					Kit.votes.put(username, Kit.findByName(kit));
        				}
        				else {
        					if (!Kit.donations.containsKey(username))
        						Kit.donations.put(username, new ArrayList<Kit>());
        					Kit.donations.get(username).add(Kit.findByName(kit));
        				}
        			}
        		} catch (SQLException e) {
        			Chat.log("DB Error: Coulnd't load donations/votes!");
        		}
	        }
	    }).start();
	}
}
