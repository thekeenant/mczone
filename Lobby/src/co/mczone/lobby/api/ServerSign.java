package co.mczone.lobby.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import co.mczone.util.Chat;

import lombok.Getter;
import lombok.Setter;

public class ServerSign {
	@Getter static List<ServerSign> list = new ArrayList<ServerSign>();
	@Getter @Setter OpenServer current;
	@Getter @Setter Block block;
	@Getter @Setter boolean used;
	
	public ServerSign(Block b) {
		list.add(this);
		block = b;
	}
	
	public void update() {
		if (block.getType() != Material.WALL_SIGN) {
			if (current != null)
				Chat.log(block.getType().name());
			else
				Chat.log(block.getType().name());
			return;
		}
		
		if (current == null || used == false) {
			Sign s = (Sign) block.getState();
        	s.setLine(1, "");
        	s.setLine(2, Chat.colors("&0&lNo Servers"));
        	s.setLine(3, "");
			s.update(true);
			return;
		}
		
		Query q = new Query(current.getAddress().split(":")[0], Integer.valueOf(current.getAddress().split(":")[1]));
		q.fetchData();
		

		Sign s = (Sign) block.getState();
		s.setLine(1, ChatColor.GREEN + "Server ID: " + current.getAddress().substring(current.getAddress().length() - 2, current.getAddress().length()));
		s.setLine(2, q.getPlayersOnline() + "/" + q.getMaxPlayers());
		s.update(true);
	}
}
