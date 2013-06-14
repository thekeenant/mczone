package co.mczone.api.shop;

import co.mczone.api.players.Gamer;

import lombok.Getter;

public interface ShopItem {
	@Getter String title = "Item";
	@Getter String description = "1 item";
	@Getter int price = 0;

	public boolean buy(Gamer g, Object o);
}
