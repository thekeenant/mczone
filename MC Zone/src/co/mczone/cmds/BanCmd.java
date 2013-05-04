package co.mczone.cmds;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.mczone.api.infractions.Ban;
import co.mczone.api.players.Rank;
import co.mczone.api.players.RankType;
import co.mczone.api.server.Hive;
import co.mczone.util.Chat;

public class BanCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (Rank.getRank(sender.getName()).getType().getLevel() < RankType.MOD.getLevel()) {
    		Chat.player(sender, "&cYou don't have permission to do this.");
    		return true;
    	}
        if (args.length < 2) {
        	Chat.player(sender, "&cUsage: /ban [username] [reason]");
        	return true;
        }
        
        String username = null;
        String reason = Chat.stripColor(StringUtils.join(args, " ", 1, args.length));
        
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
        String prefix = "INSERT INTO infractions (username,staff,type,reason) VALUES ";
        String content = "('" + username + "','" + sender.getName() + "','ban','" + reason + "')";
        Hive.getInstance().getDatabase().update(prefix + content);
        
        Chat.player(sender, "&4[Ban] &b" + username + " &7&ofor &c" + reason);
        
        Ban i = new Ban(username, reason, Hive.getInstance().getServerTime());
      	if (p != null && p.isOnline())
      		p.kickPlayer(i.getKickMessage());
      	
        return true;
    }
}
