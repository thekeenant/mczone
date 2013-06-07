package co.mczone.api.modules;

import java.util.Set;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import co.mczone.util.Chat;

public class Sidebar {
	
	@Getter String title;
	
	@Getter Scoreboard scoreboard;
	@Getter Objective objective;
	
	public Sidebar(String title) {
		this.title = title;
		
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		scoreboard.getObjective(DisplaySlot.SIDEBAR);
		objective = scoreboard.registerNewObjective("test", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(Chat.colors(title));
	}
	
	public Sidebar(String title, Scoreboard scoreboard) {
		this.title = title;
		this.scoreboard = scoreboard;
		
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
	
	public void resetScores() {
		Set<OfflinePlayer> players = scoreboard.getPlayers();
		
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		scoreboard.getObjective(DisplaySlot.SIDEBAR);
		objective = scoreboard.registerNewObjective("test", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(Chat.colors(title));
		
		for (OfflinePlayer p : players)
			p.getPlayer().setScoreboard(scoreboard);
	}

	public void hide() {
		for (OfflinePlayer op : scoreboard.getPlayers()) {
			Player p = op.getPlayer();
			if (p != null && p.isOnline())
				p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		}
	}
}
