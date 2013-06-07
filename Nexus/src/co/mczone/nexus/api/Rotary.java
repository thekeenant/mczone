package co.mczone.nexus.api;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import co.mczone.api.modules.Sidebar;
import co.mczone.api.players.Gamer;
import co.mczone.nexus.GameSchedule;
import co.mczone.nexus.Nexus;
import co.mczone.nexus.enums.GameState;
import co.mczone.util.Chat;

public class Rotary {

	@Getter List<Map> maps = new ArrayList<Map>();
	
	@Getter @Setter GameState state;
	@Getter @Setter int time;

	@Getter @Setter GameSchedule schedule;
	
	@Getter Sidebar sidebar;
	
	public Rotary() {
		this.sidebar = new Sidebar("&eKills");
		this.state = GameState.STARTING;
		this.time = 0;
	}
	
	public void updateSidebar() {
		for (Team team : getCurrentMap().getTeams())
			sidebar.set(team.getColor().getChatColor() + team.getTitle(), team.getKills());
	}
	
	public void start() {
		// Load the match world
		loadNext();
		
		// Countdown until 0 to start the match
		time = 30;
		
		schedule = new GameSchedule();
		schedule.runTaskTimerAsynchronously(Nexus.getPlugin(), 0, 20);
		
	}
	
	public Map getCurrentMap() {
		return maps.get(currentMapIndex);
	}

	int currentMapIndex = -1;
	public void loadNext() {
		Map next = getNextMap();
		next.loadMatch();
	}
	
	public Map getNextMap() {
		currentMapIndex += 1;
		try {
			return maps.get(currentMapIndex);
		}
		catch (IndexOutOfBoundsException e) {
			return maps.get(currentMapIndex);
		}
	}

	public void nextMatch() {
		this.getSidebar().resetScores();
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
		Chat.server("&aThe match has &2STARTED. &aPrepare for battle!");
		updateSidebar();
		
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
		sidebar.resetScores();
		setState(GameState.END);
		setTime(30);
	}	
	
}
