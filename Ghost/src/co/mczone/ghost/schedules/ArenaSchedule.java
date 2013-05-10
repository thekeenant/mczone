package co.mczone.ghost.schedules;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.ghost.api.Arena;
import co.mczone.ghost.api.ArenaState;
import co.mczone.util.Chat;

public class ArenaSchedule extends BukkitRunnable {
	@Getter @Setter int time = 0;
	@Getter @Setter Arena match;
	
	public ArenaSchedule(Arena match) {
		this.match = match;
	}
	
	@Override
	public void run() {
		time += 1;
		
		match.updateScoreboard();
		
		if (time % 3 == 0)
			match.updateSign();
		
		if (match.getState() == ArenaState.WAITING) {
			if (match.getRedPlayers().size() >= Arena.MAX_PER_TEAM && match.getBluePlayers().size() >= Arena.MAX_PER_TEAM) {
				match.startGame();
				return;
			}
			if (time % 30 == 0) {
				int more = (Arena.MAX_PER_TEAM * 2) - match.getPlayers().size();
				match.sendMessage("&eWaiting for &4" + more + "&e more players to start the match.");
			}
		}
		else if (match.getState() == ArenaState.STARTED) {
			int blue = match.getBluePlayers().size();
			int red = match.getRedPlayers().size();
			if (red == 0) {
	        	Chat.server("&4# # &6Red team has won! &4# #");
			}
			else if (blue == 0) {
	        	Chat.server("&1# # &6Red team has won! &1# #");
			}
			
			if (red == 0 || blue == 0) {
				match.endGame();
			}
		}
	}
	
}
