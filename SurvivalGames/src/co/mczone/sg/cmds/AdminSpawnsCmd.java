package co.mczone.sg.cmds;

import org.bukkit.Location;
import co.mczone.util.Chat;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.mczone.sg.api.Map;
import co.mczone.sg.api.SubCommand;

public class AdminSpawnsCmd implements SubCommand {
    public boolean execute(CommandSender sender, String[] args) {
    	if (!CmdBase.isPlayer(sender))
    		return true;
    	
    	Player p = (Player) sender;
    	
    	if (args.length != 1) {
    		Chat.player(sender, "&4[SG] &cPlease include the world name");
    		return true;
    	}
    	
    	Chat.player(sender, Map.getList().toString());
    	Map m = Map.getByID(1);
    	
    	
    	for (Location l : m.getSpawns()) {
    		l.setWorld(m.getWorld());
    		p.sendBlockChange(l, Material.GLOWSTONE, (byte) 0);
    	}
    	
    	Chat.player(sender, "&2[SG] &aSpawns have been shown with glowstone");
    	
        return true;
    }
}
