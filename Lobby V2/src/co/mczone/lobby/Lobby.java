package co.mczone.lobby;

import org.bukkit.plugin.java.JavaPlugin;

import co.mczone.api.ConfigAPI;
import co.mczone.api.server.GameType;
import co.mczone.api.server.Hive;
import co.mczone.lobby.api.Portal;

import lombok.Getter;

public class Lobby extends JavaPlugin {

	@Getter static Lobby instance;
	@Getter static ConfigAPI conf;
		
	public void onEnable() {
		instance = this;
		
		Hive.getInstance().setType(GameType.LOBBY);
		
		conf = new ConfigAPI(this);
		
		for (String name : conf.getConfigurationSection("portals").getKeys(false)) {
			new Portal(name, conf.getBlock("portals." + name + ".sign"));
		}
		

		new QuerySchedule().runTaskTimerAsynchronously(this, 0, 20 * 5);
	}
	
}
