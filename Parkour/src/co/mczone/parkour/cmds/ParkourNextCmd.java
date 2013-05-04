package co.mczone.parkour.cmds;

import org.bukkit.command.CommandSender;

import co.mczone.parkour.*;
import co.mczone.util.Chat;

public class ParkourNextCmd implements SubCommand {
    public boolean execute(CommandSender sender, String[] args) {
    	if (!CmdBase.isPlayer(sender))
    		return true;
    	
    	if (args.length != 2) {
    		Chat.player(sender, "&cPlease include the name and the next parkour course.");
    		return true;
    	}
    	
    	Course c = Course.get(args[0]);
    	if (c == null) {
    		Chat.player(sender, "&7[Parkour] Couldn't find the course, " + args[0]);
    		return true;
    	}
    	
    	Course next = Course.get(args[1]);
    	if (next == null) {
    		Chat.player(sender, "&7[Parkour] Couldn't find the course, " + args[1]);
    		return true;
    	}
    	
    	c.setNextCourse(next.getName());
    	
    	Parkour.getInstance().getConfigAPI().set(args[0] + ".next", next.getName());
    	Chat.player(sender, "&7[Parkour] &aSet next course to: " + next.getName());
    	Parkour.getInstance().saveConfig();
    	Parkour.getInstance().loadCourses();
    	
    	
    	
    	
    	
        return true;
    }
}
