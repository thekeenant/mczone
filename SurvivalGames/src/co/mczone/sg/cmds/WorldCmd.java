package co.mczone.sg.cmds;

import org.bukkit.Bukkit;

import co.mczone.api.players.Gamer;
import co.mczone.api.players.Permissible;
import co.mczone.api.players.RankType;
import co.mczone.util.Chat;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldCmd implements CommandExecutor, Permissible {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	Player p = (Player) sender;
    	
    	if (args.length != 1) {
    		Chat.player(sender, "&4[SG] &cPlease include the world name");
    		return true;
    	}
    	
    	String name = args[0];
    	World w = Bukkit.getWorld(name);
    	if (w == null)
    		Bukkit.createWorld(new WorldCreator(name));
    	w = Bukkit.getWorld(name);
    	
    	p.teleport(w.getSpawnLocation());
    	Chat.player(sender, "&2[SG] &aTeleported to " + w.getName() + " spawn point");
    	
    	
		return true;
    }

	@Override
	public boolean hasPermission(Gamer g) {
		if (g.getRank().getLevel() < RankType.ADMIN.getLevel())
			return false;
		return true;
	}
}