package co.mczone.sg.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import co.mczone.util.Chat;

public class VotePanel {
	Player p;
	
	public VotePanel(Player p) {
		this.p = p;
	}
	
	public void show() {
		Inventory inv = Bukkit.getServer().createInventory(p, 9, Chat.colors("&oVoting Panel"));
		int index = 0;
		for (Map m : Map.getList()) {
			ItemStack item = new ItemStack(Material.WOOL, 1, (short) index);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.AQUA + m.getTitle());
			List<String> lore = new ArrayList<String>();
			lore.add(Chat.colors("&2Votes: &f" + m.getVotes().size()));
			lore.add(Chat.colors("&7Click to vote for this map!"));
			meta.setLore(lore);
			item.setItemMeta(meta);
			inv.addItem(item);
			
			index += 1;
		}
		p.openInventory(inv);
	}

}
