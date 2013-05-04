package co.mczone.api;

import lombok.Getter;

public enum GameType {
	SURVIVAL_GAMES("Survival Games", "sg"),
	HUNGER_GAMES("Hunger Games", "hg"),
	NEBULA("Nebula MC", "pn"),
	NEXUS("Nexus MC", "nexus"),
	WALLS("The Walls", "walls"),
	SKYWARS("Sky Wars", "skywars");
	
	@Getter String title;
	@Getter String name;
	private GameType(String title, String name) {
		this.title = title;
		this.name = name;
	}
}
