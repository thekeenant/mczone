package co.mczone.parkour.cmds;

import org.bukkit.command.CommandSender;

import co.mczone.parkour.*;
import co.mczone.util.Chat;

public class ParkourNewCmd implements SubCommand {
    public boolean execute(CommandSender sender, String[] args) {
    	if (!CmdBase.isPlayer(sender))
    		return true;
    	
    	if (args.length < 2) {
    		Chat.player(sender, "&cPlease include the name and title.");
    		return true;
    	}
    	
    	String name = args[0];
    	String title = "";
    	for (String arg : args) {
    		if (arg.equalsIgnoreCase(name))
    			continue;
    		
    		title += arg + " ";
    	}
    	
    	new Course(name, title);
    	Parkour.getInstance().getConfigAPI().set(name + ".title", title);
    	Chat.player(sender, "&7[Parkour] &aYou have created: " + title + " (" + name + ")");
    	Parkour.getInstance().saveConfig();
    	Parkour.getInstance().loadCourses();
    	
        return true;
    }
}
