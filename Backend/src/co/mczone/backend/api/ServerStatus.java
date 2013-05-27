package co.mczone.backend.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class ServerStatus {
	// HashMap<Server Name, List of servers>
	@Getter static HashMap<String, List<ServerStatus>> servers = new HashMap<String, List<ServerStatus>>();
	@Getter int id;
	@Getter @Setter int playerCount;
	@Getter @Setter Status status;
	
	public ServerStatus(String name, int id, int playerCount, int status) {
		this.id = id;
		this.playerCount = playerCount;
		this.status = Status.getByValue(status);
		set(name, id, this);
	}
	
	public static void addStatus(String name, ServerStatus status) {
		if (servers.get(name) == null)
			servers.put(name, new ArrayList<ServerStatus>());
		
		servers.get(name).add(status);
	}
	
	public void set(String name, int id, ServerStatus status) {
		if (servers.get(name) == null)
			servers.put(name, new ArrayList<ServerStatus>());
		
		for (ServerStatus server : servers.get(name)) {
			if (server.getId() == id) {
				server.setPlayerCount(status.getPlayerCount());
				server.setStatus(status.getStatus());
				return;
			}
		}
		
		servers.get(name).add(status);
		
	}
	
	public static List<ServerStatus> retrieve(String name) {
		return servers.get(name);
	}
	
}
