package co.mczone.ghost.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import co.mczone.api.players.Gamer;

public class Kit {
	@Getter static List<Kit> list = new ArrayList<Kit>();
	@Getter	public static HashMap<String, List<Kit>> donations = new HashMap<String, List<Kit>>();
	@Getter public static HashMap<String, Kit> votes = new HashMap<String, Kit>();
	@Getter String name;
	@Getter List<ItemStack> items;
	@Getter boolean free;
	
	public Kit(String name, List<ItemStack> items) {
		this.name = name;
		this.items = items;
		list.add(this);
	}
	
	public static void giveKit(final Player p) {
		Gamer g = Gamer.get(p.getName());
		g.clearInventory();
		Kit k = (Kit) g.getSettings().get("kit");
		
		if (k == null) {
			k = Kit.get("barbarian");
			g.setVariable("kit", k);
		}
		
		List<ItemStack> items = k.getItems();
		
		for (ItemStack i : items) {
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
		for (PotionEffect potion : p.getActivePotionEffects())
			p.removePotionEffect(potion.getType());
		p.getInventory().addItem(new ItemStack(Material.COMPASS));
	}
	
	public static Kit get(String name) {
		for (Kit k : list) {
			if (k.getName().equalsIgnoreCase(name))
				return k;
		}
		return null;
	}
}
