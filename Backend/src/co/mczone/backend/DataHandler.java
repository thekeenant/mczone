package co.mczone.backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import co.mczone.backend.api.Chat;
import co.mczone.backend.api.ServerStatus;

public class DataHandler implements Runnable {
	static int port = 850;
	
	public static class Handler implements Runnable {

		public void respond() throws Exception {
			BufferedReader br =	new BufferedReader(new InputStreamReader(sock.getInputStream()));
			BufferedWriter bw =	new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			String line = "";
			while ((line = br.readLine()) != null) {
				Chat.log("Handling request: " + line);
				
				// name:id,players,status
				
				if (line.startsWith("get/")) {
					String get = line.replace("get/", "");
					if (get.equalsIgnoreCase("all")) {
						String result = "";
						for (List<ServerStatus> list : ServerStatus.getServers().values())
							for (ServerStatus server : list) {
								result += server.getName() + ":" + server.getId() + ",";
								result += server.getPlayerCount() + ",";
								result += server.getStatus().getValue() + "/";
							}
						
						bw.write(result);
					}
					else {
						if (get.split(":").length != 2) {
							bw.write("error");
						}
						else {
							String name = get.split(":")[0];
							int id = Integer.parseInt(get.split(":")[1]);
							String result = "";
							List<ServerStatus> list = ServerStatus.retrieve(name);
							if (list != null) {
								for (ServerStatus server : list) {
									if (server.getId() != id)
										continue;
									
									result += server.getName() + ":" + server.getId() + ",";
									result += server.getPlayerCount() + ",";
									result += server.getStatus().getValue();
								}
								
								bw.write(result);
							}
						}
					}
				}
				
				bw.newLine();
				bw.flush();
			}

			bw.close();
			br.close();
			sock.close();
		}
		
		Socket sock = null;
		public Handler(Socket socket) {
			sock = socket;
		}
		
		@Override
		public void run() {
			try {
				respond();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void run() {
		ServerSocket listenSock = null; //the listening server socket
		Socket sock = null;	//the socket that will actually be used for communication

		try {
			listenSock = new ServerSocket(port);
			while (true) {

				sock = listenSock.accept();
				new Thread(new Handler(sock)).start();
				
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
