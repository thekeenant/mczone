package co.mczone;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.api.ConfigAPI;
import co.mczone.api.database.MySQL;
import co.mczone.api.infractions.Infraction;
import co.mczone.api.players.Rank;
import co.mczone.api.players.SignChangePacket;
import co.mczone.api.server.Hive;
import co.mczone.cmds.CmdBase;
import co.mczone.events.*;
import co.mczone.schedules.*;
import co.mczone.util.Chat;

import lombok.Getter;

public class MCZone extends JavaPlugin {
	@Getter static MCZone instance;
	@Getter List<BukkitRunnable> schedules = new ArrayList<BukkitRunnable>();
	
	public void onEnable() {
		instance = this;
		new ConfigAPI(this);
		new Hive(new MySQL("alpha.mczone.co", "3306", "mczone", "root", "johnt#@!"));

		BukkitRunnable schedule = null;
		
		schedule = new RankSchedule();
		schedule.runTaskTimerAsynchronously(this, 0, 20 * 60);
		schedules.add(schedule);
		
		schedule = new InfractionSchedule();
		schedule.runTaskTimerAsynchronously(this, 0, 20 * 30);
		schedules.add(schedule);
		
		schedule = new SignChangePacketSchedule();
		schedule.runTaskTimerAsynchronously(this, 0, 20 * 1);
		schedules.add(schedule);
		
		new ConnectEvents();
		new GeneralEvents();
		new DamageEvents();
		
		new CmdBase();
	}
	
	public void onDisable() {
		Chat.log("Cancelling tasks...");
		for (BukkitRunnable runnable : schedules) {
			Chat.log("Cancelling: #" + runnable.getTaskId() + " runnable");
			runnable.cancel();
		}
		Chat.log("Clearing ranks, infractions, and sign change packets...");
		Rank.getRanks().clear();
		Infraction.getList().clear();
		Chat.log("MC Zone HIVE is disabled");
		SignChangePacket.getList().clear();
	}
}
