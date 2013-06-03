package co.mczone.ghost.api;

import lombok.Getter;

import org.bukkit.ChatColor;

public enum TeamColor {
	RED("Red", ChatColor.RED),
	SPEC("spec", ChatColor.GRAY),
	BLUE("Blue", ChatColor.BLUE);
	
	@Getter String name;
	@Getter ChatColor color;
	private TeamColor(String name, ChatColor color) {
		this.name = name;
		this.color = color;
	}
}
