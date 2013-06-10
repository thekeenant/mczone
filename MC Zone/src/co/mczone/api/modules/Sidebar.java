package co.mczone.api.modules;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import co.mczone.util.Chat;
import co.mczone.util.RandomUtil;

public class Sidebar {
	
	@Getter String title;
	
	@Getter Scoreboard scoreboard;
	@Getter Objective objective;
	
	public Sidebar(String title) {
		this.title = title;
		
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		
		objective = scoreboard.registerNewObjective("test" + RandomUtil.between(1, 500), "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(Chat.colors(title));
	}
	
	public Sidebar(String title, Scoreboard scoreboard) {
		this.title = title;
		this.scoreboard = scoreboard;
		
		objective = scoreboard.registerNewObjective("test" + RandomUtil.between(1, 500), "dummy");
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
	
	public void resetScores() {
		for (OfflinePlayer score : scoreboard.getPlayers())
			objective.getScoreboard().resetScores(score);
	}
}
