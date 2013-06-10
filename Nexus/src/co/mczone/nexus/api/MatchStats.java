package co.mczone.nexus.api;

import java.util.HashMap;

import co.mczone.api.players.Gamer;

public class MatchStats {

	HashMap<String, Integer> kills = new HashMap<String, Integer>();
	HashMap<String, Integer> deaths = new HashMap<String, Integer>();
	HashMap<String, Integer> killstreak = new HashMap<String, Integer>();
	
	public MatchStats() {
		
	}
	
	public void reset() {
		kills.clear();
		deaths.clear();
	}
	
	public void resetKillstreaks() {
		killstreak.clear();
	}
	
	public void resetKillstreak(Gamer g) {
		killstreak.put(g.getName(), 0);
	}
	
	public int getKillstreak(Gamer g) {
		return killstreak.containsKey(g.getName()) ? 0 : killstreak.get(g.getName());
	}
	
	public void addKill(Gamer g) {
		if (!kills.containsKey(g.getName()))
			kills.put(g.getName(), 0);
		
		if (!killstreak.containsKey(g.getName()))
			killstreak.put(g.getName(), 0);

		kills.put(g.getName(), kills.get(g.getName()) + 1);
		killstreak.put(g.getName(), killstreak.get(g.getName()) + 1);
	}
	
	public void addDeath(Gamer g) {
		if (!deaths.containsKey(g.getName()))
			deaths.put(g.getName(), 0);
		
		deaths.put(g.getName(), deaths.get(g.getName()) + 1);
		resetKillstreak(g);
	}

	public int getKills(Gamer g) {
		return kills.containsKey(g.getName()) ? 0 : kills.get(g.getName());
	}
	
	public int getDeaths(Gamer g) {
		return deaths.containsKey(g.getName()) ? 0 : deaths.get(g.getName());
	}
	
	public double getKD(Gamer g) {
		double kills = getKills(g);
		double deaths = getDeaths(g);
		
		if (deaths == 0)
			deaths = 1;
		
		return kills / deaths;
	}
	
}
