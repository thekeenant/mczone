package co.mczone.ghost.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import lombok.Getter;

public class Match {
	@Getter int id;
	@Getter Scoreboard scoreboard;
	@Getter Objective sidebar;

	@Getter Team red;
	@Getter Team blue;
	@Getter Team spec;
	
	public Match(int id) {
		this.id = id;
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

		red = scoreboard.registerNewTeam("red");
		blue = scoreboard.registerNewTeam("blue");
		spec = scoreboard.registerNewTeam("spec");

		
		// Apparently, opactiy doesn't work with friendly fire >:(
		red.setDisplayName("Red");
		//red.setAllowFriendlyFire(false);
		red.setCanSeeFriendlyInvisibles(true);
		red.setPrefix(ChatColor.RED + "");

		blue.setDisplayName("Blue");
		//blue.setAllowFriendlyFire(false);
		blue.setCanSeeFriendlyInvisibles(true);
		blue.setPrefix(ChatColor.BLUE + "");

		spec.setDisplayName("Spectators");
		//spec.setAllowFriendlyFire(false);
		spec.setCanSeeFriendlyInvisibles(true);
		spec.setPrefix(ChatColor.GRAY + "");

		sidebar = scoreboard.registerNewObjective("test", "dummy");
		sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
		sidebar.setDisplayName(ChatColor.GREEN + "Sidebar");
	}
	
	public void setScore(String key, int value) {
		Score score = sidebar.getScore(Bukkit.getOfflinePlayer(key));
		score.setScore(value);
	}
	
	public List<Player> getPlayers() {
		List<Player> list = new ArrayList<Player>();
		for (Team team : scoreboard.getTeams())
			for (OfflinePlayer p : team.getPlayers())
				if (p.isOnline())
					list.add(p.getPlayer());
		return list;
	}
	
	public void join(Player p, String team) {
		if (team.equals("red"))
			red.addPlayer(p);
		else if (team.equals("blue"))
			blue.addPlayer(p);
		else
			spec.addPlayer(p);
		
		p.setScoreboard(scoreboard);
	}

	public void leave(Player p, String team) {
		scoreboard.getTeam(team).removePlayer(p);
		p.setScoreboard(null);
	}
}
