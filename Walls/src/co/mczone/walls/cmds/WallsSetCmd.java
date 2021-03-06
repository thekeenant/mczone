package co.mczone.walls.cmds;

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
import co.mczone.util.Chat;
import co.mczone.walls.Walls;
import co.mczone.walls.api.Arena;

public class WallsSetCmd implements SubCommand,Permissible {
	@Getter String about = "Modify arena settings";
	
	@Override
	public boolean execute(CommandSender sender, String[] args) {
		Gamer g = Gamer.get(sender.getName());
		Arena a = Arena.getArena(g.getPlayer());

		if (args.length == 0) {
			Chat.player(sender, "&cWrong arguments: /walls set [variable] (setting)");
			return true;
		}
		
		ConfigAPI config = Walls.getConf();
		String base = "";
		String type = args[0].toLowerCase();
		if (type.equals("lobby")) {			
			Location l = g.getPlayer().getLocation();
			config.set("lobby", l);
			Walls.getLobby().setWorld(l.getWorld());
			Walls.getLobby().setSpawn(l);
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
			
			base = "arenas." + a.getName() + ".";
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
			
			Chat.player(sender, "&aChanged setting " + type + " in " + a.getName());
			save();
			return true;
		}
		else {
			if (a == null) {
				Chat.player(sender, "&cYou must specify an arena to change settings");
				return true;
			}
			
			base = "maps." + a.getCurrent().getWorldName() + ".";
			if (type.contains("red")) {
				Location l = g.getPlayer().getLocation();
				config.set(base + "red", l);
				a.getCurrent().setBlueSpawn(l);
			}
			else if (type.contains("spawn")) {
				Location l = g.getPlayer().getLocation();
				config.set(base + "spawn", l);
				a.getCurrent().setSpawn(l);
			}
			else if (type.contains("blue")) {
				Location l = g.getPlayer().getLocation();
				config.set(base + "blue", l);
				a.getCurrent().setBlueSpawn(l);
			}
			else {
				Chat.player(sender, "&cUnknown setting, " + args[0]);
				return true;
			}
		}
		
		if (a != null) {
			Chat.player(sender, "&aChanged setting " + type + " in " + a.getCurrent().getTitle());
		}
		else {
			Chat.player(sender, "&aChanged setting " + type);
		}
		save();
		return true;
	}

	@Override
	public boolean hasPermission(Gamer g) {
		if (g.getRank().getLevel() < RankType.ADMIN.getLevel())
			return false;
		return true;
	}
	
	public void save() {
		Walls.getInstance().saveConfig();
	}
}
