package co.mczone.api.commands;

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
import co.mczone.util.Chat;

public class BaseCommand implements CommandExecutor {
	@Getter	HashMap<String, SubCommand> subCommands = new HashMap<String, SubCommand>();
	@Getter	@Setter String title = "";
	@Getter @Setter boolean carryPermissions = false;

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player && carryPermissions) {
			if (this instanceof Permissible) {
				Permissible perm = (Permissible) this;
				if (!perm.hasPermission(Gamer.get(sender.getName()))) {
					Chat.player(sender, "&cYou don't have permission to use that command.");
					return true;
				}
			}
		}
		
		if (args.length == 0) {
			Chat.player(sender, title);
			for (Entry<String, SubCommand> e : subCommands.entrySet()) {
				String s = e.getKey();
				SubCommand c = e.getValue();
				if (c instanceof Permissible && !carryPermissions) {
					Permissible perm = (Permissible) c;
					if (!perm.hasPermission(Gamer.get(sender))) {
						continue;
					}
				}
				Chat.player(sender, "&6/" + label + " " + s + "&f: " + c.getAbout());
			}
			return true;
		} 
		else {
			SubCommand sub = null;
			for (Entry<String, SubCommand> e : subCommands.entrySet()) {
				if (e.getKey().equalsIgnoreCase(args[0]))
					sub = e.getValue();
			}
			if (sub != null) {
				if (sub instanceof Permissible && !carryPermissions) {
					Permissible perm = (Permissible) sub;
					if (!perm.hasPermission(Gamer.get(sender))) {
						Chat.player(sender, "&cYou don't have permission to do that.");
						return true;
					}
				}
				return sub.execute(sender,	Arrays.copyOfRange(args, 1, args.length));
			}

			Chat.player(sender, "&cInvalid sub command.");
			return true;
		}
	}
}
