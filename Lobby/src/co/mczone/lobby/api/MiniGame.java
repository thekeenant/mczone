package co.mczone.lobby.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

import co.mczone.lobby.Lobby;
import co.mczone.util.Chat;

import lombok.Getter;
import lombok.Setter;

public class MiniGame {
	@Getter @Setter String title;
	@Getter	static List<MiniGame> list = new ArrayList<MiniGame>();
	@Getter List<ServerRange> servers = new ArrayList<ServerRange>();
	@Getter List<ServerSign> signs = new ArrayList<ServerSign>();

	@Getter List<OpenServer> open = new ArrayList<OpenServer>();

	@Getter @Setter boolean searching;
	@Getter @Setter Date lastSearch;
	
	public MiniGame(List<ServerRange> servers, List<Block> signs) {
		list.add(this);
		this.servers = servers;
		for (Block b : signs)
			this.signs.add(new ServerSign(b));
	}

	public static void load(ConfigurationSection servers) {
		for (String name : servers.getKeys(false)) {
			List<ServerRange> ranges = new ArrayList<ServerRange>();
			for (String raw : servers.getStringList(name + ".servers")) {
				String ip = raw.split(":")[0];
				
				String ports = raw.split(":")[1];
				int first = Integer.parseInt(ports.split(",")[0]);
				int second = Integer.parseInt(ports.split(",")[1]);
				
				ranges.add(new ServerRange(ip, first, second));
			}
			
			List<Block> blocks = new ArrayList<Block>();
			for (String raw : servers.getStringList(name + ".signs")) {
				blocks.add(Lobby.getConfigAPI().parseBlock(raw));
			}
			
			MiniGame g = new MiniGame(ranges, blocks);
			g.setTitle(servers.getString(name + ".title"));
		}
		Chat.log("Loaded " + MiniGame.getList().size() + " minigames!");
	}
}
