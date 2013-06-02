package co.mczone.walls.cmds;

import java.util.HashMap;
import co.mczone.util.Chat;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HelpCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	HashMap<String,String> help = new HashMap<String,String>();
		Chat.player(sender, "&7---------- &4&oWalls Squadron Commands&7 -----------");
		help.put("/help", "View Survival Game commands");
		help.put("/list", "View online gamers");
		help.put("/leave", "Leave your current match");
		for (Entry<String, String> e : help.entrySet()) {
			Chat.player(sender, "&6" + e.getKey() + "&f: " + e.getValue());
		}
		Chat.player(sender, "&7----------------------------------------");
		return true;
    }

}
