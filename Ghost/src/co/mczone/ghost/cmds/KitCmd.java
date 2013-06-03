package co.mczone.ghost.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.mczone.api.players.Gamer;
import co.mczone.api.players.Permissible;
import co.mczone.api.players.RankType;
import co.mczone.ghost.api.Arena;
import co.mczone.ghost.api.ArenaState;
import co.mczone.ghost.api.Kit;
import co.mczone.util.Chat;

public class KitCmd implements CommandExecutor, Permissible {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Gamer g = Gamer.get(sender);
		
		if (args.length == 0) {
			String kits = "Kits: ";
			for (Kit k : Kit.getList()) {
				if (k.hasPermission(g))
					kits += "&a" + k.getName();
				else
					kits += "&c" + k.getName();
				
				kits += "&f, ";
			}
			
			Chat.player(g, kits.substring(0, kits.length() - 2));
			Chat.player(g, "&7Choose your kit with &o/kit <kit name>");
			return true;
		}
		
		String s = args[0];
		Kit kit = Kit.get(s);
		if (kit == null) {
			Chat.player(g, "&cThe kit, " + args[0] + ", does not exist!");
			return true;
		}
		
		if (!kit.hasPermission(g)) {
			Chat.player(g, "You can purchase this kit at www.mczone.co/shop");
			return true;
		}
		
		boolean allow = true;
		if (g.getVariable("arena") != null) {
			Arena a = (Arena) g.getVariable("arena");
			if (a.getState() == ArenaState.STARTED)
				allow = false;
		}
		
		if (!allow) {
			Chat.player(g, "&cYou cannot change kits at this time.");
			return true;
		}
		
		g.setVariable("kit", kit);
		Kit.giveKit(g.getPlayer());
		Chat.player(g, "&eYou have selected the &6" + kit.getName() + "&e kit");
		return true;
	}

	@Override
	public boolean hasPermission(Gamer g) {
		if (g.getRank().getType() == RankType.CONSOLE)
			return false;
		return true;
	}
	
}
