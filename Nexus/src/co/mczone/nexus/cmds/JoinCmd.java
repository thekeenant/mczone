package co.mczone.nexus.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.mczone.api.players.Gamer;
import co.mczone.nexus.Nexus;
import co.mczone.nexus.api.Team;
import co.mczone.util.Chat;

public class JoinCmd implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Gamer g = Gamer.get(sender);
		
		Team team = Nexus.getRotary().getCurrentMap().getTeams().get(0);
		team.join(g);
		

		Chat.player(g, "&7You have joined " + team.getColor().getChatColor() + team.getTitle());
		return true;
		
		
	}

}
