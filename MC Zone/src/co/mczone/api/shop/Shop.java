package co.mczone.api.shop;

import java.util.ArrayList;
import java.util.List;

import co.mczone.api.players.Gamer;

import lombok.Getter;
import lombok.Setter;

public class Shop {
	@Getter @Setter boolean allow = false;
	@Getter @Setter List<ShopItem> list = new ArrayList<ShopItem>();

	public ShopItem get(String title) {
		for (ShopItem item : getList())
			if (item.getTitle().equalsIgnoreCase(title))
				return item;
		return null;
	}
	
	public void addPurchase(Gamer g) {
		if (g.getVariable("purchases") == null) {
			g.setVariable("purchases", 1);
			return;
		}
		
		int purchases = (int) g.getVariable("purchases");
		g.setVariable("purchases", purchases + 1);
	}
	
	public void addItem(ShopItem item) {
		list.add(item);
	}
}
