package co.mczone.skywars;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import co.mczone.api.ConfigAPI;
import co.mczone.api.players.Gamer;
import co.mczone.api.players.GamerRunnable;
import co.mczone.api.server.GameType;
import co.mczone.api.server.Hive;
import co.mczone.skywars.api.Arena;
import co.mczone.skywars.api.Kit;
import co.mczone.skywars.api.Lobby;
import co.mczone.skywars.cmds.CmdBase;
import co.mczone.skywars.events.*;
import co.mczone.util.Chat;
import co.mczone.util.ItemUtil;

public class SkyWars extends JavaPlugin {
	@Getter static SkyWars instance;
	@Getter static ConfigAPI conf;
	@Getter static Lobby lobby;
	
	@Getter static ConfigAPI kitConf;
	
	public void onEnable() {
		Hive.getInstance().setType(GameType.SKYWARS);
		
		instance = this;
		conf = new ConfigAPI(this);
		kitConf = new ConfigAPI("kits.yml", this);
		
		lobby = new Lobby(conf.getLocation("lobby"));

		new GeneralEvents();
		new ConnectEvents();
		new SignEvents();
		new GameEvents();
		
		new CmdBase();
		
		for (String name : kitConf.getKeys(false)) {
			ConfigurationSection kit = kitConf.getConfigurationSection(name);
			List<ItemStack> items = new ArrayList<ItemStack>();
			
			for (String raw : kit.getStringList("items"))
				items.add(ItemUtil.deserializeItem(raw));
			
			if (!kit.contains("effects"))
				new Kit(name, items);
			else {
				List<PotionEffect> effects = new ArrayList<PotionEffect>();
				for (String raw : kit.getStringList("effects"))
					effects.add(ItemUtil.deserializePotionEffect(raw));
				
				new Kit(name, items, effects);
			}
		}
		Chat.log("Loaded " + Kit.getList().size() + " kits");
		
		for (String worldName : conf.getConfigurationSection("arenas").getKeys(false)) {
			String base = "arenas." + worldName + ".";
			String title = conf.getString(base + "title", "NULL TITLE");
			int id = conf.getInt(base + "id", 0);
			
			Block sign = conf.getBlock(base + "sign");
			
			new WorldCreator(worldName).createWorld();
			
			Location spawn = Bukkit.getWorld(worldName).getSpawnLocation();
			if (conf.contains(base + "spawn"))
				spawn = conf.getLocation(base + "spawn");
			
			Location red = conf.getLocation(base + "red");
			Location blue = conf.getLocation(base + "blue");
			Location green = conf.getLocation(base + "green");
			
			new Arena(conf.getConfigurationSection("arenas." + worldName), id, title, worldName, sign, spawn, red, blue, green);
		}
		
		Gamer.addFunction("load-kits", new GamerRunnable() {
			@Override
			public void run() {
				List<Kit> kits = new ArrayList<Kit>();
				ResultSet r = Hive.getInstance().getDatabase().query("SELECT * FROM skywars_donations WHERE username='" + gamer.getName()  + "'");
				try {
					while (r.next()) {
						Kit kit = Kit.get(r.getString("kit"));
						if (kit == null)
							continue;

						if (r.getInt("free") == 1)
							gamer.setVariable("skywars-vote", kit);
						else						
							kits.add(kit);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				gamer.setVariable("skywars-kits", kits);
			}
		});
	}
}
