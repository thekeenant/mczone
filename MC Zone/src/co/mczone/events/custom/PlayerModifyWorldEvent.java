package co.mczone.events.custom;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerModifyWorldEvent extends Event {
	public static HandlerList handlers = new HandlerList();
	@Getter Player player;
	@Getter ModifyWorldAction action;
	@Getter Event event;
	@Getter @Setter boolean cancelled = false;
	
	public PlayerModifyWorldEvent(Player player, ModifyWorldAction action, Event event, boolean cancelled) {
		this.player = player;
		this.action = action;
		this.event = event;
		this.cancelled = cancelled;
	}
	 
    public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
