package co.mczone.events.custom;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SignClickEvent extends Event {
	public static HandlerList handlers = new HandlerList();
	@Getter @Setter Player player;
	@Getter @Setter Block block;
	@Getter @Setter Sign sign;
	@Getter @Setter boolean rightClick;
	
	public SignClickEvent(Player player, Sign sign, Block b, boolean rightClick) {
		this.player = player;
		this.sign = sign;
		this.block = b;
		this.rightClick = rightClick;
	}
	 
    public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
