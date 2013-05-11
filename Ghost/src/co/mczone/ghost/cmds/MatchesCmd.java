package co.mczone.ghost.cmds;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.mczone.ghost.api.Arena;

public class MatchesCmd implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		List<Arena> matches = Arena.getList();
		
		for (Arena match : matches) {
			String t = match.getTitle();
		}
		
		// Nice work haribo :P
		
		return false;
	}

}
