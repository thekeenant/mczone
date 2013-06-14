package co.mczone.api.shop;

import lombok.Getter;

import org.bukkit.potion.PotionEffect;

import co.mczone.api.players.Gamer;
import co.mczone.api.server.Hive;

public class ShopAbility implements ShopItem {
	@Getter PotionEffect potion;
	@Getter String title;
	@Getter int price;
	@Getter String description;
	
	public ShopAbility(String title, String description, PotionEffect potion, int price) {
		this.title = title;
		this.description = description;
		this.potion = potion;
		this.price = price;
	}

	@Override
	public boolean buy(Gamer g, Object o) {
		int i = (Integer) o;
		if (g.getCredits() * i < price)
			return false;
		
		int endPrice = price * i;
		
		g.addPotionEffect(new PotionEffect(potion.getType(), potion.getDuration() *  i, potion.getAmplifier()), true);
		g.takeCredits(endPrice);
		Hive.getInstance().getShop().addPurchase(g);
		
		return true;
	}
}
