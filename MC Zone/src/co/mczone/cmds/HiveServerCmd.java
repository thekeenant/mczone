package co.mczone.cmds;

import org.bukkit.command.CommandSender;

import lombok.Getter;

import co.mczone.api.commands.SubCommand;
import co.mczone.api.players.Gamer;
import co.mczone.util.Chat;

public class HiveServerCmd implements SubCommand {
	@Getter String about = "Teleport to a Minecraft server";
	
	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if (args.length != 2) {
			Chat.player(sender, "&cUsage: /hive server <ip> <port>");
			return true;
		}
		
		String ip = args[0];
		int port = Integer.parseInt(args[1]);
		
		Gamer g = Gamer.get(sender);
		Chat.player(sender, "&7Directing you to &o" + ip + ":" + port);
		g.sendToServer(ip, port);
		return true;
	}
}
