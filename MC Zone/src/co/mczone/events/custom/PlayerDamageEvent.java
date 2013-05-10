package co.mczone.events.custom;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PlayerDamageEvent extends Event {
	public static HandlerList handlers = new HandlerList();
	@Getter Player player;
	@Getter Player target;
	@Getter DamageCause cause;
	@Getter @Setter boolean cancelled;
	@Getter boolean damageByEntity;
	
	public PlayerDamageEvent(Player player, Player target, DamageCause cause) {
		this.damageByEntity = true;
		this.player = player;
		this.target = target;
		this.cause = cause;
		this.cancelled = false;
	}
	
	public PlayerDamageEvent(Player target, DamageCause cause) {
		this.damageByEntity = false;
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
