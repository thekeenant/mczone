package co.mczone.nexus.api;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import co.mczone.api.ConfigAPI;
import co.mczone.nexus.enums.TeamColor;

import lombok.Getter;

public class Map {

	@Getter String worldName;
	
	@Getter List<TeamColor> teamColors;
	@Getter List<Team> teams;
	
	@Getter ConfigAPI config;
	
	
	public Map(String worldName, ConfigAPI config, List<TeamColor> teamColors) {
		this.worldName = worldName;
		this.config = config;
		this.teamColors = teamColors;
	}
	
	public Team getTeam(TeamColor color) {
		for (Team team : teams)
			if (team.getColor() == color)
				return team;
		return null;
	}
	
	public World getWorld() {
		return Bukkit.getWorld(worldName);
	}
	
	public void load() {
		WorldCreator wc = new WorldCreator(worldName);
		wc.createWorld();
		
		for (Team team : teams) {
			team.getMembers().clear();
		}
	}
	
}
