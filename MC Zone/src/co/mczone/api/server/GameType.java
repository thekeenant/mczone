package co.mczone.api.server;

import lombok.Getter;

public enum GameType {
	SURVIVAL_GAMES("Survival Games", "sg", "198.100.115.186"),
	
	SKYWARS("Sky Wars", "skywars", "198.100.101.219"),
	HUNGER_GAMES("Hunger Games", "hg", "198.100.101.219"),
	
	NEBULA("Nebula MC", "pn", "198.27.75.226"),
	
	WALLS("The Walls", "walls", "198.100.97.106"),
	
	LOBBY("Lobby", "lobby", ""),
	
	NEXUS("Nexus MC", "nexus", "64.237.39.226"),
	GHOST("Ghost Squadron", "ghost", "64.237.39.226");
	
	@Getter String title;
	@Getter String name;
	@Getter String ip;
	private GameType(String title, String name, String ip) {
		this.title = title;
		this.name = name;
		this.ip = ip;
	}
	
	public static GameType fromName(String name) {
		for (GameType g : values())
			if (g.getName().equalsIgnoreCase(name))
				return g;
		return null;
	}
}
