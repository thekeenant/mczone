package co.mczone.backend.command;

import co.mczone.backend.Main;
import co.mczone.backend.api.Chat;
import co.mczone.backend.api.Command;

public class PlayersCmd implements Command {

	@Override
	public void execute(String[] arr) {
		Chat.log("Current player count: " + Main.getServer().getTotalPlayerCount());
		return;
	}

}
