package co.mczone.nexus.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.mczone.api.players.Gamer;
import co.mczone.nexus.Nexus;
import co.mczone.nexus.api.MatchStats;
import co.mczone.util.Chat;

public class MatchCmd implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Gamer g = Gamer.get(sender);
		
		MatchStats stats = Nexus.getMatchStats();
		int kills = stats.getKills(g);
		int deaths = stats.getDeaths(g);
		int killstreak = stats.getKillstreak(g);
		double kd = stats.getKD(g);
		
		String line1 = "&fMatch: &cTDM &7on &a" + Nexus.getRotary().getCurrentMap().getTitle();
		String line2 = "&fKD: " + (kd >= 1 ? ChatColor.GREEN : ChatColor.RED) + kd;
		String line3 = "&fKillstreak: &f" + killstreak;
		String line4 = "&fKills: &f" + kills;
		String line5 = "&fDeaths: &f" + deaths;

		Chat.player(g, line1 + "\n" + line2 + "\n" + line3 + "\n" + line4 + "\n" + line5);
		return true;		
	}

}
