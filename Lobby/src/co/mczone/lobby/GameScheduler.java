package co.mczone.lobby;

import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.lobby.api.Game;

public class GameScheduler extends BukkitRunnable {
	int time = 0;
	public void run() {
		time += 1;
		if (time % 5 == 0) {
			new BukkitRunnable() {
				public void run() {
					for (Game game : Game.getList()) {
						game.update();
					}
				}
			}.runTaskAsynchronously(Lobby.getInstance());
		}
	}
	
}
