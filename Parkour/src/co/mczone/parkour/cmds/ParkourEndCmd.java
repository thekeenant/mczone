package co.mczone.parkour.cmds;

import lombok.Getter;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.mczone.api.commands.SubCommand;
import co.mczone.parkour.*;
import co.mczone.util.Chat;

public class ParkourEndCmd implements SubCommand {
	@Getter String about = "Set parkour end button";
	
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
    	
		Block b = p.getTargetBlock(null, 10);
		if (b == null || b.getType() != Material.STONE_BUTTON) {
			Chat.player(sender, "&2[SG] &cThat is not a button");
			return true;
		}
		Parkour.getInstance().getConfigAPI().set(c.getName() + ".end.x", b.getX());
		Parkour.getInstance().getConfigAPI().set(c.getName() + ".end.y", b.getY());
		Parkour.getInstance().getConfigAPI().set(c.getName() + ".end.z", b.getZ());
		Parkour.getInstance().getConfigAPI().set(c.getName() + ".end.world", b.getWorld().getName());
		Chat.player(sender, "&7[Parkour] &aEnd button added to " + c.getName());
    	Parkour.getInstance().saveConfig();
    	Parkour.getInstance().loadCourses();
    	
        return true;
    }
}
