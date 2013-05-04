package co.mczone.util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Chat {
	public static void player(CommandSender p, String msg) {
		p.sendMessage(ChatColor.translateAlternateColorCodes("&".charAt(0), msg));
	}
	
	public static void log(String msg) {
		log(Level.INFO, msg);
	}
	
	public static void log(Level l, String msg) {
		Bukkit.getServer().getLogger().log(l, "[MC Zone] " + msg);
	}

	public static void server(String msg) {
		Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes("&".charAt(0), msg));
	}
	
	public static String stripColor(String msg) {
		return ChatColor.stripColor(Chat.colors(msg));
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
	
	public static List<String> colors(List<String> list) {
		List<String> result = new ArrayList<String>();
		for (String s : list)
			result.add(Chat.colors(s));
		return result;
	}
	
	public static String toString(List<String> list, String seperator) {
		String result = "";
		for (String s : list)
			result += s + seperator;
		result = result.substring(0, result.length() - 2);
		return result;
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
	
	public static String toClock(int secondsCount){
	    int seconds = secondsCount % 60;
	    secondsCount -= seconds;
	    long minutesCount = secondsCount / 60;
	    long minutes = minutesCount % 60;
	    minutesCount -= minutes;
        String minutesS = (minutes < 10) ? "0" + minutes : minutes + "";
        String secondsS = (seconds < 10) ? "0" + seconds : seconds + "";
	    
	    return minutesS + ":" + secondsS;
	}  
}
