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

import co.mczone.util.Chat;
import co.mczone.util.RandomUtil;

import lombok.Getter;

public class Match {
	@Getter int id;
	@Getter Scoreboard scoreboard;
	@Getter Objective sidebar;
	
	public Match(int id) {
		this.id = id;
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

		scoreboard.registerNewTeam("red");
		scoreboard.registerNewTeam("blue");
		scoreboard.registerNewTeam("spec");

		scoreboard.getTeam("red").setAllowFriendlyFire(false);
		scoreboard.getTeam("red").setCanSeeFriendlyInvisibles(true);
		scoreboard.getTeam("red").setPrefix(ChatColor.RED + "");
		
		scoreboard.getTeam("blue").setAllowFriendlyFire(false);
		scoreboard.getTeam("blue").setCanSeeFriendlyInvisibles(true);
		scoreboard.getTeam("blue").setPrefix(ChatColor.BLUE + "");

		scoreboard.getTeam("spec").setAllowFriendlyFire(false);
		scoreboard.getTeam("spec").setCanSeeFriendlyInvisibles(false);
		scoreboard.getTeam("spec").setPrefix(ChatColor.GRAY + "");

		sidebar = scoreboard.registerNewObjective("test", "dummy");
		sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
		sidebar.setDisplayName(ChatColor.GREEN + "Levels");
		
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
		scoreboard.getTeam(team).addPlayer(p);
		p.setScoreboard(scoreboard);

		this.setScore(p.getName(), RandomUtil.between(20, 100));
		
		Chat.server("&aTeam: " + scoreboard.getTeam(team).getPlayers().toString());
	}

	public void leave(Player p, String team) {
		scoreboard.getTeam(team).removePlayer(p);
		p.setScoreboard(null);
	}
}
