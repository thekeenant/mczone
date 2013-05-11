package co.mczone.ghost.schedules;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.ghost.Ghost;
import co.mczone.ghost.api.Arena;
import co.mczone.ghost.api.ArenaState;
import co.mczone.util.Chat;

public class ArenaSchedule extends BukkitRunnable {
	public static int TIME_LIMIT = Ghost.getConf().getInt("time-limit", 60 * 10);
	@Getter @Setter int time = 0;
	@Getter @Setter Arena match;
	
	@Getter @Setter int countdown = Ghost.getConf().getInt("countdown", 60 * 2);
	
	public ArenaSchedule(Arena match) {
		this.match = match;
	}
	
	@Override
	public void run() {
		time += 1;

		final Arena a = match;
		match.updateScoreboard();
		
		if (time % 3 == 0) {
			match.updateSign();
			
			if (match.getWorld() != null) {
				new BukkitRunnable() {
					@Override
					public void run() {
						a.getWorld().setTime(16000);
					}
				}.runTask(Ghost.getInstance());
			}
		}
		
		if (match.getState() == ArenaState.WAITING) {
			countdown -= 1;
			boolean start = false;
			if (match.getRedPlayers().size() >= Arena.MAX_PER_TEAM && match.getBluePlayers().size() >= Arena.MAX_PER_TEAM) 
				start = true;
			else if (countdown == 10) {
				if (match.getRedPlayers().size() < 2 || match.getBluePlayers().size() < 2) {
					a.sendMessage("&cNeed at least 2 players per team to start the match!");
					resetCountdown();
					return;
				}
				start = true;
			}
			
			
			if (start) {
				if (a.isStarting())
					return;
				a.setStarting(true);
				
				// Countdown to start
				new BukkitRunnable() {
					int time = 10;
					@Override
					public void run() {
						if (time == 0) {
							a.startGame();
							cancel();
							return;
						}
						a.sendMessage("&aMatch starting in &4" + Chat.time(time));
						time--;
					}
					
				}.runTaskTimerAsynchronously(Ghost.getInstance(), 0, 20);
				return;
			}
			if (time % 30 == 0) {
				int more = (Arena.MAX_PER_TEAM * 2) - match.getPlayers().size();
				//match.sendMessage("&eWaiting for &4&l" + more + " players &eto join in order to start the match.");
			}
		}
		else if (match.getState() == ArenaState.STARTED) {
			boolean broadcast = false;
			if (time == TIME_LIMIT - 300)
				broadcast = true;
			else if (time == TIME_LIMIT - 120)
				broadcast = true;
			else if (time == TIME_LIMIT - 60)
				broadcast = true;
			else if (time >= TIME_LIMIT - 10 && time != TIME_LIMIT)
				broadcast = true;
			
			if (broadcast)
				Chat.server("&7Game ending automatically in &f" + Chat.time(TIME_LIMIT - time) + "&7");
				
			// Game over?
			if (time == TIME_LIMIT || match.getBluePlayers().size() == 0 || match.getRedPlayers().size() == 0)
				match.endGame();
		}
	}

	public void resetCountdown() {
		countdown = Ghost.getConf().getInt("countdown", 60 * 2);
	}
	
}
