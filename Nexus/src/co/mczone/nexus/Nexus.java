package co.mczone.nexus;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import co.mczone.api.ConfigAPI;
import co.mczone.api.database.MySQL;
import co.mczone.api.modules.Coordinate;
import co.mczone.api.players.Gamer;
import co.mczone.api.players.GamerRunnable;
import co.mczone.api.server.GameType;
import co.mczone.api.server.Hive;
import co.mczone.nexus.api.*;
import co.mczone.nexus.cmds.*;
import co.mczone.nexus.enums.GameState;
import co.mczone.nexus.enums.TeamColor;
import co.mczone.nexus.events.*;
import co.mczone.util.Chat;
import co.mczone.util.FileUtil;

public class Nexus {
	
	@Getter static Nexus instance;
	@Getter static Main plugin;
	@Getter static ConfigAPI config;
	@Getter static ConfigAPI kitConfig;
	@Getter static MySQL database;
	@Getter static MatchStats matchStats;
	
	@Getter static Rotary rotary;
	
	public Nexus(Main main) {
		instance = this;
		plugin = main;
		
		kitConfig = new ConfigAPI("kits.yml", plugin);
		config = new ConfigAPI(plugin);
		
		database = Hive.getInstance().getDatabase();
		rotary = new Rotary();
		matchStats = new MatchStats();

		new ConnectEvents();
		new GameEvents();
		new GeneralEvents();
		new KitEvents();
		
		Hive.getInstance().setType(GameType.NEXUS);
		
		loadMaps();
		
		for (String name : config.getStringList("rotation")) {
			Map map = Map.get(name);
			
			if (map != null)
				rotary.getMaps().add(map);
		}
		
		loadKits();
		rotary.start();

		Hive.getInstance().registerCommand(Nexus.getPlugin(), "match", new MatchCmd());
		Hive.getInstance().registerCommand(Nexus.getPlugin(), "join", new JoinCmd());
		Hive.getInstance().registerCommand(Nexus.getPlugin(), "leave", new LeaveCmd());
		Hive.getInstance().registerCommand(Nexus.getPlugin(), "kit", new KitCmd());
		
		Gamer.addFunction("give-kit", new GamerRunnable() {
			@Override
			public void run() {
				Kit kit = null;
				if (gamer.getVariable("kit") == null) {
					kit = Kit.get("warrior");
					gamer.setVariable("kit", kit);
				}
				else
					kit = (Kit) gamer.getVariable("kit");
				

				kit.giveKit(gamer);
				
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
				
				gamer.giveItem(new ItemStack(Material.GOLDEN_APPLE, 3));
				gamer.giveItem(new ItemStack(Material.COOKED_BEEF, 16));
				
			}
		});
		
		Gamer.addFunction("give-book", new GamerRunnable() {
			@Override
			public void run() {
				ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
		        BookMeta data = (BookMeta) book.getItemMeta();
		        data.setAuthor(Chat.colors("&7&oMC Zone"));
		        data.setTitle(Chat.colors("&2&lNeuxs MC"));
		        String page1 = "     ";
		        String page2 = "";
		        
		        // Page 1
		        page1 += "  &3&lNexus MC\n";
		        page1 += "    &6&oHow To Guide\n";
		        page1 += "\n";
		        page1 += "&0Nexus MC is a server made for Minecraft PVP gamers. Fight on various maps with any number of teams in team deathmatch. Learn how to join and how to play on the next page.";
		        page1 += "\n\n&7www.mczone.co";
		        // Page 2
		        page2 += "       &4&oCommands\n";
		        page2 += "\n";
		        page2 += "&6/join &0Joins the game\n";
		        page2 += "&6/leave &0Leave the game\n";
		        page2 += "&6/kit &0View available kits\n";
		        page2 += "&6/kit <kit> &0Choose a kit\n";
		        page2 += "&6/help &0Show commands";
		        
		        data.setPages(Chat.colors(page1), Chat.colors(page2));
		        book.setItemMeta(data);
		        gamer.giveItem(book);
			}
		});
		
        new BukkitRunnable() {
			@Override
			public void run() {
				ResultSet r = database.query("SELECT * FROM nexus_donations");
				//Kit.votes.clear();
				Kit.getDonations().clear();
				int donations = 0;
				try {
					while (r.next()) {
						String username = r.getString("username").toLowerCase();
						String kit = r.getString("kit").toLowerCase();
						boolean free = (r.getInt("free")==1) ? true : false;
						if (free) {
							//Kit.votes.put(username, Kit.findByName(kit));
						}
						else {
							donations += 1;
							if (!Kit.getDonations().containsKey(username))
								Kit.getDonations().put(username, new ArrayList<Kit>());
							Kit.getDonations().get(username).add(Kit.get(kit));
						}
					}
				} catch (SQLException e) {
					Chat.log("DB Error: Couldn't load donations/votes!");
				}
				Chat.log("Loaded " + donations + " kit donations!");
			}
        }.runTaskTimerAsynchronously(getPlugin(), 80, 60 * 20);
		
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
			
			new Map(title, creators, version, world, duration, config, spawn, teams);
		}
	}
	
	public void loadKits() {
        int c = 0;
        Kit.getList().clear();
        if (kitConfig.getConfigurationSection("kits") != null) {
            for (String name : kitConfig.getConfigurationSection("kits").getKeys(false)) {
                List<String> rawList = kitConfig.getStringList("kits." + name);
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
                    if (args.length >= 3) {
                    	List<String> enchantments = new ArrayList<String>();
                    	for (int ec = 0; ec <= args.length -1; ec++) {
                    		if (ec >= 2)
                    			enchantments.add(args[ec]);
                    	}
                    	for (String ench : enchantments) {
                            if (Enchantment.getByName(ench.split(":")[0].toUpperCase())==null) {
                                Chat.log(Level.SEVERE, "Bad Enchantment: " + first);
                                continue;
                            }
                            Enchantment e = Enchantment.getByName(ench.split(":")[0].toUpperCase());
                            int level = Integer.parseInt(ench.split(":")[1]);
                            i.addUnsafeEnchantment(e, level);
                    	}
                    }
                    items.add(i);
                }
                c += 1;
                new Kit(name, items);
            }
            Chat.log("Loaded " + c + " kits into game!");
        }
	}
	
	public void onDisable() {
		for (World w : Bukkit.getWorlds())
			Bukkit.unloadWorld(w, false);
	}
	
	public void updateHidden() {
		try {
			for (Team team : rotary.getCurrentMap().getTeams()) {
				
				for (Gamer g : team.getMembers()) {
					
					if (g.getPlayer() == null || g.getPlayer().isOnline() == false)
						continue;
					
					for (Gamer t : Gamer.getList()) {
						
						if (t.getPlayer() == null || t.getPlayer().isOnline() == false)
							continue;
						
						if (Nexus.getRotary().getState() != GameState.PLAYING) {
							g.getPlayer().showPlayer(t.getPlayer());
							continue;
						}
						
						if (t.getVariable("spectator") == null)
							g.getPlayer().showPlayer(t.getPlayer());
						else
							g.getPlayer().hidePlayer(t.getPlayer());
					}
					
				}
			
			}
		}
		catch (ConcurrentModificationException e) {
			return;
		}
	}
}
