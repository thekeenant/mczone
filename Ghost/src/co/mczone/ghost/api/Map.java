package co.mczone.ghost.api;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class Map {
	
	@Getter static List<Map> list = new ArrayList<Map>();
	
	@Getter List<Integer> matches = new ArrayList<Integer>();
	
	@Getter @Setter String folderName;
	@Getter @Setter String mapName;
	
	public Map(String folderName, String mapName) {
		setFolderName(folderName);
		setMapName(mapName);
		list.add(this);
	}
	
}
