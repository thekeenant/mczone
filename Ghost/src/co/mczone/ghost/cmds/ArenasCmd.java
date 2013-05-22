package co.mczone.ghost.cmds;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.mczone.ghost.api.Arena;
import co.mczone.ghost.api.ArenaState;
import co.mczone.util.Chat;

public class ArenasCmd implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		List<Arena> matches = Arena.getList();
		
		String list = "";
		for (Arena match : matches) {
			ChatColor color = ChatColor.GRAY;
			if (match.getState() != ArenaState.LOADING)
				color = match.getState() == ArenaState.WAITING ? ChatColor.GREEN : ChatColor.RED;
			
			
			String t = color + match.getCurrent().getTitle() + "&f, ";
			list += t;
		}
		
		list = list.substring(0, list.length() - 2);
		Chat.player(sender, "&7Arenas: " + list);
		return true;
	}

}
