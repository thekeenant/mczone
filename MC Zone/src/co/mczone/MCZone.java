package co.mczone;

import org.bukkit.plugin.java.JavaPlugin;

import co.mczone.api.ConfigAPI;
import co.mczone.api.GameType;
import co.mczone.api.database.MySQL;
import co.mczone.api.server.Hive;
import co.mczone.schedules.*;

import lombok.Getter;

public class MCZone extends JavaPlugin {
	@Getter static MCZone instance;
	
	public void onEnable() {
		instance = this;
		new ConfigAPI(this);
		new Hive(new MySQL(null, null, null, null, null));
		
		new RankSchedule().runTaskTimerAsynchronously(this, 0, 20);
	}
	
	public void setGameType(GameType game) {
		Hive.getInstance().setType(game);
	}
}
