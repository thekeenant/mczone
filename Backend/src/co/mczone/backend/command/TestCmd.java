package co.mczone.backend.command;

import java.io.IOException;

import co.mczone.backend.api.Chat;
import co.mczone.backend.api.Command;
import co.mczone.backend.api.StatusQuery;

public class TestCmd implements Command {

	@Override
	public void execute(String[] arr) {
		StatusQuery q = new StatusQuery("localhost", 850);
		try {
			q.retrieve();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Chat.log(q.getResult() + " result");
		return;
	}

}
