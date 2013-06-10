package co.mczone.api.modules;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import lombok.Getter;
import lombok.Setter;

public class Coordinate {

	@Getter Double x;
	@Getter Double y;
	@Getter Double z;
	@Getter float yaw;
	@Getter float pitch;
	
	// Just a custom value
	@Getter @Setter Object custom;

	public Coordinate(Double x, Double y, Double z, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	public Coordinate(Double x, Double y, Double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Coordinate(int x, int y, int z) {
		this.x = (double) x;
		this.y = (double) y;
		this.z = (double) z;
	}

	public Location getLocation(World world) {
		return new Location(world, x, y, z, yaw, pitch);
	}
	
	public Block getBlock(World world) {
		return world.getBlockAt((int) (0 + x), (int) (0 + y), (int) (0 + z));
	}
	
}
