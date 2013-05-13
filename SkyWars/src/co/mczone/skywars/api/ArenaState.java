package co.mczone.skywars.api;

import lombok.Getter;

import org.bukkit.ChatColor;

public enum ArenaState {
	LOADING(ChatColor.GOLD),
	WAITING(ChatColor.GREEN),
	STARTED(ChatColor.RED);
	
	@Getter ChatColor color;
	private ArenaState(ChatColor color) {
		this.color = color;
	}
}
