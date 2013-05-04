package co.mczone.parkour;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class Course {
	@Getter static List<Course> list = new ArrayList<Course>();
	@Getter String name;
	@Getter String title;
	@Getter Location start;
	@Getter Block button;
	@Getter Block end;
	@Getter @Setter String nextCourse;
	
	public Course(String name, String title, Location start, Block button, Block end) {
		this.name = name;
		this.title = title;
		this.start = start;
		this.button = button;
		this.end = end;
		list.add(this);
	}

	public Course(String name, String title) {
		this.name = name;
		this.title = title;
		list.add(this);
	}
	
	public static Course get(String name) {
		for (Course c : list)
			if (c.getName().equalsIgnoreCase(name))
				return c;
		return null;
	}
}
