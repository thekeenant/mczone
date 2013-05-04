package co.mczone.parkour;

import java.io.File;
import java.io.IOException;
import java.util.List;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigAPI {
	@Getter FileConfiguration config;
    
    public ConfigAPI(FileConfiguration config) {
        this.config = config;
    }

    public ConfigAPI() {
    	Parkour plugin = Parkour.getInstance();
        plugin.reloadConfig();
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
        config = plugin.getConfig();
    }
    
    public boolean save(File file) {
        try {
            config.save(file);
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }
    
    public boolean contains(String s) {
    	return (config.contains(s));
    }
    
    public void remove(String s) {
    	config.set(s, null);
    }
    
	public void set(String s, Object o) {
		if (o instanceof Location) {
			Location l = (Location) o;

	        String world = l.getWorld().getName();
			double x = l.getX();
			double y = l.getY();
			double z = l.getZ();
			double yaw = l.getYaw();
			double pitch = l.getPitch();

            config.set(s + ".world", world);
            config.set(s + ".x", x);
			config.set(s + ".y", y);
			config.set(s + ".z", z);
			config.set(s + ".yaw", yaw);
			config.set(s + ".pitch", pitch);
		}
		else {
			config.set(s, o);
		}
	}

	public Location getLocation(String s) {
		String base = s + ".";
        String world = config.getString(base + "world");
        double x = config.getDouble(base + "x");
		double y = config.getDouble(base + "y");
		double z = config.getDouble(base + "z");
		float yaw = (float) config.getDouble(base + "yaw");
		float pitch = (float) config.getDouble(base + "pitch");

		Location r = new Location(Bukkit.getServer().getWorld(world), x, y, z, yaw, pitch);
		return r;
	}

    public String getString(String s) {
        return config.getString(s);
    }

    public boolean getBoolean(String s) {
        return config.getBoolean(s);
    }

    public boolean getBoolean(String s, boolean def) {
        return config.getBoolean(s, def);
    }

    public String getString(String s, String def) {
        if (config.getString(s) != null)
            return config.getString(s);
        config.set(s, def);
        Parkour.getInstance().saveConfig();
        return def;
    }

	public List<String> getList(String s) {
        return config.getStringList(s);
	}

    public int getInt(String s) {
        return config.getInt(s);
    }

    public int getInt(String s, int i) {
        if (config.get(s) != null)
            return config.getInt(s);
        config.set(s, i);
        Parkour.getInstance().saveConfig();
        return i;
    }
}
