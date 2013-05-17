package co.mczone.sg.cmds;

import java.io.File;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.mczone.api.commands.SubCommand;
import co.mczone.api.players.Gamer;
import co.mczone.api.players.Permissible;
import co.mczone.api.players.RankType;
import co.mczone.sg.api.ConfigAPI;
import co.mczone.sg.api.Map;
import co.mczone.util.Chat;

public class AdminAddSpawnCmd implements SubCommand,Permissible {
    public boolean execute(CommandSender sender, String[] args) {
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

	@Override
	public String getAbout() {
		return "Add a spawn location";
	}

	@Override
	public boolean hasPermission(Gamer g) {
		if (g.getRank().getLevel() < RankType.ADMIN.getLevel())
			return false;
		return true;
	}
}
