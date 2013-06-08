package co.mczone.nexus.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.mczone.api.players.Gamer;
import co.mczone.nexus.Nexus;
import co.mczone.nexus.api.Kit;
import co.mczone.nexus.enums.GameState;
import co.mczone.util.Chat;

public class KitCmd implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Gamer g = Gamer.get(sender);
		
		if (args.length < 1) {
        	Kit.list(sender);
        	return true;
		}
		
		String s = args[0];
		Kit k = Kit.get(s);
		
		if (k == null) {
			Chat.player(g, "&cCould not find the kit, &4" + s);
			return true;
		}
		
		g.setVariable("kit", k);
		
		if (Nexus.getRotary().getState() == GameState.PLAYING)
			Chat.player(g, "&7You will receieve the &f" + k.getName() + "&7 kit upon respawning.");
		else
			Chat.player(g, "&7You have selected the &f" + k.getName() + "&7 kit.");

		return true;
	}

}
