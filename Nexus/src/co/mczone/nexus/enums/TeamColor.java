package co.mczone.nexus.enums;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public enum TeamColor {
	
	RED("Red"),
	LIGHT_RED("LightRed"),
	BLUE("Blue"),
	GREEN("Green"),
	LIME("Lime"),
	YELLOW("Yellow"),
	ORANGE("Orange"),
	PURPLE("Purple"),
	CYAN("Cyan"),
	PINK("Pink"),
	GRAY("Gray"),
	NAVY("Navy"),
	WHITE("White"),
	BLACK("Black"),
	LIGHT_GRAY("LightGray"),
	SPECTATOR("Spectator"),
	INVALID("Invalid");
	
	String team;
	TeamColor(String s) {
		team = s;
	}
	
	public String getTeamString() {
		return team;
	}
	
	public static TeamColor getTeam(String teamString) {
		for (TeamColor team : TeamColor.values()) {
			if (team.getTeamString().equalsIgnoreCase(teamString)) return team;
		}
		
		return INVALID;
	}
	
	public ChatColor getChatColor() {
		if (this.equals(RED)) {
			return ChatColor.DARK_RED;
		} else if (this.equals(LIGHT_RED)) {
			return ChatColor.RED;
		} else if (this.equals(BLUE)) {
			return ChatColor.BLUE;
		} else if (this.equals(GREEN)) {
			return ChatColor.DARK_GREEN;
		} else if (this.equals(LIME)) {
			return ChatColor.GREEN;
		} else if (this.equals(YELLOW)) {
			return ChatColor.YELLOW;
		} else if (this.equals(ORANGE)) {
			return ChatColor.GOLD;
		} else if (this.equals(PURPLE)) {
			return ChatColor.DARK_PURPLE;
		} else if (this.equals(CYAN)) {
			return ChatColor.DARK_AQUA;
		} else if (this.equals(PINK)) {
			return ChatColor.LIGHT_PURPLE;
		} else if (this.equals(GRAY)) {
			return ChatColor.DARK_GRAY;
		} else if (this.equals(NAVY)) {
			return ChatColor.DARK_BLUE;
		} else if (this.equals(WHITE)) {
			return ChatColor.WHITE;
		} else if (this.equals(BLACK)) {
			return ChatColor.BLACK;
		} else if (this.equals(LIGHT_GRAY)) {
			return ChatColor.GRAY;
		} else {
			return ChatColor.AQUA;
		}
	}
	
	public static Color getColor(TeamColor team) {
		if (team.equals(RED)) {
			return Color.fromRGB(255, 0, 0);
		} else if (team.equals(LIGHT_RED)) {
			return Color.fromRGB(255, 104, 104);
		} else if (team.equals(BLUE)) {
			return Color.fromRGB(65, 135, 255);
		} else if (team.equals(GREEN)) {
			return Color.fromRGB(0, 255, 0);
		} else if (team.equals(LIME)) {
			return Color.fromRGB(145, 255, 0);
		} else if (team.equals(YELLOW)) {
			return Color.fromRGB(247, 255, 0);
		} else if (team.equals(ORANGE)) {
			return Color.fromRGB(255, 128, 0);
		} else if (team.equals(PURPLE)) {
			return Color.fromRGB(213, 0, 255);
		} else if (team.equals(CYAN)) {
			return Color.fromRGB(0, 128, 255);
		} else if (team.equals(PINK)) {
			return Color.fromRGB(255, 0, 162);
		} else if (team.equals(GRAY)) {
			return Color.fromRGB(75, 75, 75);
		} else if (team.equals(NAVY)) {
			return Color.fromRGB(0, 14, 203);
		} else if (team.equals(WHITE)) {
			return Color.fromRGB(255, 255, 255);
		} else if (team.equals(BLACK)) {
			return Color.fromRGB(0, 0, 0);
		} else if (team.equals(LIGHT_GRAY)) {
			return Color.fromRGB(200, 200, 200);
		} else {
			return Color.fromRGB(247, 255, 0);
		}
	}
	
}
