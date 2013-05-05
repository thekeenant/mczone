package co.mczone.ghost;

import lombok.Getter;

import org.bukkit.plugin.java.JavaPlugin;

import co.mczone.api.ConfigAPI;

public class Ghost extends JavaPlugin {
	@Getter static Ghost instance;
	@Getter static ConfigAPI conf;
	
	public void onEnable() {
		instance = this;
		
		
	}
}
