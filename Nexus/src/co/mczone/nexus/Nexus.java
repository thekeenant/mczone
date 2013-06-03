package co.mczone.nexus;

import lombok.Getter;
import co.mczone.api.ConfigAPI;
import co.mczone.api.database.MySQL;
import co.mczone.api.server.Hive;

public class Nexus {
	
	@Getter static Nexus instance;
	@Getter static Main plugin;
	@Getter static ConfigAPI config;
	@Getter static MySQL database;
	
	public Nexus(Main main) {
		instance = this;
		plugin = main;
		config = new ConfigAPI(plugin);
		database = Hive.getInstance().getDatabase();
	}
	
	public void onDisable() {
		
	}
}
