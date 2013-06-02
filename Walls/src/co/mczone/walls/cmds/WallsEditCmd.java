package co.mczone.walls.cmds;

import lombok.Getter;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;

import co.mczone.api.commands.SubCommand;
import co.mczone.api.players.Gamer;
import co.mczone.util.Chat;

public class WallsEditCmd implements SubCommand {
	@Getter String about = "Toggle map edit mode";
	
	@Override
	public boolean execute(CommandSender sender, String[] args) {
		Gamer g = Gamer.get(sender.getName());
		if (g.getVariable("edit") == null)
			g.setVariable("edit", true);
		else
			g.setVariable("edit", ! (boolean) g.getVariable("edit"));
		
		if ((boolean) g.getVariable("edit") == true) {
			g.getPlayer().setGameMode(GameMode.CREATIVE);
			Chat.player(sender, "&eYou have entered map edit mode. Remember to /walls save");
		}
		else {
			g.getPlayer().setGameMode(GameMode.SURVIVAL);
			Chat.player(sender, "&eYou have left map edit mode. Remember to /walls save");
		}
		return false;
	}
}
