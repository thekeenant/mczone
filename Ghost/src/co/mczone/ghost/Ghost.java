package co.mczone.ghost;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import co.mczone.api.ConfigAPI;
import co.mczone.ghost.api.Kit;
import co.mczone.ghost.api.Lobby;
import co.mczone.ghost.api.Arena;
import co.mczone.ghost.cmds.CmdBase;
import co.mczone.ghost.events.*;
import co.mczone.ghost.schedules.KitSchedule;
import co.mczone.util.Chat;
import co.mczone.util.ItemUtil;

public class Ghost extends JavaPlugin {
	@Getter static Ghost instance;
	@Getter static ConfigAPI conf;
	@Getter static Lobby lobby;
	
	@Getter static ConfigAPI kitConf;
	
	public void onEnable() {
		instance = this;
		conf = new ConfigAPI(this);
		kitConf = new ConfigAPI("kits.yml", this);
		
		lobby = new Lobby(conf.getLocation("lobby"));

		new ConnectEvents();
		new SignEvents();
		new GameEvents();
		
		new CmdBase();
		
		for (String name : kitConf.getKeys(false)) {
			ConfigurationSection kit = kitConf.getConfigurationSection(name);
			List<ItemStack> items = new ArrayList<ItemStack>();
			for (String raw : kit.getStringList("items"))
				items.add(ItemUtil.deserializeItem(raw));
			
			new Kit(name, items);
		}
		Chat.log("Loaded " + Kit.getList().size() + " kits");
		
		for (String worldName : conf.getConfigurationSection("matches").getKeys(false)) {
			String base = "matches." + worldName + ".";
			String title = conf.getString(base + "title", "NULL TITLE");
			int id = conf.getInt(base + "id", 0);
			
			Block sign = conf.getBlock(base + "sign");

			Location spawn = conf.getLocation(base + "spawn");
			Location red = conf.getLocation(base + "red");
			Location blue = conf.getLocation(base + "blue");
			
			new Arena(id, title, worldName, sign, spawn, red, blue);
		}
		
		new KitSchedule().runTaskTimerAsynchronously(this, 0, 45 * 20);
	}
}
