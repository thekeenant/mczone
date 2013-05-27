package co.mczone.backend;

import java.util.List;

import co.mczone.backend.api.ServerStatus;

public class Server {

	public Server() {
		
	}
	
	public int getTotalPlayerCount() {
		int count = 0;
		for (List<ServerStatus> list : ServerStatus.getServers().values())
			for (ServerStatus server : list)
				count += server.getPlayerCount();
		
		return count;
	}
	
}
