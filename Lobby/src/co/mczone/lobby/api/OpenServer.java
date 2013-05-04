package co.mczone.lobby.api;

import org.bukkit.block.Block;

import lombok.Getter;
import lombok.Setter;

public class OpenServer {
	@Getter @Setter String address;
	@Getter @Setter String motd;
	@Getter @Setter int players = 0;
	@Getter @Setter int maxPlayers = 0;
	@Getter @Setter Block block;
	
	public OpenServer(String ip, String motd, int players, int max) {
		this.address = ip;
		this.motd = motd;
		this.players = players;
		this.maxPlayers = max;
	}
}
