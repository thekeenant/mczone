package co.mczone.ghost.schedules;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.ghost.api.Match;
import co.mczone.ghost.api.MatchState;

public class MatchSchedule extends BukkitRunnable {
	@Getter @Setter int time = 0;
	@Getter @Setter Match match;
	
	public MatchSchedule(Match match) {
		this.match = match;
	}
	
	@Override
	public void run() {
		time += 1;
		
		match.updateScoreboard();
		
		if (time % 3 == 0)
			match.updateSign();
		
		if (match.getState() == MatchState.WAITING) {
			if (match.getRedPlayers().size() >= 8 && match.getBluePlayers().size() >= 8) {
				match.startGame();
			}
			if (time % 30 == 0) {
				int more = (Match.MAX_PER_TEAM * 2) - match.getPlayers().size();
				match.sendMessage("&eWaiting for &4" + more + "&e more players to start the match.");
			}
		}
	}
	
}
