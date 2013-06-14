package co.mczone.api.shop;

import lombok.Getter;

import org.bukkit.inventory.ItemStack;

import co.mczone.api.players.Gamer;
import co.mczone.api.server.Hive;

public class ShopItemStack implements ShopItem {	
	@Getter ItemStack itemStack;
	@Getter String title;
	@Getter int price;
	@Getter String description;
	
	public ShopItemStack(String title, String description, ItemStack itemStack, int price) {
		this.title = title;
		this.description = description;
		this.itemStack = itemStack;
		this.price = price;
	}

	@Override
	public boolean buy(Gamer g, Object o) {
		if (g.getCredits() < price * (Integer) o)
			return false;

		int endPrice = price * (Integer) o;
		
		g.getPlayer().getInventory().addItem(new ItemStack(itemStack.getType(), (Integer) o));
		g.takeCredits(endPrice);
		Hive.getInstance().getShop().addPurchase(g);
		return true;
	}
}
