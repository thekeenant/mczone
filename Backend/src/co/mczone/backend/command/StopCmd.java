package co.mczone.backend.command;

import co.mczone.backend.api.Chat;
import co.mczone.backend.api.Command;

public class StopCmd implements Command {

	@Override
	public void execute(String[] arr) {
		Chat.log("Shutting down...");
		System.exit(1);
		return;
	}

}
