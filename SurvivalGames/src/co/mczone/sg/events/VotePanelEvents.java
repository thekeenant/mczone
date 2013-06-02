package co.mczone.sg.events;

import lombok.Getter;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import co.mczone.sg.api.Map;
import co.mczone.sg.api.VotePanel;
import co.mczone.util.Chat;

public class VotePanelEvents implements Listener {
	@Getter static VotePanelEvents instance;
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player p = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();
		if (Chat.stripColor(inv.getTitle()).equalsIgnoreCase("Voting Panel")) {
			event.setCancelled(true);
			if (event.getRawSlot() > 9 || event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
				return;
			ItemStack i = event.getCurrentItem();
			Map m = Map.getByTitle(Chat.stripColor(i.getItemMeta().getDisplayName()));
			if (m != null) {
				m.addVote(p);
				p.closeInventory();
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onCompassClick(PlayerInteractEvent event) {
		if (event.getItem() == null || event.getItem().getType() != Material.EMERALD)
			return;
		Player p = event.getPlayer();
		ItemStack stack = event.getItem();
		if (stack.getItemMeta().hasDisplayName()) {
			String display = Chat.stripColor(stack.getItemMeta().getDisplayName());
			if (display.equalsIgnoreCase("voting panel")) {
				new VotePanel(p).show();
			}
		}
	}
}
