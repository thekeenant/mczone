package co.mczone.cmds;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.mczone.api.infractions.Tempban;
import co.mczone.api.players.Rank;
import co.mczone.api.players.RankType;
import co.mczone.api.server.Hive;
import co.mczone.util.Chat;

public class TempbanCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (Rank.getRank(sender.getName()).getType().getLevel() < RankType.MOD.getLevel()) {
    		Chat.player(sender, "&cYou don't have permission to do this.");
    		return true;
    	}
        if (args.length < 3) {
        	Chat.player(sender, "&cUsage: /tempban [username] [time] [reason]");
        	return true;
        }
        
        String username = null;
        String reason = Chat.stripColor(StringUtils.join(args, " ", 2, args.length));
        
        Player p = Bukkit.getPlayerExact(args[0]);
        if (p != null)
        	username = p.getName();
        else {
        	String check = Hive.getInstance().getUsername(args[0]);
        	if (check != null)
        		username = check;
        }
        if (username == null) {
        	Chat.player(sender, "&cNo player found by the name of, " + args[0]);
        	return true;
        }
        
        int duration = parseTime(args[1]);
        if (duration == 0) {
            Chat.player(sender, "&cInvalid duration! (ex. 10m, 6h, 1d, 1w)");
            return true;
        }
        String prefix = "INSERT INTO infractions (username,staff,type,end,reason) VALUES ";
        String content = "('" + username + "','" + sender.getName() + "','tempban',DATE_ADD(now(), INTERVAL " + duration + " SECOND),'" + reason + "')";
        Hive.getInstance().getDatabase().update(prefix + content);
        Chat.player(sender, "&4[Tempban] &b" + username + " &7&ofor &c" + reason + " &7(" + getDuration(args[1]) + ")");
        
        Date expires = null;
        ResultSet r = Hive.getInstance().getDatabase().query("SELECT DATE_ADD(now(), INTERVAL " + duration + " SECOND)");
        try {
			while (r.next())
				expires = r.getTimestamp("DATE_ADD(now(), INTERVAL " + duration + " SECOND)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
        	
        Tempban i = new Tempban(username, reason, Hive.getInstance().getServerTime(), expires);
      	if (p != null && p.isOnline())
      		p.kickPlayer(i.getKickMessage());
      	
    	return true;
    }
    
    public static int parseTime(String time) {
        String[] arr = time.split("");
        String sdur = "0";
        for (String c : arr) {
            try {
                if (Integer.valueOf(c) != null)
                    sdur += c;
            }
            catch (NumberFormatException e) {
                continue;
            }
        }
        int duration = Integer.valueOf(sdur);
        if (duration == 0) {
            return 0;
        }

        if (time.contains("s") || time.contains("second")) {
            return duration;
        }
        if (time.contains("m") || time.contains("minute")) {
            duration *= 60;
            return duration;
        }
        if (time.contains("h") || time.contains("hour")) {
            duration *= 60 * 60;
            return duration;
        }
        if (time.contains("d") || time.contains("day")) {
            duration *= 60 * 60 * 24;
            return duration;
        }
        if (time.contains("w") || time.contains("week")) {
            duration *= 60 * 60 * 24 * 7;
            return duration;
        }
        return 0;
    }

    public static String getDuration(String time) {
        String[] arr = time.split("");
        String sdur = "0";
        for (String c : arr) {
            try {
                if (Integer.valueOf(c) != null)
                    sdur += c;
            }
            catch (NumberFormatException e) {
                continue;
            }
        }
        int duration = Integer.valueOf(sdur);
        Chat.log(sdur);

        String ending = "seconds";
        if (time.contains("m")) {
            ending = "minutes";
        }
        if (time.contains("h")) {
            ending = "hours";
        }
        if (time.contains("d")) {
            ending = "days";
        }
        if (time.contains("w")) {
            ending = "weeks";
        }
        if (duration == 1)
            ending = ending.substring(0, ending.length() - 1);

        return duration + " " + ending;
    }
}
