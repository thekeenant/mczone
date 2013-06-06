package co.mczone.nexus;

import java.io.File;
import java.util.List;

import lombok.Getter;
import co.mczone.api.ConfigAPI;
import co.mczone.api.database.MySQL;
import co.mczone.api.modules.Coordinate;
import co.mczone.api.server.Hive;
import co.mczone.nexus.api.Rotary;
import co.mczone.nexus.api.Team;
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
		
		loadMaps();
		
		database = Hive.getInstance().getDatabase();
		rotary = new Rotary();
	}
	
	public void loadMaps() {
		List<File> files = FileUtil.getFiles(new File(this.getPlugin().getDataFolder(), "maps"));
		for (File f : files) {
			ConfigAPI config = new ConfigAPI(f, plugin);
			String world = config.getString("info.world");
			String title = config.getString("info.title");
			String version = config.getString("info.version");
			List<String> creators = config.getList("info.creators");
			String s = "String title, String worldName, int timeLimit, ConfigAPI config, Coordinate spawn, List<Team> teams";
		}
	}
	
	public void onDisable() {
		
	}
}
