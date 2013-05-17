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

public class AdminDeleteSpawnCmd implements SubCommand, Permissible {
    public boolean execute(CommandSender sender, String[] args) {
    	if (args.length != 1) {
    		Chat.player(sender, "&cPlease include the spawn name.");
    		return true;
    	}
    	
    	Player p = (Player) sender;
    	
    	Map map = null;
    	for (Map m : Map.getList()) {
    		if (m.getWorldName().equalsIgnoreCase(p.getWorld().getName()))
    			map = m;
    	}
    	
    	String spawnName = args[0];
    	ConfigAPI api = new ConfigAPI(map.getConfig());
    	if (!api.contains("spawns." + spawnName)) {
    		Chat.player(sender, "&cCouldn't find the spawn, \"" + spawnName + "\".");
    		return true;
    	}
    	api.remove("spawns." + spawnName);
    	api.save(new File(Map.getDirectory(), map.getWorldName() + ".yml"));
    	map.reload();
    	Chat.player(sender, "&aRemoved spawn, &2" + spawnName + "&a from " + map.getTitle());
        return true;
    }

	@Override
	public boolean hasPermission(Gamer g) {
		if (g.getRank().getLevel() < RankType.ADMIN.getLevel())
			return false;
		return true;
	}

	@Override
	public String getAbout() {
		return "Remove team spawn";
	}
}
