package co.mczone.api.players;

import java.util.Date;
import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

public class Rank {
	@Getter static HashMap<String, Rank> ranks = new HashMap<String, Rank>();
	@Getter @Setter boolean expires = false;
	@Getter @Setter Date expireDate = null;
	@Getter @Setter RankType type;
	
	public Rank(RankType type) {
		this.type = type;
	}
}
