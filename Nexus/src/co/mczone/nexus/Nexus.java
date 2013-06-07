package co.mczone.nexus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import co.mczone.api.ConfigAPI;
import co.mczone.api.database.MySQL;
import co.mczone.api.modules.Coordinate;
import co.mczone.api.server.Hive;
import co.mczone.nexus.api.Map;
import co.mczone.nexus.api.Rotary;
import co.mczone.nexus.api.Team;
import co.mczone.nexus.cmds.JoinCmd;
import co.mczone.nexus.enums.TeamColor;
import co.mczone.nexus.events.ConnectEvents;
import co.mczone.nexus.events.GeneralEvents;
import co.mczone.util.FileUtil;

public class Nexus {
	
	@Getter static Nexus instance;
	@Getter static Main plugin;
	@Getter static ConfigAPI config;
	@Getter static MySQL database;
	
	@Getter static Rotary rotary;
	
	public Nexus(Main main) {
		instance = this;
		plugin = main;
		config = new ConfigAPI(plugin);
		
		
		database = Hive.getInstance().getDatabase();
		rotary = new Rotary();

		new ConnectEvents();
		new GeneralEvents();
		
		loadMaps();
		rotary.start();
		
		Hive.getInstance().registerCommand(Nexus.getPlugin(), "join", new JoinCmd());
	}
	
	public void loadMaps() {
		List<File> files = FileUtil.getFiles(new File(getPlugin().getDataFolder(), "maps"));
		for (File f : files) {
			ConfigAPI config = new ConfigAPI(f, plugin);
			String world = config.getString("info.world");
			String title = config.getString("info.title");
			int duration  = config.getInt("info.duration", 600);
			String version = config.getString("info.version");
			List<String> creators = config.getList("info.creators");
			
			Coordinate spawn = config.getCoordinate("spawn");
			
			List<Team> teams = new ArrayList<Team>();
			for (String key : config.getConfigurationSection("teams").getKeys(false)) {
				Team team = new Team(
						config.getString("teams." + key + ".title"),
						TeamColor.valueOf(config.getString("teams." + key + ".color").toUpperCase()),
						config.getCoordinate("teams." + key + ".spawn")
						);
				teams.add(team);
			}
			
			Map map = new Map(title, creators, version, world, duration, config, spawn, teams);
			rotary.getMaps().add(map);
		}
	}
	
	public void onDisable() {
		
	}
}
