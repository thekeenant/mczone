package co.mczone.nexus;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	public void onEnable() {
		new Nexus(this);
	}
	
	public void onDisable() {
		Nexus.getInstance().onDisable();
	}
	
}
