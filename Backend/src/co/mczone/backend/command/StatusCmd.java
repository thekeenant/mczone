package co.mczone.backend.command;

import java.util.List;

import co.mczone.backend.api.Chat;
import co.mczone.backend.api.Command;
import co.mczone.backend.api.ServerStatus;

public class StatusCmd implements Command {

	@Override
	public void execute(String[] arr) {
		if (arr.length != 1) {
			Chat.log("Usage: /status server:id");
			return;
		}

		String name = arr[0].split(":")[0];
		int id = Integer.parseInt(arr[0].split(":")[1]);
		
		List<ServerStatus> servers = ServerStatus.retrieve(name);
		if (servers == null) {
			Chat.log("Couldn't find a server with the name of " + name);
			return;
		}
		
		for (ServerStatus server : servers) {
			if (server.getId() == id) {
				Chat.log(name + ":" + id + " is " + server.getStatus().name() + " with " + server.getPlayerCount() + " players");
				return;
			}
		}
		
		Chat.log("Couldn't find a server with the ID of " + id);	
		return;
	}

}
