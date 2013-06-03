package co.mczone.api.modules;

import org.bukkit.Location;
import org.bukkit.World;

import lombok.Getter;

public class Coordinate {

	@Getter Double x;
	@Getter Double y;
	@Getter Double z;
	@Getter float yaw;
	@Getter float pitch;

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
	
	public Location getLocation(World world) {
		return new Location(world, x, y, z, yaw, pitch);
	}
	
}
