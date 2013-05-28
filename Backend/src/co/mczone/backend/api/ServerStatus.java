package co.mczone.backend.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class ServerStatus {
	// HashMap<Server Name, List of servers>
	@Getter static HashMap<String, List<ServerStatus>> servers = new HashMap<String, List<ServerStatus>>();
	@Getter String name;
	@Getter int id;
	@Getter @Setter int playerCount;
	@Getter @Setter Status status;
	@Getter @Setter Date date;
	
	public ServerStatus(String name, int id, int playerCount, int status) {
		this.name = name;
		this.id = id;
		this.playerCount = playerCount;
		this.status = Status.getByValue(status);
		this.date = new Date();
		set(name, id, this);
	}
	
	public void set(String name, int id, ServerStatus status) {
		if (servers.get(name) == null)
			servers.put(name, new ArrayList<ServerStatus>());
		
		for (ServerStatus server : servers.get(name)) {
			if (server.getId() == id) {
				server.setPlayerCount(status.getPlayerCount());
				server.setStatus(status.getStatus());
				server.setDate(new Date());
				return;
			}
		}
		
		servers.get(name).add(status);
	}
	
	public static List<ServerStatus> retrieve(String name) {
		return servers.get(name);
	}
	
}
