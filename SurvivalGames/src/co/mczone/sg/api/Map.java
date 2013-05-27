package co.mczone.sg.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.api.players.Gamer;
import co.mczone.sg.SurvivalGames;
import co.mczone.sg.util.Files;
import co.mczone.util.Chat;

import lombok.Getter;
import lombok.Setter;

public class Map {
	@Getter static File directory = new File(SurvivalGames.getInstance().getDataFolder(), "maps");
	@Getter static List<Map> list = new ArrayList<Map>();
	@Getter @Setter static Map current;
	@Getter int id;
	@Getter String title;
	@Getter String worldName;
	@Getter List<Location> spawns;
	@Getter Location specSpawn;
	@Getter @Setter List<String> votes = new ArrayList<String>();
	@Getter YamlConfiguration config;
	
	@Getter HashMap<Location, Player> places = new HashMap<Location, Player>();
	
	public Map(String title, String worldName, List<Location> spawns, Location specSpawn) {
        list.add(this);
		this.id = list.size();
		this.title = title;
		this.worldName = worldName;
		this.spawns = spawns;
		for (Location l : spawns)
			places.put(l, null);
		
		this.specSpawn = specSpawn;
        File file = new File(SurvivalGames.getInstance().getDataFolder() + File.separator + "maps", worldName + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
	}
	
	public void loadWorld() {
		new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.createWorld(new WorldCreator(getWorldName()));
				getWorld().setTime(6000 - (20 * 20));
				for (Location l : getSpawns())
					l.setWorld(getWorld());
				getSpecSpawn().setWorld(getWorld());
    			for (Gamer g : Game.getTributes()) {
    				places.put(getNextSpawn(), g.getPlayer());
    				g.setVariable("spawn-block", getNextSpawn());
    				g.getPlayer().teleport((Location) g.getVariable("spawn-block"));
    				g.setInvisible(false);
    				g.setVariable("moveable", false);
    				g.getPlayer().setHealth(20);
    				g.clearInventory();
    			}
			}
		}.runTask(SurvivalGames.getInstance());
	}
	
	public static Map getTopVoted() {
		Map result = Map.getList().get(0);
		for (Map map : Map.getList()) {
			if (map.getVotes().size() > result.getVotes().size())
				result = map;
		}
		return result;
	}

	public static Map getByID(int id) {
		for (Map m : getList())
			if (m.getId() == id)
				return m;
		return null;
	}
	
	public static Map getByName(String name) {
		for (Map m : getList()) {
			if (m.getWorldName() == name)
				return m;
		}
		return null;
	}	
	
	public static Map getByTitle(String name) {
		for (Map m : getList()) {
			if (m.getTitle().equalsIgnoreCase(name))
				return m;
		}
		return null;
	}
	
	public static void load() {
        list.clear();
        File maps = new File(SurvivalGames.getInstance().getDataFolder(), "maps");
        for (File yml : Files.getFiles(maps)) {
            if (!yml.getName().endsWith(".yml"))
                continue;
            
            ConfigAPI api = new ConfigAPI(YamlConfiguration.loadConfiguration(yml));
            FileConfiguration config = api.getConfig();
            String title = config.getString("info.title");
            String worldName = config.getString("info.worldName");
            if (title == null || worldName == null) {
                Chat.log(Level.SEVERE, "Error with map file: " + yml.getName() + " (no title and/or world name)");
                continue;
            }
            List<Location> spawns = new ArrayList<Location>();
            for (String s : config.getConfigurationSection("spawns").getKeys(false)) {
            	if (api.getString("spawns." + s + ".team") != null && api.getString("spawns." + s + ".team").equals("spec"))
            		continue;
            	Location l = api.getLocation("spawns." + s);
            		spawns.add(l);
            }
            Location specSpawn = api.getLocation("spawns.spec");
            
            new Map(title, worldName, spawns, specSpawn);
        }
        
		Comparator<Map> comp = new Comparator<Map>() {
			public int compare(Map m1, Map m2) {
				return m1.getTitle().compareTo(m2.getTitle());
			}
		};
		
		Collections.sort(list, comp);
        
        Chat.log("Loaded a total of " + Map.getList().size() + " maps!");
    }
	
	public Location getNextSpawn() {
		Location l = null;
		for (Entry<Location, Player> e : Map.getCurrent().getPlaces().entrySet()) {
			if (e.getValue() == null) {
				l = e.getKey();
			}
		}
		return l;
	}
	
	public World getWorld() {
		return Bukkit.getWorld(worldName);
	}

	public boolean reload() {
		ConfigAPI api = new ConfigAPI(getConfig());
        FileConfiguration config = api.getConfig();
        String title = config.getString("info.title");
        String worldName = config.getString("info.worldName");
        
        List<Location> spawns = new ArrayList<Location>();
        for (String s : config.getConfigurationSection("spawns").getKeys(false)) {
        	Location l = api.getLocation("spawns." + s);
        		spawns.add(l);
        }
        Location specSpawn = api.getLocation("spawns.spec");
        
        this.title = title;
        this.worldName = worldName;
        this.specSpawn = specSpawn;
        this.spawns = spawns;
		return true;
	}

	public void unload() {
		Bukkit.unloadWorld(worldName, false);
	}

	public void addVote(Player p) {
    	if (getVotes().contains(p.getName()))
    		getVotes().remove(p.getName());
    	
    	for (Map m : Map.getList()) {
    		if (m.getVotes().contains(p.getName()))
    			m.getVotes().remove(p.getName());
    	}
    	
    	getVotes().add(p.getName());
	}
}
