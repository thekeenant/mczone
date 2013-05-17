package co.mczone.ghost.cmds;

import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import co.mczone.api.ConfigAPI;
import co.mczone.api.commands.SubCommand;
import co.mczone.api.players.Gamer;
import co.mczone.api.players.Permissible;
import co.mczone.api.players.RankType;
import co.mczone.ghost.Ghost;
import co.mczone.ghost.api.Arena;
import co.mczone.util.Chat;

public class GhostSetCmd implements SubCommand,Permissible {
	@Getter String about = "Modify arena settings";
	
	@Override
	public boolean execute(CommandSender sender, String[] args) {
		Gamer g = Gamer.get(sender.getName());
		Arena a = Arena.getArena(g.getPlayer());

		if (args.length == 0) {
			Chat.player(sender, "&cWrong arguments: /ghost set [variable] (setting)");
			return true;
		}
		
		ConfigAPI config = Ghost.getConf();
		String base = "";
		String type = args[0].toLowerCase();
		if (type.equals("lobby")) {			
			Location l = g.getPlayer().getLocation();
			config.set("lobby", l);
			Ghost.getLobby().setWorld(l.getWorld());
			Ghost.getLobby().setSpawn(l);
		}
		else if (type.contains("sign")) {
			if (args.length != 2) {
				Chat.player(sender, "&cMust include the arena name");
				return true;
			}
			a = Arena.get(args[1]);
			if (a == null) {
				Chat.player(sender, "&cUnknown arena, " + args[1]);
				return true;
			}
			
			base = "arenas." + a.getWorldName() + ".";
			Block b = g.getPlayer().getTargetBlock(null, 5);
			if (b == null || b.getType() != Material.WALL_SIGN) {
				Chat.player(sender, "&cYou must look at a sign to set the sign variable");
				return true;
			}

			config.set(base + "sign.world", b.getWorld().getName());
			config.set(base + "sign.x", b.getX());
			config.set(base + "sign.y", b.getY());
			config.set(base + "sign.z", b.getZ());
			a.setSignBlock(b);
		}
		else {
			if (a == null) {
				Chat.player(sender, "&cYou must specify an arena to change settings");
				return true;
			}
			
			base = "arenas." + a.getWorldName() + ".";
			if (type.contains("red")) {
				Location l = g.getPlayer().getLocation();
				config.set(base + "red", l);
				a.setBlueSpawn(l);
			}
			else if (type.contains("spawn")) {
				Location l = g.getPlayer().getLocation();
				config.set(base + "spawn", l);
				a.setSpawn(l);
			}
			else if (type.contains("blue")) {
				Location l = g.getPlayer().getLocation();
				config.set(base + "blue", l);
				a.setBlueSpawn(l);
			}
			else {
				Chat.player(sender, "&cUnknown setting, " + args[0]);
				return true;
			}
		}
		
		if (a != null) {
			Chat.player(sender, "&aChanged setting " + type + " in " + a.getTitle());
			Ghost.getConf().set("arenas." + a.getWorldName(), a.getConfig());
		}
		else {
			Chat.player(sender, "&aChanged setting " + type);
		}
		Ghost.getInstance().saveConfig();
		return true;
	}

	@Override
	public boolean hasPermission(Gamer g) {
		if (g.getRank().getLevel() < RankType.ADMIN.getLevel())
			return false;
		return true;
	}
}
