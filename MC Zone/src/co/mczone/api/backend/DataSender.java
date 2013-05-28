package co.mczone.api.backend;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import co.mczone.MCZone;
import co.mczone.util.Chat;

public class DataSender {
	Socket socket = null;
	PrintWriter out = null;
	BufferedWriter bw =	null;
	
	public DataSender() {
	}
	
	public void send(String name, int id, int players, int status) throws IOException {
		try {
			socket = new Socket(
					MCZone.getInstance().getConfig().getString("backend.hostname", "198.24.165.66"), 
					MCZone.getInstance().getConfig().getInt("backend.port", 950)
					);
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			Chat.log("Unable to send server status data!");
			return;
		}
		bw.write(name + ":" + id + "," + players + "," + status);
		bw.newLine();
		bw.flush();
	}

}
