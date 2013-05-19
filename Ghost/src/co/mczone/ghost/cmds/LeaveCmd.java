package co.mczone.ghost.cmds;

import co.mczone.api.players.Gamer;
import co.mczone.ghost.Ghost;
import co.mczone.ghost.api.Arena;
import co.mczone.util.Chat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class LeaveCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	Gamer g = Gamer.get(sender);
    	if (g.getVariable("arena") == null) {
    		Chat.player(sender, "&cYou are not in a match!");
    		return true;
    	}
    	
    	Arena a = (Arena) g.getVariable("arena");
    	a.leave(g.getPlayer());
    	g.teleport(Ghost.getLobby().getSpawn());
    	
    	
		return true;
    }

}
