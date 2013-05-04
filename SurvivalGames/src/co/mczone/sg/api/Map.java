package co.mczone.sg.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

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
	
	public Map(String title, String worldName, List<Location> spawns, Location specSpawn) {
        list.add(this);
		this.id = list.size();
		this.title = title;
		this.worldName = worldName;
		this.spawns = spawns;
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
    			for (GamerSG g : GamerSG.getTributes()) {
    				g.setSpawnBlock(getNextSpawn());
    				g.getPlayer().teleport(g.getSpawnBlock());
    				g.setSpectator(false);
    				g.setMoveable(false);
    				g.clearInventory();
    				g.heal();
    			}
    			Map.nextSpawn = 0;
    			GamerSG.hideSpectators();
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
        Chat.log("Loaded a total of " + Map.getList().size() + " maps!");
    }
	
	public static int nextSpawn = 0;
	public Location getNextSpawn() {
		nextSpawn +=1;
		if (nextSpawn - 1 > spawns.size() - 1) {
			Chat.log("Error! Wrong spawn count!");
			return spawns.get(0);
		}
		return spawns.get(nextSpawn - 1);
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
}
