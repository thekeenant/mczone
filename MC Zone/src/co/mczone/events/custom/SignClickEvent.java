package co.mczone.events.custom;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SignClickEvent extends Event {
	public static HandlerList handlers = new HandlerList();
	@Getter @Setter Player player;
	@Getter @Setter Sign sign;
	@Getter @Setter boolean rightClick;
	
	public SignClickEvent(Player player, Sign sign, boolean rightClick) {
		this.player = player;
		this.sign = sign;
		this.rightClick = rightClick;
	}
	 
    public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
