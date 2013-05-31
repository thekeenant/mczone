package co.mczone.lobby;

import java.io.IOException;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.MCZone;
import co.mczone.lobby.api.Portal;
import co.mczone.lobby.api.ServerStatus;
import co.mczone.lobby.api.Status;

public class QuerySchedule extends BukkitRunnable {

	@Override
	public void run() {
		ServerStatus.fromArray(query().split("/"));
		
		for (Portal p : Portal.getList()) {
			boolean change = false, found = false;
			List<ServerStatus> servers = ServerStatus.get(p.getName());
			for (ServerStatus s : servers) {
				if (p.getCurrent().getId() == s.getId()) {
					found = true;
					if (s.getStatus() == Status.CLOSED)
						change = true;
					break;
				}
			}
			if (!found)
				change = true;
			
			if (change) {
				if (servers.size() > 0) {
					p.setCurrent(servers.get(0));
				}
			}
		}
		
		for (Sign )
	}
	
	public String query() {
		StatusQuery q = new StatusQuery(
				MCZone.getInstance().getConfig().getString("backend.hostname", "198.24.165.66"), 
				MCZone.getInstance().getConfig().getInt("backend.port", 850)
				);
		
		try {
			q.retrieve();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return q.getResult();
	}
	
	
}
