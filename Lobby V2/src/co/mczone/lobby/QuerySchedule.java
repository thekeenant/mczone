package co.mczone.lobby;

import java.io.IOException;

import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.MCZone;
import co.mczone.util.Chat;

public class QuerySchedule extends BukkitRunnable {

	@Override
	public void run() {
		String query = query();
		
		Chat.log(query);
	}
	
	public String query() {
		StatusQuery q = new StatusQuery(
				MCZone.getInstance().getConfig().getString("backend.hostname", "198.24.165.66"), 
				MCZone.getInstance().getConfig().getInt("backend.port", 850)
				);
		
		try {
			q.retrieve();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return q.getResult();
	}
	
	
}
