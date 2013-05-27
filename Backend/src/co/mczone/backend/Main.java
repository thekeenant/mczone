package co.mczone.backend;

import lombok.Getter;

public class Main {
	@Getter static Server server = null;
	
	
	public static void main(String[] args) {
		new Server();
	}

}
