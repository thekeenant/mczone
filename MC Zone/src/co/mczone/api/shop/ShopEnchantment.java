package co.mczone.api.shop;

import lombok.Getter;

import org.bukkit.enchantments.Enchantment;

import co.mczone.api.players.Gamer;
import co.mczone.api.server.Hive;

public class ShopEnchantment implements ShopItem {
	@Getter Enchantment enchantment;
	@Getter String title;
	@Getter int price;
	@Getter int level;
	@Getter String description;
	
	public ShopEnchantment(String title, String description, Enchantment ench, int level, int price) {
		this.title = title;
		this.description = description;
		this.enchantment = ench;
		this.level = level;
		this.price = price;
	}

	@Override
	public boolean buy(Gamer g, Object o) {
		if (g.getCredits() < price)
			return false;
		
		g.getPlayer().getItemInHand().addUnsafeEnchantment(enchantment, level);
		g.takeCredits(price);
		Hive.getInstance().getShop().addPurchase(g);
		
		return true;
	}
}
