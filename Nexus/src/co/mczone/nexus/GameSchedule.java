package co.mczone.nexus;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.nexus.api.Map;
import co.mczone.nexus.api.Rotary;
import co.mczone.nexus.api.Team;
import co.mczone.nexus.enums.GameState;
import co.mczone.util.Chat;

public class GameSchedule extends BukkitRunnable {

	@Override
	public void run() {
		
		Rotary rotary = Nexus.getRotary();
		
		GameState state = rotary.getState();
		Map map = rotary.getCurrentMap();
		
		if (state == GameState.STARTING) {
			rotary.addTime(-1);
			
			if (rotary.getTime() == 0) {
				rotary.startMatch();
				return;
			}
			
			if (rotary.getTime() % 5 == 0 || rotary.getTime() <= 10)
				Chat.server("&6Match on &e" + map.getTitle() + " &6starting in &e" + Chat.time(rotary.getTime()));
		}
		
		else if (state == GameState.PLAYING) {
			rotary.addTime(1);
			
			if (rotary.getTime() >= map.getTimeLimit()) {
				rotary.endMatch();
				return;
			}
			
			if (rotary.getTime() % 120 == 0) {
				String prefix = "&fScore: ";
				String scores = "";
				for (Team team : map.getTeams()) {
					int kills = team.getKills();
					ChatColor color = team.getColor().getChatColor();
					scores += color + "&l" + kills + "  &7/  ";
				}
				scores = Chat.chomp(scores, 7);
				
				int time = map.getTimeLimit() - rotary.getTime();
				Chat.server(prefix + scores);
				Chat.server("&7Time left: " + Chat.time(time));
			}
		}
		
		else if (state == GameState.END) {
			rotary.addTime(-1);
			
			if (rotary.getTime() == 0) {
				rotary.nextMatch();
				return;
			}
			
			if (rotary.getTime() % 5 == 0 || rotary.getTime() <= 10)
				Chat.server("&6Next match on &e" + map.getTitle() + " &6loading in &e" + Chat.time(rotary.getTime()));
		}
	}

}
