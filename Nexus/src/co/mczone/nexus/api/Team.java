package co.mczone.nexus.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import lombok.Getter;
import lombok.Setter;
import co.mczone.api.modules.Coordinate;
import co.mczone.api.players.Gamer;
import co.mczone.nexus.Nexus;
import co.mczone.nexus.enums.GameState;
import co.mczone.nexus.enums.TeamColor;

public class Team {

	@Getter String title;
	@Getter TeamColor color;
	@Getter Coordinate spawn;
	
	@Getter List<Gamer> members = new ArrayList<Gamer>();
	
	@Getter int kills;
	
	@Getter @Setter org.bukkit.scoreboard.Team team;
	
	public Team(String title, TeamColor color, Coordinate spawn) {
		this.title = title;
		this.color = color;
		this.spawn = spawn;
	}
	
	public void join(Gamer g) {
		team.addPlayer(g.getPlayer());
		members.add(g);
		g.setVariable("spectator", null);
		
		if (Nexus.getRotary().getState() == GameState.PLAYING) {
			g.teleport(getSpawnLocation());
			g.setFlying(false);
			g.setAllowFlight(false);
			g.getPlayer().setFallDistance(0.0F);
			g.run("give-kit");
		}
		else {
			
		}
	}
	
	public void remove(Gamer g) {
		team.removePlayer(g.getPlayer());
		members.remove(g);
	}
	
	public Location getSpawnLocation() {
		Location spawn = this.spawn.getLocation(Nexus.getRotary().getCurrentMap().getWorld());
		return spawn;
	}
	
}
