package co.mczone.schedules;

import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.api.server.Hive;

public class DataSenderSchedule extends BukkitRunnable {	
	@Override
	public void run() {
		Hive.getInstance().updateStatus();
	}
}
