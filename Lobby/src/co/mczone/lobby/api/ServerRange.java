package co.mczone.lobby.api;

import lombok.Getter;

public class ServerRange {
	@Getter String ip;
	@Getter int minPort;
	@Getter int maxPort;
	
	public ServerRange(String ip, int minPort, int maxPort) {
		this.ip = ip;
		this.minPort = minPort;
		this.maxPort = maxPort;
	}
	
	
}
