package co.mczone.walls.utils;

import java.util.logging.Level;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Chat {
	public static void simple(CommandSender p, String msg) {
		p.sendMessage(ChatColor.translateAlternateColorCodes("&".charAt(0), msg));
	}

	public static void player(CommandSender p, String msg) {
		p.sendMessage(ChatColor.translateAlternateColorCodes("&".charAt(0), msg));
	}
	
	public static void log(String msg) {
		Bukkit.getServer().getLogger().log(Level.INFO, "[Walls] " + msg);
	}
	
	public static void log(Level l, String msg) {
		Bukkit.getServer().getLogger().log(l, "[Walls] " + msg);
	}

	public static void server(String msg) {
		Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes("&".charAt(0), msg));
	}
	
	public static String capitalize(String word) {
		return WordUtils.capitalize(word);
	}
	
	public static String time(int input) {
		int minutes = input / 60;
		int seconds = input - (minutes * 60);
		String str_min = "minute";
		String str_sec = "second";

		if (minutes != 1) str_min += "s";
		if (seconds != 1) str_sec += "s";
		
		if (minutes != 0)
			if (seconds != 0)
				return minutes + " " + str_min + ", " + seconds + " " + str_sec;
			else
				return minutes + " " + str_min;
		else
			return seconds + " " + str_sec;
	}
	
	public static String colors(String s) {
		return ChatColor.translateAlternateColorCodes("&".charAt(0), s);
	}
	
	public static boolean setPlayerListName(Player p, String s) {
		Boolean result = true;
		if (s.length() > 16) {
			s = s.substring(0, 16);
			result = false;
		}
		
		p.setPlayerListName(s);
		return result;
	}
}
