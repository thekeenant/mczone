package co.mczone.cmds;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.mczone.api.players.Rank;
import co.mczone.api.players.RankType;
import co.mczone.api.server.Hive;
import co.mczone.util.Chat;

public class UnbanCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { 
    	if (Rank.getRank(sender.getName()).getType().getLevel() < RankType.MOD.getLevel()) {
    		Chat.player(sender, "&cYou don't have permission to do this.");
    		return true;
    	}
    	
        if (args.length != 1) {
        	Chat.player(sender, "&cUsage: /unban [username]");
        	return true;
        }
        
        String username = null;
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
        
        boolean found = false;
        ResultSet r = Hive.getInstance().getDatabase().query("SELECT * FROM infractions WHERE username='" + username + "' WHERE active=1 AND (type='ban' OR type='tempban')");
        try {
			while (r.next()) {
				found = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
        if (!found) {
        	Chat.player(sender, "&cThe player, " + username + ", has not been banned.");
        	return true;
        }
        
        Hive.getInstance().getDatabase().update("UPDATE infractions SET active=0 WHERE username='" + username + "'");
        Chat.player(sender, "&aThe user, " + username + ", has been unbanned.");
        return true;
    }
}
