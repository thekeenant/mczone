package co.mczone.lobby.api;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import co.mczone.util.Chat;

public class Portal {
	@Getter static List<Portal> list = new ArrayList<Portal>();

	@Getter String name;
	
	@Getter Sign sign;
	
	@Getter ServerStatus current;
	
	public Portal(String name, Block sign) {
		this.name = name;
		if (sign.getType() != Material.WALL_SIGN)
			this.sign = (Sign) sign.getState();
		
		list.add(this);
	}
	
	public void setCurrent(ServerStatus server) {
		this.current = server;
		updateSign();
	}
	
	public void updateSign() {
		if (sign == null)
			return;
		
		String line1 = Chat.colors("&l[" + current.getName().toUpperCase() + "]");
		String line2 = Chat.colors("&aServer ID: &0" + current.getId());
		String line3 = Chat.colors(current.getPlayerCount() + "/" + current.getMaxPlayers() + " players");
		
		if (current.getStatus() == Status.CLOSED) {
			line2 = Chat.colors("&4Server ID: &0" + current.getId());
		}
		
		sign.setLine(0, line1);
		sign.setLine(1, line2);
		sign.setLine(2, line3);
		sign.update(true);
	}
	
}
