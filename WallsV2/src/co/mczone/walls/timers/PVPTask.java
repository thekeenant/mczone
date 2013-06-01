package co.mczone.walls.timers;

import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;

import co.mczone.walls.Walls;
import co.mczone.walls.utils.Chat;

public class PVPTask extends TimerTask {
    public static Timer TIMER = new Timer();
	public static int TIME = 0;
	
	@Override
	public void run() {
		TIME += 1;

        Bukkit.getScheduler().scheduleSyncDelayedTask(Walls.instance, new Runnable() {
            @Override
            public void run() {
                Walls.checkWinner();
            }
        });
		
		int limit = 2700;
		if (TIME == limit - 300) {
		    Chat.server("&cGame ending in 5 minutes!");
		    return;
		}
		if (TIME == limit - 120) {
		    Chat.server("&cGame ending in 2 minutes!");
		    return;
		}
		if (TIME == limit - 60) {
		    Chat.server("&cGame ending in 1 minute!");
		    return;
		}
		if (TIME >= limit - 10) {
			Chat.server("&cGame ending in " + Chat.time(2700 - TIME) + "!");
		}
		if (TIME == limit || Bukkit.getOnlinePlayers().length == 0) {
			Bukkit.shutdown();
		}
		
	}
}
