package co.mczone.ghost.api;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import co.mczone.api.players.Gamer;

public class Kit {
	@Getter static List<Kit> list = new ArrayList<Kit>();
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
		
		for (ItemStack i : items)
			g.giveItem(i);
		
		g.removePotionEffects();
	}
	
	public static Kit get(String name) {
		for (Kit k : list) {
			if (k.getName().equalsIgnoreCase(name))
				return k;
		}
		return null;
	}
}
