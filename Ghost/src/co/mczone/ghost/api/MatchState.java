package co.mczone.ghost.api;

import lombok.Getter;

import org.bukkit.ChatColor;

public enum MatchState {
	LOADING(ChatColor.GOLD),
	WAITING(ChatColor.GREEN),
	STARTED(ChatColor.RED);
	
	@Getter ChatColor color;
	private MatchState(ChatColor color) {
		this.color = color;
	}
}
