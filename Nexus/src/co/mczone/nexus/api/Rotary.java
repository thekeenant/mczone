package co.mczone.nexus.api;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import co.mczone.api.players.Gamer;
import co.mczone.nexus.GameSchedule;
import co.mczone.nexus.Nexus;
import co.mczone.nexus.enums.GameState;

public class Rotary {

	@Getter List<Map> maps = new ArrayList<Map>();
	
	@Getter @Setter GameState state;
	@Getter @Setter int time;

	@Getter @Setter GameSchedule schedule;
	
	public Rotary() {
		this.state = GameState.STARTING;
		this.time = 0;
	}
	
	public void start() {
		// Load the match world
		loadNext();
		
		// Countdown until 0 to start the match
		time = 30;
		
		schedule = new GameSchedule();
		schedule.runTaskTimerAsynchronously(Nexus.getPlugin(), 0, 20);
		
	}
	
	public void addTime(int i) {
		if (i > 0)
			time += i;
		else
			time -= i;
	}
	
	public Map getCurrentMap() {
		return maps.get(currentMapIndex);
	}

	int currentMapIndex = 0;
	public void loadNext() {
		currentMapIndex += 1;
		Map next = getNextMap();
		next.loadMatch();
	}
	
	public Map getNextMap() {
		int next = currentMapIndex + 1;
		try {
			return maps.get(next);
		}
		catch (IndexOutOfBoundsException e) {
			return maps.get(next);
		}
	}

	public void nextMatch() {
		setState(GameState.STARTING);
		loadNext();
		
		// Reset time to 30 seconds to countdown to match
		setTime(30);
		
		for (Gamer g : Gamer.getList()) {
			g.clearInventory(true);
			g.setHealth(20);
			g.setFoodLevel(20);
			g.setSaturation(99F);
			g.removePotionEffects();
			
			g.teleport(getCurrentMap().getSpawn().getLocation(getCurrentMap().getWorld()));
			
			g.setAllowFlight(true);
			g.setFlying(true);
		}
	}
	
	public void startMatch() {
		setState(GameState.PLAYING);
		
		// Reset time, will count up
		setTime(0);
		
		for (Team team : getCurrentMap().getTeams()) {
			for (Gamer g : team.getMembers()) {	
				g.teleport(team.getSpawn());
				
				g.setFlying(false);
				g.setAllowFlight(false);
				g.getPlayer().setFallDistance(0F);
			}
		}
	}
	
	public void endMatch() {
		setState(GameState.END);
		setTime(30);
	}	
	
}
