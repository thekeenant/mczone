package co.mczone.walls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import co.mczone.walls.utils.Chat;

@SuppressWarnings("unchecked")
public class Config {
	public static HashMap<String, Object> setting = new HashMap<String, Object>();
	public static FileConfiguration instance;

	public Config() {
		loadConfig();
	}

	public static void loadConfig() {
		Walls.instance.reloadConfig();
		Walls.instance.getConfig().options().copyDefaults(true);
		Walls.instance.saveConfig();
		instance = Walls.instance.getConfig();

		for (String s : instance.getKeys(false)) {
			setting.put(s, instance.get(s));
		}

		Chat.log(setting.size() + " configuration options loaded!");

		int c = 0;
		for (String name : instance.getConfigurationSection("kits").getKeys(false)) {
			List<String> rawList = instance.getStringList("kits." + name);
			List<ItemStack> items = new ArrayList<ItemStack>();
			for (String raw : rawList) {
				String[] info;
				String first;
				Integer amount;
				Integer id;
				String itemName;
				Short durability = 0;
				ItemStack i;
				String[] args = raw.split(",");
				if (args.length>1) {
					first = args[0];
					amount = Integer.parseInt(args[1]);
				}
				else {
					first = raw;
					amount = 1;
				}
				if (first.contains(":")) {
					info = first.split(":");
					itemName = info[0];
					if (Material.getMaterial(itemName.toUpperCase())==null) {
						Chat.log(Level.SEVERE, "Bad Item: " + first);
						continue;
					}
					id = Material.getMaterial(itemName.toUpperCase()).getId();
					durability = Short.parseShort(info[1]);
					i = new ItemStack(id, amount, durability);
				}
				else {

					if (Material.getMaterial(first.toUpperCase())==null) {
						Chat.log(Level.SEVERE, "Bad Item: " + first);
						continue;
					}
					id = Material.getMaterial(first.toUpperCase()).getId();
					i = new ItemStack(Material.getMaterial(first.toUpperCase()), amount);
				}
				if (args.length == 3) {
					String ench = args[2];
					if (Enchantment.getByName(ench.split(":")[0].toUpperCase())==null) {
						Chat.log(Level.SEVERE, "Bad Enchantment: " + first);
						continue;
					}
					Enchantment e = Enchantment.getByName(ench.split(":")[0].toUpperCase());
					int level = Integer.parseInt(ench.split(":")[1]);
					i.addUnsafeEnchantment(e, level);
				}
				items.add(i);
			}
			c += 1;
			new Kit(name, items);
		}
		Chat.log("Loaded " + c + " kits into game!");
	}

	public static void set(String s, Object o) {
		if (o instanceof Location) {
			Location l = (Location) o;
			setting.put(s, l);

			double x = l.getX();
			double y = l.getY();
			double z = l.getZ();
			double yaw = l.getYaw();
			double pitch = l.getPitch();

			instance.set("locations." + s + ".x", x);
			instance.set("locations." + s + ".y", y);
			instance.set("locations." + s + ".z", z);
			instance.set("locations." + s + ".yaw", yaw);
			instance.set("locations." + s + ".pitch", pitch);
		}
		else {
			setting.put(s, o);
		}

		Walls.instance.saveConfig();
	}

	public static Location getLocation(String s) {
		String base = "locations." + s + ".";
		double x = instance.getDouble(base + "x");
		double y = instance.getDouble(base + "y");
		double z = instance.getDouble(base + "z");
		float yaw = (float) instance.getDouble(base + "yaw");
		float pitch = (float) instance.getDouble(base + "pitch");

		Location r = new Location(getWorld(), x, y, z, yaw, pitch);
		return r;
	}

	public static Object get(String s) {
		return setting.get(s);
	}

	public static World getWorld() {
		return Bukkit.getWorld("world");
	}

	public static String getString(String s) {
		return (String) setting.get(s);
	}

	public static List<String> getList(String s) {
		return (List<String>) setting.get(s);
	}

	public static int getInt(String s) {
		return (Integer) setting.get(s);
	}
}
