package co.mczone.ghost.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import lombok.Getter;
import lombok.Setter;

public class Lobby {
	@Getter @Setter World world;
	@Getter @Setter Location spawn;
	@Getter List<Block> kitSigns = new ArrayList<Block>();
	
	public Lobby(Location spawn) {
		this.world = spawn.getWorld();
		this.spawn = spawn;
		
		spawn.getWorld().setAutoSave(false);
	}
	
	public Sign getKitSign(Sign s) {
		for (Block b : kitSigns) 
			if (b.getX() == s.getX() && b.getY() == s.getY() && b.getZ() == s.getZ())
				if (b.getType() == Material.SIGN || b.getType() == Material.WALL_SIGN)
					return (Sign) b.getState();
		return null;
	}
}
