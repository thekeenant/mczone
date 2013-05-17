package co.mczone.ghost.cmds;

import lombok.Getter;

import org.bukkit.World;
import org.bukkit.command.CommandSender;

import co.mczone.api.commands.SubCommand;
import co.mczone.api.players.Gamer;
import co.mczone.api.players.Permissible;
import co.mczone.api.players.RankType;
import co.mczone.util.Chat;

public class GhostSaveCmd implements SubCommand,Permissible {
	@Getter String about = "Toggle map edit mode";
	
	@Override
	public boolean execute(CommandSender sender, String[] args) {
		Gamer g = Gamer.get(sender.getName());
		
		World w = g.getPlayer().getWorld();
		w.save();
		
		Chat.player(sender, "&eYou have saved the world!");
		return false;
	}

	@Override
	public boolean hasPermission(Gamer g) {
		if (g.getRank().getLevel() < RankType.ADMIN.getLevel())
			return false;
		return true;
	}
}
