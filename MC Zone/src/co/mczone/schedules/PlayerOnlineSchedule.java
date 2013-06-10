package co.mczone.schedules;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.api.server.Hive;

public class PlayerOnlineSchedule extends BukkitRunnable {	
	@Override
	public void run() {
		if (Bukkit.getOnlinePlayers().length == 0 || Hive.getInstance().getType() == null)
			return;
		
		String query = "UPDATE players SET created=created,updated=now(),server='" + Hive.getInstance().getType().getTitle() + "' WHERE ";
		for (Player p : Bukkit.getOnlinePlayers())
			query += "username = '" + p.getName() + "' OR ";
		
		query = query.substring(0, query.length() - 4);
		Hive.getInstance().getDatabase().update(query);
	}
}
