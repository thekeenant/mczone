package co.mczone.nexus;

import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.nexus.api.Map;
import co.mczone.nexus.api.Rotary;
import co.mczone.nexus.enums.GameState;
import co.mczone.util.Chat;

public class GameSchedule extends BukkitRunnable {

	@Override
	public void run() {
		
		final Rotary rotary = Nexus.getRotary();
		
		GameState state = rotary.getState();
		Map map = rotary.getCurrentMap();
		
		Nexus.getInstance().updateHidden();
		
		if (state == GameState.STARTING) {
			rotary.setTime(rotary.getTime() - 1);
			rotary.updateSidebar();
			
			if (rotary.getTime() == 0) {
				new BukkitRunnable() {

					@Override
					public void run() {
						rotary.startMatch();
					}
					
				}.runTask(Nexus.getPlugin());
				return;
			}
			
			if (rotary.getTime() % 5 == 0 || rotary.getTime() <= 10)
				Chat.server("&6Match on &e" + map.getTitle() + " &6starting in &e" + Chat.time(rotary.getTime()));
		}
		
		else if (state == GameState.PLAYING) {
			rotary.setTime(rotary.getTime() + 1);
			rotary.updateSidebar();
			
			if (rotary.getTime() >= map.getDuration()) {
				rotary.endMatch();
				return;
			}
			
			int left = map.getDuration() - rotary.getTime();
			if (left <= 30 && (left % 5 == 0 || left <= 10)) {
				Chat.server("&cMatch ending in &4" + Chat.time(left));
				return;
			}
			
			if (rotary.getTime() % 60 == 0) {
				/*
				String prefix = "&fScore: ";
				String scores = "";
				for (Team team : map.getTeams()) {
					int kills = team.getKills();
					ChatColor color = team.getColor().getChatColor();
					scores += color + "&l" + kills + " &f/ ";
				}
				scores = Chat.chomp(scores, 5);
				
				Chat.server(prefix + scores);
				*/
				
				int time = map.getDuration() - rotary.getTime();
				Chat.server("&fTime left: &7" + Chat.time(time));
			}
		}
		
		else if (state == GameState.END) {
			rotary.setTime(rotary.getTime() - 1);
			
			Map next = rotary.getNextMap();
			
			if (rotary.getTime() == 0) {
				new BukkitRunnable() {

					@Override
					public void run() {
						rotary.nextMatch();
					}
					
				}.runTask(Nexus.getPlugin());
				return;
			}
			
			if (rotary.getTime() % 5 == 0 || rotary.getTime() <= 10)
				Chat.server("&6Next match on &e" + next.getTitle() + " &6loading in &e" + Chat.time(rotary.getTime()));
		}
	}

}
