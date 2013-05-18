package co.mczone.parkour.cmds;

import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.mczone.api.commands.SubCommand;
import co.mczone.parkour.*;
import co.mczone.util.Chat;

public class ParkourSpawnCmd implements SubCommand {
	@Getter String about = "Set parkour spawn";
	
    public boolean execute(CommandSender sender, String[] args) {
    	if (args.length != 1) {
    		Chat.player(sender, "&cPlease include the name.");
    		return true;
    	}
    	
    	Course c = Course.get(args[0]);
    	if (c == null) {
    		Chat.player(sender, "&7[Parkour] Couldn't find the course, " + args[0]);
    		return true;
    	}
    	
    	Player p = (Player) sender;
    	Location spawn = p.getLocation();
    	Parkour.getInstance().getConfigAPI().set(c.getName() + ".start", spawn);
    	Chat.player(sender, "&7[Parkour] &aSet start location of " + c.getName() + " to your location");
    	Parkour.getInstance().saveConfig();
    	Parkour.getInstance().loadCourses();
        return true;
    }
}
