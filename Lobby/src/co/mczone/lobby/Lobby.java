package co.mczone.lobby;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;

import net.minecraft.server.v1_5_R3.Packet41MobEffect;
import net.minecraft.server.v1_5_R3.Packet42RemoveMobEffect;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.api.server.GameType;
import co.mczone.api.server.Hive;
import co.mczone.lobby.api.*;
import co.mczone.lobby.events.*;
import co.mczone.lobby.util.Random;
import co.mczone.util.Chat;

public class Lobby extends JavaPlugin implements Listener {
	@Getter static Lobby instance;
	@Getter static ConfigAPI configAPI;
	@Getter	public static HashMap<String, Date> connectionTimes = new HashMap<String, Date>();
	
	public void onLoad() {
		rmdir(new File("void", "players"));
	}
	
	public void onEnable() {
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		Hive.getInstance().setType(GameType.LOBBY);
		instance = this;
		configAPI = new ConfigAPI();

		// Load configuration
		MiniGame.load(configAPI.getConfig().getConfigurationSection("minigames"));
		Game.load(configAPI.getConfig().getConfigurationSection("games"));

		// Game Icons!
		new GameIcon(Material.DIAMOND_SWORD, "&5Nebula MC", new Location(Bukkit.getWorld("void"), 41.5, 69.5, -1.5, 270, 0), true);
		new GameIcon(Material.BRICK, "&2The Walls", new Location(Bukkit.getWorld("void"), -25.5, 69.5, -1.5, 90, 0));
		new GameIcon(Material.FIRE, "&4Survival Games", new Location(Bukkit.getWorld("void"), -25.5, 69.2, 45.5, 90, 0));
		new GameIcon(Material.GOLDEN_APPLE, "&2Hunger Games", new Location(Bukkit.getWorld("void"), 41.5, 69.2, 45.5, 270, 0));
		
		new GameIcon(Material.IRON_SWORD, "&9Nexus MC", new Location(Bukkit.getWorld("void"), 8.5, 69.5, 78.5, 0, 0));
		new GameIcon(Material.BOW, "&bSky Wars", new Location(Bukkit.getWorld("void"), 8.5, 69.5, -34, 180, 0), true);

		// Events!
		new CompassEvents();
		new GameEvents();
		new Events();

		// Timers!
		new PlayerScheduler().runTaskTimerAsynchronously(this, 0, 20);
		new MiniGameScheduler().runTaskTimerAsynchronously(this, 0, 20);
		new GameScheduler().runTaskTimerAsynchronously(this, 0, 20);
		
		// Potion Effects!
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.getLocation().getX() < 150)
						//p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, 2), true);
						addPotionEffect(p, new PotionEffect(PotionEffectType.SPEED, 2147483647, 2));
					else
						//p.removePotionEffect(PotionEffectType.SPEED);
						removePotionEffect(p, PotionEffectType.SPEED);
				}
				Bukkit.getWorld("void").setTime(6000);
			}
			
		}.runTaskTimerAsynchronously(this, 0, 40);
		
		// Server Shutdown!
		new BukkitRunnable() {
			int seconds = 0;
			int timeLimit = 60 * (56 + Random.between(1, 8));
			@Override
			public void run() {
				seconds += 1;
				if (seconds == timeLimit - 300 || seconds == timeLimit - 120) {
					Chat.server("&4[Lobby] &cLobby restarting in " + Chat.time(timeLimit - seconds) + "!");
				}
				if (seconds >= timeLimit - 10 && seconds != timeLimit) {
					Chat.server("&4[Lobby] &cLobby restarting in " + Chat.time(timeLimit - seconds) + "!");
				}
				if (seconds >= timeLimit) {
					Bukkit.shutdown();
					this.cancel();
					return;
				}
			}
		}.runTaskTimerAsynchronously(this, 0, 20);

        getCommand("spawn").setExecutor(new SpawnCmd());
        
        // Compass Item!
        ItemStack c = new ItemStack(Material.COMPASS);
        ItemMeta meta = c.getItemMeta();
        meta.setDisplayName(Chat.colors("&aGame Picker"));
        List<String> list = new ArrayList<String>();
        list.add(Chat.colors("&7Right click and select a"));
        list.add(Chat.colors("&7game to play on!"));
        meta.setLore(list);
        c.setItemMeta(meta);
        compass = c;
        
        // Book Item!
    	ItemStack b = new ItemStack(Material.WRITTEN_BOOK, 1);
        BookMeta data = (BookMeta) b.getItemMeta();
        data.setAuthor(Chat.colors("&7&oMC Zone"));
        data.setTitle(Chat.colors("&b&lAbout MC Zone"));
        String page1 = "";
        String page2 = "";
        String page3 = "";
        String page4 = "";
        String page5 = "";
        
        // Page 1
        page1 += "      &4&lMC Zone\n\n";
        page1 += "&0Welcome to MC Zone! Enjoy many different types of minigames and activites all in one server! To join a game, find a server that you would like to play on and enter that servers' portal.";
        
        page2 += "       &oRules\n\n";
        page2 += "&0- Casual Language\n";
        page2 += "- No spam\n";
        page2 += "- No Advertising\n";
        page2 += "- Obey staff\n";
        page2 += "- Be respectful\n";
        page2 += "- No team griefing\n";
        page2 += "- No client side mods\n";
        page2 += "\nFor more, visit: \nwww.mczone.co/rules";
        
        page3 += "     &oDonations\n\n";
        page3 += "&0Donate to get upgraded to VIP, Elite or Titan. Each has different benefits. You can also buy various kits for HG, Walls, Sky Wars and Nexus. Visit our website, www.mczone.co/donate for more information!";   
        
        page4 += "   &oAbout MC Zone\n\n";
        page4 += "&0MC Zone started on October 11th and has grown expontentially. We now have over 300,000 users and often 1,800+ players online! We are expecting to grow even further as we better the MC Zone experience!";
        
        page5 += "    &oContact Us!\n\n";
        page5 += "&0The first place for help would be our forums at our website. A community of members, mods, and admins can all help you. Feel free to contact us at info@mczone.co if you would prefer!";
        
        data.setPages(Chat.colors(page1), Chat.colors(page2), Chat.colors(page3), Chat.colors(page4), Chat.colors(page5));
        b.setItemMeta(data);
        book = b;
	}
	
	
	public static void rmdir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				rmdir(new File(dir, children[i]));
			}
		}
		dir.delete();
	}
	
	public static void addPotionEffect(Player p, PotionEffect pe) {
		Packet41MobEffect pm = new Packet41MobEffect();
		pm.a = p.getEntityId();
		pm.b = (byte)pe.getType().getId();
		pm.c = (byte)pe.getAmplifier();
		pm.d = (short)pe.getDuration();
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(pm);
		pm = null;
	}

	public static void removePotionEffect(Player p, PotionEffectType pe) {
		Packet42RemoveMobEffect pm = new Packet42RemoveMobEffect();
		pm.a = p.getEntityId();
		pm.b = (byte) pe.getId();
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(pm);
		pm = null;
	}

	public static ItemStack book;
	public static ItemStack compass;
	
	public void onDisable() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.kickPlayer("Restarting lobby!");
		}
		Bukkit.unloadWorld("void", false);
	}
}
