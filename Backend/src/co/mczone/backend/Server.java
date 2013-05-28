package co.mczone.backend;

import java.util.List;

import lombok.Getter;

import co.mczone.backend.api.Chat;
import co.mczone.backend.api.ServerStatus;

public class Server {
	@Getter MinecraftReceiever minecraftReceiver;
	@Getter DataHandler dataHandler;

	public Server() {
		this.minecraftReceiver = new MinecraftReceiever();
		this.dataHandler = new DataHandler();
	}
	
	public void start() {
		Chat.log("Starting receiever");
		new Thread(minecraftReceiver).start();
		new Thread(dataHandler).start();
	}
	
	public int getTotalPlayerCount() {
		int count = 0;
		for (List<ServerStatus> list : ServerStatus.getServers().values())
			for (ServerStatus server : list)
				count += server.getPlayerCount();
		
		return count;
	}
	
}
