package co.mczone.lobby;

import org.bukkit.plugin.java.JavaPlugin;

import co.mczone.api.server.GameType;
import co.mczone.api.server.Hive;

import lombok.Getter;

public class Lobby extends JavaPlugin {
	
	@Getter static Lobby instance;
		
	public void onEnable() {
		instance = this;
		
		Hive.getInstance().setType(GameType.LOBBY);
		
		new QuerySchedule().runTaskTimerAsynchronously(this, 0, 20 * 5);
	}
	
}
