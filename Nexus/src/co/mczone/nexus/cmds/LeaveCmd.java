package co.mczone.nexus.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.mczone.api.players.Gamer;
import co.mczone.api.players.RankType;
import co.mczone.nexus.Nexus;
import co.mczone.nexus.api.Map;
import co.mczone.nexus.api.Team;
import co.mczone.nexus.enums.TeamColor;
import co.mczone.util.Chat;

public class LeaveCmd implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Gamer g = Gamer.get(sender);
		
		boolean random = true;
		if (args.length > 0)
			random = false;
		
		if (random == true && g.getRank().getLevel() < RankType.VIP.getLevel()) {
			Chat.player(g, "&cGet VIP or higher in order to choose the team you join!");
			return true;
		}
		
		Map map = Nexus.getRotary().getCurrentMap();
		Team team = null;
		if (random == false) {
			TeamColor color = null;
			try {
				color = TeamColor.valueOf(args[0].toUpperCase());
			}
			catch (IllegalArgumentException e) {
				Chat.player(g, "&cUnknown color: " + args[0]);
				return true;
			}
			
			team = map.getTeam(color);
			if (team == null) {
				String choices = "";
				for (Team t : map.getTeams())
					choices += t.getColor().name().toLowerCase() + ", ";
				choices = Chat.chomp(choices, 2);
				
				Chat.player(g, "&7Unknown team: &f" + args[0] + "&7. Choices: &f" + choices);
				return true;
			}
		}
		else {
			Team result = map.getTeams().get(0);
			for (Team t : map.getTeams()) {
				if (t.getMembers().size() < result.getMembers().size())
					result = t;
			}
			team = result;
		}
		
		team.join(g);
		Chat.player(g, "&7You have joined " + team.getColor().getChatColor() + team.getTitle());
		return true;		
	}

}
