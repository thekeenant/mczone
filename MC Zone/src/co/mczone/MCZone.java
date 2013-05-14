package co.mczone;

import org.bukkit.plugin.java.JavaPlugin;

import co.mczone.api.ConfigAPI;
import co.mczone.api.database.MySQL;
import co.mczone.api.server.Hive;
import co.mczone.cmds.CmdBase;
import co.mczone.events.*;
import co.mczone.schedules.*;

import lombok.Getter;

public class MCZone extends JavaPlugin {
	@Getter static MCZone instance;
	
	public void onEnable() {
		instance = this;
		new ConfigAPI(this);
		new Hive(new MySQL("alpha.mczone.co", "3306", "mczone", "root", "johnt#@!"));

		new RankSchedule().runTaskTimerAsynchronously(this, 0, 20 * 60);
		new InfractionSchedule().runTaskTimerAsynchronously(this, 0, 20 * 30);
		new SignChangePacketSchedule().runTaskTimerAsynchronously(this, 0, 20 * 1);
		
		new ConnectEvents();
		new GeneralEvents();
		new DamageEvents();
		
		new CmdBase();
	}
}
