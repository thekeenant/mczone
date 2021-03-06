package co.mczone.walls.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.mczone.api.players.Gamer;
import co.mczone.api.players.Permissible;
import co.mczone.api.players.RankType;
import co.mczone.util.Chat;
import co.mczone.walls.Walls;
import co.mczone.walls.api.Arena;

public class LeaveCmd implements CommandExecutor,Permissible {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Gamer g = Gamer.get(sender);
		
		if (g.getVariable("arena") == null) {
			Chat.player(sender, "&cYou are not in an arena!");
			return true;
		}
		
		Arena a = (Arena) g.getVariable("arena");
		a.leave(g.getPlayer());
    	g.teleport(Walls.getLobby().getSpawn());
		Chat.player(sender, "&aYou have left your current match.");
		return true;
	}

	@Override
	public boolean hasPermission(Gamer g) {
		if (g.getRank().getType() == RankType.CONSOLE)
			return false;
		return true;
	}
	
}
