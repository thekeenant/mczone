package co.mczone.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;

import co.mczone.backend.api.*;
import co.mczone.backend.command.*;
import lombok.Getter;

public class Main {
	@Getter	static Server server = null;
	@Getter static HashMap<String, Command> commands = new HashMap<String, Command>();

	public static void main(String[] args) {
		server = new Server();
		server.start();

		commands.put("players", new PlayersCmd());
		commands.put("stop", new StopCmd());
		commands.put("status", new StatusCmd());
		commands.put("test", new TestCmd());

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			String line = "";
			try {
				line = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			line = line.toLowerCase();
			boolean found = false;
			for (String s : commands.keySet()) {
				if (line.startsWith(s)) {
					String[] arr = {};
					if (line.split(" ").length > 1)
						arr = Arrays.copyOfRange(line.split(" "), 1, line.split(" ").length);
					commands.get(s).execute(arr);
					found = true;
					break;
				}
			}
			
			if (!found)
				Chat.log("Command not found.");
		}
	}
}
