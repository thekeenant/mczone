package co.mczone.walls.timers;

import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;

import co.mczone.walls.Config;
import co.mczone.walls.Kit;
import co.mczone.walls.Team;
import co.mczone.walls.Walls;
import co.mczone.walls.utils.Chat;

public class PreTask extends TimerTask {
    public static Timer TIMER = new Timer();
    public static int TIME = Config.getInt("pre-time") - 1;
    public static int TOTAL_TIME = 0;
	
	@Override
	public void run() {
		if (TIME % 20 == 0) {
	        Kit.load();
		}
		
		TOTAL_TIME += 1;
		if (Bukkit.getOnlinePlayers().length > 1)
		    TIME -= 1;
		
		if (TOTAL_TIME > 900 && Bukkit.getOnlinePlayers().length <= 3) {
		    Bukkit.shutdown();
		}
		
		if (Bukkit.getOnlinePlayers().length==Bukkit.getMaxPlayers() && TIME >= 15) {
			TIME = 10;
		}
		
		if (TIME==0) {
			boolean start = true;
			for (Team team : Team.list) {
				if (team.getMembers().size() <= 2) {
					start = false;
					break;
				}
			}
			if (start) {
			    Bukkit.getScheduler().scheduleSyncDelayedTask(Walls.instance, new Runnable() {
                    @Override
                    public void run() {
                        Walls.prep();
                    }
			    });
			}
			else {
				Chat.server("&cWe need 2 players per team in order to start the game!");
				TIME = Config.getInt("pre-time");
				return;
			}
			return;
		}
		
		if (TIME % 20 == 0 || TIME <= 10)
			Chat.server("&eGame beginning in &4" + Chat.time(TIME) + "&e!");	
	}
}
