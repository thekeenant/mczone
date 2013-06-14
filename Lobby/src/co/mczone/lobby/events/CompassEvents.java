package co.mczone.lobby.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import co.mczone.util.Chat;
import co.mczone.lobby.Lobby;
import co.mczone.lobby.api.GameIcon;

public class CompassEvents implements Listener {
	public CompassEvents() {
		Lobby.getInstance().getServer().getPluginManager().registerEvents(this, Lobby.getInstance());
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player p = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();

		if (p.getGameMode() != GameMode.CREATIVE)
			event.setCancelled(true);
		
		if (Chat.stripColor(inv.getTitle()).equalsIgnoreCase("MC Zone Games")) {
			if (event.getRawSlot() > 9 || event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
				return;
			GameIcon game = GameIcon.get(event.getCurrentItem());
			if (game != null) {
				p.teleport(game.getTo());
				p.closeInventory();
			}
		}
	}
	
	@EventHandler
	public void onCompassClick(PlayerInteractEvent event) {
		if (event.getItem() == null || event.getItem().getType() != Material.COMPASS)
			return;
		
		Player p = event.getPlayer();
		ItemStack stack = event.getItem();
		if (stack.getItemMeta().hasDisplayName()) {
			String display = Chat.stripColor(stack.getItemMeta().getDisplayName());
			if (display.equalsIgnoreCase("game picker")) {
				Inventory inv = Bukkit.getServer().createInventory(p, 9, Chat.colors("&oMC Zone Games"));
				for (int i = 1; i < 12; i++) {

					int number = i - 1;
					
					if (i > GameIcon.getList().size())
						break;
					
					inv.setItem(i, GameIcon.getList().get(number).getItemStack());
				}
				p.openInventory(inv);
			}
		}
	}
}
