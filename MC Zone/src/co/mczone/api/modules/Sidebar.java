package co.mczone.api.modules;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import co.mczone.util.Chat;

public class Sidebar {
	@Getter Scoreboard scoreboard;
	@Getter Objective objective;
	
	public Sidebar(String title) {
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		scoreboard.getObjective(DisplaySlot.SIDEBAR);
		objective = scoreboard.registerNewObjective("test", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(Chat.colors(title));
	}
	
	public void set(String key, int value) {
		Score score = objective.getScore(Bukkit.getOfflinePlayer(Chat.colors(key)));
		score.setScore(value);
	}
	
	public void add(Player p) {
		p.setScoreboard(scoreboard);
	}
	
	public void clearScore(String key) {
		getScoreboard().resetScores(Bukkit.getOfflinePlayer(Chat.colors(key)));
	}
}
