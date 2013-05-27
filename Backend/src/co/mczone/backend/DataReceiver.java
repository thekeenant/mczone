package co.mczone.backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class DataReceiver implements Runnable {
	static int port = 950;
	
	public static class Handler implements Runnable {

		public void respond() throws Exception {
			BufferedReader br =	new BufferedReader(new InputStreamReader(sock.getInputStream()));
			BufferedWriter bw =	new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			String line = "";
			while ((line = br.readLine()) != null) {
				bw.write(line + "\n");
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
