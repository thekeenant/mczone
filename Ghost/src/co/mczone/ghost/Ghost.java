package co.mczone.ghost;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import co.mczone.api.ConfigAPI;
import co.mczone.ghost.api.Match;
import co.mczone.ghost.events.ConnectEvents;

public class Ghost extends JavaPlugin {
	@Getter static Ghost instance;
	@Getter static ConfigAPI conf;
	@Getter static List<Match> matches = new ArrayList<Match>();
	@Getter static World lobby;
	
	public void onEnable() {
		instance = this;
		conf = new ConfigAPI(this);
		
		new ConnectEvents();
		
		for (String worldName : conf.getConfigurationSection("matches").getKeys(false)) {
			String title = conf.getString(worldName + ".title", "NULL TITLE");
			int id = conf.getInt(worldName + ".id", 0);
			
			Block sign = conf.getBlock(worldName + ".sign");
			
			new Match(id, title, worldName, sign);
		}
		
		lobby = Bukkit.getWorld(Ghost.getConf().getString("lobby-world", "world"));
	}
}
