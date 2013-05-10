package co.mczone.ghost.cmds;

import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import co.mczone.api.ConfigAPI;
import co.mczone.api.players.Gamer;
import co.mczone.cmds.SubCommand;
import co.mczone.ghost.Ghost;
import co.mczone.ghost.api.Arena;
import co.mczone.util.Chat;

public class GhostSetCmd implements SubCommand {
	@Getter String about = "Modify arena settings";
	
	@Override
	public boolean execute(CommandSender sender, String[] args) {
		Gamer g = Gamer.get(sender.getName());
		Arena a = Arena.getMatch(g.getPlayer());
		
		if (a == null) {
			Chat.player(sender, "&cYou must be in an arena to change settings");
			return true;
		}
		
		if (args.length == 0) {
			Chat.player(sender, "&cWrong arguments: /ghost set [variable] (setting)");
			return true;
		}
		
		String type = args[0].toLowerCase();
		ConfigAPI config = Ghost.getConf();
		String base = "matches." + a.getWorldName() + ".";
		if (type.contains("red")) {
			Location l = g.getPlayer().getLocation();
			config.set(base + "red", l);
			a.setBlueSpawn(l);
		}
		else if (type.contains("blue")) {
			Location l = g.getPlayer().getLocation();
			config.set(base + "red", l);
			a.setRedSpawn(l);
		}
		else if (type.contains("spawn")) {
			Location l = g.getPlayer().getLocation();
			config.set(base + "spawn", l);
			a.setSpawn(l);
		}
		else if (type.contains("sign")) {
			Block b = g.getPlayer().getTargetBlock(null, 0);
			if (b.getType() != Material.WALL_SIGN) {
				Chat.player(sender, "&cYou are not looking at a sign");
				return true;
			}

			config.set(base + "sign.world", b.getWorld().getName());
			config.set(base + "sign.x", b.getX());
			config.set(base + "sign.y", b.getY());
			config.set(base + "sign.z", b.getZ());
			a.setSignBlock(b);
		}
		
		Chat.server("&aChanged setting " + type + " in " + a.getTitle());
		Ghost.getConf().set(a.getWorldName(), a.getConfig());
		return true;
	}
}
