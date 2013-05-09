package co.mczone.events.custom;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerKilledEvent extends Event {
	public static HandlerList handlers = new HandlerList();
	@Getter @Setter boolean playerKill = false;
	@Getter @Setter Player player;
	@Getter @Setter Entity entity;
	@Getter @Setter Player target;
	@Getter @Setter String deathMessage;
	
	public PlayerKilledEvent(Player player, Player target, String deathMessage) {
		this.player = player;
		this.target = target;
		this.playerKill = true;
		this.deathMessage = deathMessage;
	}
	
	public PlayerKilledEvent(Entity entity, Player target, String deathMessage) {
		this.entity = entity;
		this.target = target;
		this.deathMessage = deathMessage;
	}
	
	public PlayerKilledEvent(Player target, String deathMessage) {
		this.entity = null;
		this.target = target;
		this.deathMessage = deathMessage;
	}
	 
    public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
