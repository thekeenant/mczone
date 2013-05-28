package co.mczone.backend.api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import lombok.Getter;

public class StatusQuery {
	@Getter String hostname;
	@Getter int port;
	
	Socket socket = null;
	BufferedReader br = null;
	BufferedWriter bw = null;
	
	@Getter String result;
	
	public StatusQuery(String ip, int port) {
		this.hostname = ip;
		this.port = port;
		
		try {
			socket = new Socket(ip, port);
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void retrieve() throws IOException {

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println("get/all");
		
		String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        while ((line = br.readLine()) != null) {
        	result = line;
        	break;
        }
    
        out.close();
        br.close();
		socket.close();
	}
	
}
