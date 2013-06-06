package co.mczone.nexus.api;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import co.mczone.api.modules.Coordinate;
import co.mczone.api.players.Gamer;
import co.mczone.nexus.enums.TeamColor;

public class Team {

	@Getter TeamColor color;
	
	@Getter Coordinate spawn;
	
	@Getter List<Gamer> members = new ArrayList<Gamer>();
	
	@Getter int kills;
	
	public Team(TeamColor color, Coordinate spawn) {
		this.color = color;
		this.spawn = spawn;
	}
	
	public void join(Gamer g) {
		join(g, true);
	}
	
	public void join(Gamer g, boolean teleport) {
		members.add(g);
	}
	
}
