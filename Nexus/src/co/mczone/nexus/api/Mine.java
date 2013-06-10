package co.mczone.nexus.api;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.block.Block;

import co.mczone.api.players.Gamer;

public class Mine {

	@Getter static List<Mine> list = new ArrayList<Mine>();
	
	@Getter Gamer gamer;
	@Getter Block block;
	@Getter @Setter boolean ignited = false;
	
	public Mine(Gamer g, Block block) {
		this.gamer = g;
		this.block = block;
		
		list.add(this);
	}
	
}
