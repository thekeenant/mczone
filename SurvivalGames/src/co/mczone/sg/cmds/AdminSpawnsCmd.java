package co.mczone.sg.cmds;

import org.bukkit.Location;
import co.mczone.util.Chat;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.mczone.api.commands.SubCommand;
import co.mczone.api.players.Gamer;
import co.mczone.api.players.Permissible;
import co.mczone.api.players.RankType;
import co.mczone.sg.api.Map;

public class AdminSpawnsCmd implements SubCommand, Permissible {
    public boolean execute(CommandSender sender, String[] args) {    	
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

	@Override
	public boolean hasPermission(Gamer g) {
		if (g.getRank().getLevel() < RankType.ADMIN.getLevel())
			return false;
		return true;
	}

	@Override
	public String getAbout() {
		return "Show spawn points with glowstone";
	}
}
