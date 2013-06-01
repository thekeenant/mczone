package co.mczone.walls.timers;

import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;

import co.mczone.walls.Config;
import co.mczone.walls.Walls;
import co.mczone.walls.utils.Chat;

public class PrepTask extends TimerTask {
    public static Timer TIMER = new Timer();
	public static int TIME = Config.getInt("prep-time");
	
	@Override
	public void run() {
		TIME -= 1;		
		
		if (TIME == 0) {
	        Bukkit.getScheduler().scheduleSyncDelayedTask(Walls.instance, new Runnable() {
	            @Override
	            public void run() {
	                Walls.pvp();
	            }
	        });
		    return;
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Walls.instance, new Runnable() {
            @Override
            public void run() {
                Walls.checkWinner();
            }
        });
		final int t = TIME;
		if (TIME % 60 == 0 || TIME <= 10)
			Chat.server("&eWalls dropping in &4" + Chat.time(t) + "&e!");	
	}
}
