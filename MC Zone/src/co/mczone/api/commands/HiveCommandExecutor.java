package co.mczone.api.commands;

import java.util.HashMap;
import java.util.Map.Entry;

import lombok.Getter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.mczone.api.players.Gamer;
import co.mczone.api.players.Permissible;
import co.mczone.util.Chat;

public class HiveCommandExecutor implements CommandExecutor {
	@Getter static HashMap<String, CommandExecutor> commands = new HashMap<String, CommandExecutor>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		CommandExecutor command = null;
		for (Entry<String, CommandExecutor> e : commands.entrySet()) {
			
			if (e.getKey().equalsIgnoreCase(cmd.getName())) {
				command = e.getValue();
				
				if (e.getValue() instanceof Permissible) {
					Permissible perm = (Permissible) e.getValue();
					if (perm.hasPermission(Gamer.get(sender))) {
						command = e.getValue();
						break;
					}
					else {
						Chat.player(sender, "&cYou do not have permission to use that command.");
						return true;
					}
				}
				else {
					command = e.getValue();
					break;
				}
			}
		}
		
		if (command == null) {
			return false;
		}
		
		return command.onCommand(sender, cmd, label, args);
	}

}
