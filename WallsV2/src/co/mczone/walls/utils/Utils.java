package co.mczone.walls.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import co.mczone.walls.Config;

public class Utils {
	public static boolean isWall(Block b) {
		if (b.getType()==Material.SAND || b.getType()==Material.GRAVEL)
			return true;
		else
			return false;
	}
	
	public static class dropWalls implements Runnable {
		public dropWalls() { }
	    public void run() {
	    	Block xwall = new Location(Bukkit.getWorld("world"),-864,73,-164).getBlock();
			for (int x = xwall.getX(); x < -720; x++) {
				// Go up the y axis and set to air
				for(int y = 73; y < 100; y++){
					if (isWall(Config.getWorld().getBlockAt(x, y, -164)))
						Config.getWorld().getBlockAt(x, y, -164).setType(Material.AIR);
				}
			}
			
			Block xwall2 = new Location(Bukkit.getWorld("world"),-864,73,-182).getBlock();
			for (int x = xwall2.getX(); x < -720; x++) {
				// Go up the y axis and set to air
				for(int y = 73; y < 100; y++){
					if (isWall(Config.getWorld().getBlockAt(x, y, -182)))
						Config.getWorld().getBlockAt(x, y, -182).setType(Material.AIR);
				}
			}
			
			Block ywall = new Location(Bukkit.getWorld("world"),-785,73,-243).getBlock();
			for (int z = ywall.getZ(); z < -100; z++) {
				// Go up the y axis and set to air
				for(int y = 73; y < 100; y++){
					if (isWall(Config.getWorld().getBlockAt(-785, y, z)))
						Config.getWorld().getBlockAt(-785, y, z).setType(Material.AIR);
				}
			}
			
			Block ywall2 = new Location(Bukkit.getWorld("world"),-803,73,-243).getBlock();
			for (int z = ywall2.getZ(); z < -100; z++) {
				// Go up the y axis and set to air
				for(int y = 73; y < 100; y++){
					if (isWall(Config.getWorld().getBlockAt(-803, y, z)))
						Config.getWorld().getBlockAt(-803, y, z).setType(Material.AIR);
				}
			}
	    }
	}
	
	public static Player[] getPlayers() {
	    return Bukkit.getOnlinePlayers();
	}
}
