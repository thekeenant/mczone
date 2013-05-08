package co.mczone.ghost.schedules;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.api.server.Hive;
import co.mczone.ghost.api.Kit;
import co.mczone.util.Chat;

public class KitSchedule extends BukkitRunnable {

	@Override
	public void run() {
		ResultSet r = Hive.getInstance().getDatabase().query("SELECT * FROM ghost_donations");
		try {
			while (r.next()) {
				String username = r.getString("username").toLowerCase();
				Kit kit = Kit.get(r.getString("kit"));
				if (kit == null)
					continue;
				
				if (!Kit.getPurchases().containsKey(username))
					Kit.getPurchases().put(username, new ArrayList<Kit>());
				
				Kit.getPurchases().get(username).add(kit);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Chat.log("Loaded " + Kit.getPurchases().size() + " donation kits!");
	}
}
