package co.mczone.ghost;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import org.bukkit.plugin.java.JavaPlugin;

import co.mczone.api.ConfigAPI;
import co.mczone.ghost.api.Match;
import co.mczone.ghost.events.ConnectEvents;

public class Ghost extends JavaPlugin {
	@Getter static Ghost instance;
	@Getter static ConfigAPI conf;
	@Getter static List<Match> matches = new ArrayList<Match>();
	
	
	public void onEnable() {
		instance = this;
		
		new ConnectEvents();
		
		matches.add(new Match(1));
	}
}
