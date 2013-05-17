package co.mczone.api.players;

import java.util.Date;
import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

public class Rank {
	@Getter static HashMap<String, Rank> ranks = new HashMap<String, Rank>();
	@Getter @Setter boolean cancelled = false;
	@Getter @Setter Date expireDate = null;
	@Getter @Setter RankType type;
	
	public Rank(RankType type) {
		this.type = type;
	}
	
	public static Rank getRank(String name) {
		if (name.equals("CONSOLE"))
			return new Rank(RankType.CONSOLE);
		if (!ranks.containsKey(name))
			return new Rank(RankType.USER);
		
		return ranks.get(name);
	}
	
	public int getLevel() {
		return type.getLevel();
	}
	
	public String getPrefix() {
		return type.getPrefix();
	}
}
