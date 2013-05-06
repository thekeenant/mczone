package co.mczone.ghost;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import co.mczone.api.ConfigAPI;
import co.mczone.ghost.api.Match;
import co.mczone.ghost.events.ConnectEvents;
import co.mczone.ghost.events.PlayerEvents;

public class Ghost extends JavaPlugin {
	@Getter static Ghost instance;
	@Getter static ConfigAPI conf;
	@Getter static World lobby;
	
	public void onEnable() {
		instance = this;
		conf = new ConfigAPI(this);

		new ConnectEvents();
		new PlayerEvents();
		
		for (String worldName : conf.getConfigurationSection("matches").getKeys(false)) {
			String base = "matches." + worldName + ".";
			String title = conf.getString(base + "title", "NULL TITLE");
			int id = conf.getInt(base + "id", 0);
			
			Block sign = conf.getBlock(base + "sign");

			Location red = conf.getLocation(base + "red");
			Location blue = conf.getLocation(base + "blue");
			
			new Match(id, title, worldName, sign, red, blue);
		}
		
		lobby = Bukkit.getWorld(Ghost.getConf().getString("lobby-world", "world"));
	}
}
