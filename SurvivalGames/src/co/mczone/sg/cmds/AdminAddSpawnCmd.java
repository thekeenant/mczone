package co.mczone.sg.cmds;

import java.io.File;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.mczone.sg.api.ConfigAPI;
import co.mczone.sg.api.Map;
import co.mczone.sg.api.SubCommand;
import co.mczone.util.Chat;

public class AdminAddSpawnCmd implements SubCommand {
    public boolean execute(CommandSender sender, String[] args) {
    	if (!CmdBase.isPlayer(sender))
    		return true;
    	
    	Player p = (Player) sender;
    	
    	if (args.length != 1) {
    		Chat.player(sender, "&cPlease include the spawn name.");
    		return true;
    	}
    	
    	Map map = null;
    	for (Map m : Map.getList()) {
    		if (m.getWorldName().equalsIgnoreCase(p.getWorld().getName()))
    			map = m;
    	}
    	
    	String name = args[0];
    	ConfigAPI api = new ConfigAPI(map.getConfig());
    	api.set("spawns." + name, p.getLocation());
    	api.save(new File(Map.getDirectory(), map.getWorldName() + ".yml"));
    	if (!map.reload()) {
    		Chat.player(sender, "&4[SG] &cThere was an error reloading the map, check the logs!");
    		return true;
    	}
    	
    	Chat.player(sender, "&aAdded spawn &2" + name +"&a to &2" + map.getTitle());
        return true;
    }
}
