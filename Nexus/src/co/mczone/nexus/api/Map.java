package co.mczone.nexus.api;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import co.mczone.api.ConfigAPI;
import co.mczone.api.modules.Coordinate;
import co.mczone.nexus.enums.TeamColor;
import co.mczone.util.Chat;

import lombok.Getter;

public class Map {
	
	@Getter String title;
	@Getter String worldName;
	@Getter Coordinate spawn;
	@Getter List<Team> teams;
	@Getter ConfigAPI config;
	
	@Getter int timeLimit;
	
	public Map(String title, String worldName, int timeLimit, ConfigAPI config, Coordinate spawn, List<Team> teams) {
		this.title = title;
		this.worldName = worldName;
		this.spawn = spawn;
		this.timeLimit = timeLimit;
		this.config = config;
		this.teams = teams;
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
	
	public void loadMatch() {
		Chat.log(Prefix.LOG_WORLDS + "Generating world: " + worldName + "...");
		
		WorldCreator wc = new WorldCreator(worldName);
		wc.createWorld();
		
		for (Team team : teams) {
			team.getMembers().clear();
		}
	}
	
	public void unloadMatch() {
		Chat.log(Prefix.LOG_WORLDS + "Unloading world: " + worldName + "...");
		
		boolean unloaded = Bukkit.unloadWorld(worldName, false);
		
		if (unloaded)
			Chat.log(Prefix.LOG_WORLDS + "Successfully unloaded world!");
		else
			Chat.log(Prefix.LOG_WORLDS + "Failed to unload world");
	}
	
}
