package co.mczone.cmds;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.mczone.api.players.Gamer;
import co.mczone.api.players.Permissible;
import co.mczone.api.players.RankType;
import co.mczone.util.Chat;

public class BaseCommand implements CommandExecutor,Permissible {
	@Getter	HashMap<String, SubCommand> subCommands = new HashMap<String, SubCommand>();
	@Getter	@Setter String title = "";

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (!hasPermission(Gamer.get(sender.getName()))) {
				Chat.player(sender, "&cYou don't have permission to use that command.");
				return true;
			}
		}
		
		if (args.length == 0) {
			Chat.player(sender, title);
			for (Entry<String, SubCommand> e : subCommands.entrySet()) {
				String s = e.getKey();
				SubCommand c = e.getValue();
				Chat.player(sender, "&6/ghost " + s + "&f: " + c.getAbout());
			}
			return true;
		} 
		else {
			for (Entry<String, SubCommand> e : subCommands.entrySet()) {
				if (e.getKey().equalsIgnoreCase(args[0]))
					return e.getValue().execute(sender,
							Arrays.copyOfRange(args, 1, args.length));
			}

			Chat.player(sender, "&cInvalid sub command.");
			return true;
		}
	}

	@Override
	public boolean hasPermission(Gamer g) {
		if (g.getRank().getLevel() < RankType.ADMIN.getLevel())
			return false;
		return true;
	}
}
