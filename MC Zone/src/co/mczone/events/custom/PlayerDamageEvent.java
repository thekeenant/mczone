package co.mczone.events.custom;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PlayerDamageEvent extends Event {
	public static HandlerList handlers = new HandlerList();
	@Getter @Setter Player player;
	@Getter @Setter Player target;
	@Getter @Setter DamageCause cause;
	@Getter @Setter boolean cancelled;
	
	public PlayerDamageEvent(Player player, Player target, DamageCause cause) {
		this.player = player;
		this.target = target;
		this.cause = cause;
		this.cancelled = false;
	}
	 
    public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
