package co.mczone.skywars.cmds;

import lombok.Getter;

import org.bukkit.World;
import org.bukkit.command.CommandSender;

import co.mczone.api.players.Gamer;
import co.mczone.cmds.SubCommand;
import co.mczone.util.Chat;

public class SkyWarsSaveCmd implements SubCommand {
	@Getter String about = "Toggle map edit mode";
	
	@Override
	public boolean execute(CommandSender sender, String[] args) {
		Gamer g = Gamer.get(sender.getName());
		
		World w = g.getPlayer().getWorld();
		w.save();
		
		Chat.player(sender, "&eYou have saved the world!");
		return false;
	}
}
