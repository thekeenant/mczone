package co.mczone.sg.cmds;

import org.bukkit.Bukkit;
import co.mczone.util.Chat;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (!CmdBase.isPlayer(sender))
    		return true;
    	
    	if (!CmdBase.isAdmin(sender))
    		return true;
    	
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
}