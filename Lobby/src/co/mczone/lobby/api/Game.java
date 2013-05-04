package co.mczone.lobby.api;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;

import co.mczone.util.Chat;

public class Game {
	@Getter static List<Game> list = new ArrayList<Game>();
	@Getter String address;
	@Getter String title;
	@Getter String line_1;
	@Getter String line_2;
	@Getter List<Block> signs = new ArrayList<Block>();
	
	@Getter @Setter int maxPlayers;
	@Getter @Setter int players;
	
	public Game(String title, String line_1, String line_2, String address) {
		this.title = title;
		this.line_1 = line_1;
		this.line_2 = line_2;
		this.address = address;
		list.add(this);
	}
	
	public static void load(ConfigurationSection servers) {
		for (String name : servers.getKeys(false)) {
			String title = servers.getString(name + ".title");
			String line_1 = servers.getString(name + ".line_1");
			String line_2 = servers.getString(name + ".line_2");
			String address = servers.getString(name + ".address");
			
			new Game(Chat.colors(title), line_1, line_2, address);
		}
		Chat.log("Loaded " + Game.getList().size() + " games!");
	}
	
	public void update() {
		Query q = new Query(getAddress().split(":")[0], Integer.parseInt(getAddress().split(":")[1]));
		q.fetchData();
		
		setPlayers(q.getPlayersOnline());
		setMaxPlayers(q.getMaxPlayers());
		
		for (Block b : signs) {
			Sign s = (Sign) b.getState();
			s.setLine(2, players + "/" + maxPlayers + " online");
			s.setLine(3, ChatColor.GREEN + q.getMotd());
			s.update(true);
		}
	}
}
