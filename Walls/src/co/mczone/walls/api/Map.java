package co.mczone.walls.api;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.Location;

public class Map {
	@Getter static List<Map> list = new ArrayList<Map>();
	@Getter @Setter String name;
	@Getter @Setter String worldName;
	@Getter @Setter String title;
	@Getter @Setter Location spawn;
	@Getter @Setter Location redSpawn;
	@Getter @Setter Location blueSpawn;
	
	public Map(String name, String title, String world, Location spawn, Location redSpawn, Location blueSpawn) {
		this.name = name;
		this.title = title;
		this.worldName = world;
		this.spawn = spawn;
		this.redSpawn = redSpawn;
		this.blueSpawn = blueSpawn;
		list.add(this);
	}
	

	public static Map get(String s) {
		for (Map m : Map.getList())
			if (s.equalsIgnoreCase(m.getName()))
				return m;
			else if (s.equalsIgnoreCase(m.getWorldName()))
				return m;
		return null;
	}
}
