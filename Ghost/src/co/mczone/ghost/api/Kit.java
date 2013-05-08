package co.mczone.ghost.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import co.mczone.api.players.Gamer;
import co.mczone.api.players.Permissible;
import co.mczone.api.players.RankType;
import co.mczone.ghost.Ghost;
import co.mczone.util.Chat;

public class Kit implements Permissible {
	@Getter static HashMap<String, List<Kit>> purchases = new HashMap<String, List<Kit>>();
	@Getter static List<Kit> list = new ArrayList<Kit>();
	@Getter String name;
	@Getter List<ItemStack> items;
	@Getter boolean free;
	
	public Kit(String name, List<ItemStack> items) {
		this.name = name;
		this.items = items;
		list.add(this);
	}
	
	public String getTitle() {
		return Chat.capitalize(name.toLowerCase());
	}
	
	public static void giveKit(final Player p) {
		Gamer g = Gamer.get(p.getName());
		g.clearInventory();
		Kit k = (Kit) g.getVariable("kit");
		
		if (k == null) {
			return;
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

	@Override
	public boolean hasPermission(Gamer g) {
		RankType r = g.getRank().getType();
		if (r.getLevel() >= RankType.TITAN.getLevel()) {
			return true;
		}
		else if (r.getLevel() >= RankType.ELITE.getLevel()) {
			if (Ghost.getConf().getStringList("elite-kits").contains(name.toLowerCase()))
				return true;
		}
		else if (r.getLevel() >= RankType.VIP.getLevel()) {
			if (Ghost.getConf().getStringList("vip-kits").contains(name.toLowerCase()))
				return true;
		}
		else if (Kit.getPurchases().containsKey(g.getName().toLowerCase())) {
			if (Kit.getPurchases().get(g.getName().toLowerCase()).contains(this))
				return true;
		}
		else {
			if (name.equals("archer"))
				return true;
		}
		
		return false;
	}
}
