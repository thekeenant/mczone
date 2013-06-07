package co.mczone.nexus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import lombok.Getter;
import co.mczone.api.ConfigAPI;
import co.mczone.api.database.MySQL;
import co.mczone.api.modules.Coordinate;
import co.mczone.api.players.Gamer;
import co.mczone.api.players.GamerRunnable;
import co.mczone.api.server.Hive;
import co.mczone.nexus.api.Kit;
import co.mczone.nexus.api.Map;
import co.mczone.nexus.api.Rotary;
import co.mczone.nexus.api.Team;
import co.mczone.nexus.cmds.JoinCmd;
import co.mczone.nexus.enums.TeamColor;
import co.mczone.nexus.events.ConnectEvents;
import co.mczone.nexus.events.GameEvents;
import co.mczone.nexus.events.GeneralEvents;
import co.mczone.util.FileUtil;

public class Nexus {
	
	@Getter static Nexus instance;
	@Getter static Main plugin;
	@Getter static ConfigAPI config;
	@Getter static MySQL database;
	
	@Getter static Rotary rotary;
	
	public Nexus(Main main) {
		instance = this;
		plugin = main;
		config = new ConfigAPI(plugin);
		
		database = Hive.getInstance().getDatabase();
		rotary = new Rotary();

		new ConnectEvents();
		new GameEvents();
		new GeneralEvents();
		
		loadMaps();
		rotary.start();
		
		Hive.getInstance().registerCommand(Nexus.getPlugin(), "join", new JoinCmd());
		
		Gamer.addFunction("give-kit", new GamerRunnable() {
			@Override
			public void run() {
				if (gamer.getVariable("kit") == null)
					return;
				
				Kit kit = (Kit) gamer.getVariable("kit");
				
				if (kit.getName().equals("spy")) {
					gamer.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, -1, 1));
				}
				else if (kit.getName().equals("scout")) {
					gamer.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, -1, 1));
					gamer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, -1, 2));
				}
				else if (kit.getName().equals("tank")) {
					gamer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, -1, 2));
				}
			}
		});
	}
	
	public void loadMaps() {
		List<File> files = FileUtil.getFiles(new File(getPlugin().getDataFolder(), "maps"));
		for (File f : files) {
			ConfigAPI config = new ConfigAPI(f, plugin);
			String world = config.getString("info.world");
			String title = config.getString("info.title");
			int duration  = config.getInt("info.duration", 600);
			String version = config.getString("info.version");
			List<String> creators = config.getList("info.creators");
			
			Coordinate spawn = config.getCoordinate("spawn");
			
			List<Team> teams = new ArrayList<Team>();
			for (String key : config.getConfigurationSection("teams").getKeys(false)) {
				Team team = new Team(
						config.getString("teams." + key + ".title"),
						TeamColor.valueOf(config.getString("teams." + key + ".color").toUpperCase()),
						config.getCoordinate("teams." + key + ".spawn")
						);
				teams.add(team);
			}
			
			Map map = new Map(title, creators, version, world, duration, config, spawn, teams);
			rotary.getMaps().add(map);
		}
	}
	
	public void onDisable() {
		
	}
	
	public void updateHidden() {
		for (Team team : rotary.getCurrentMap().getTeams())
			
			for (Gamer g : team.getMembers())
				
				for (Gamer t : Gamer.getList())
					
					if (g.getVariable("spectator") == null)
						g.getPlayer().showPlayer(t.getPlayer());
					else
						g.getPlayer().hidePlayer(t.getPlayer());
	}
}
