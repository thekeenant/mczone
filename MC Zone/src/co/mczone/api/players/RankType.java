package co.mczone.api.players;

import lombok.Getter;

public enum RankType {
	USER(0, "", ""),
	VIP(1, "VIP", "&6[VIP]&f "),
	ELITE(2, "Elite", "&a[Elite]&f "),
	TITAN(3, "Titan", "&b[Titan]&f "),
	MOD(4, "Mod", "&c[Mod]&f "),
	ADMIN(5, "Admin", "&4[Admin]&f ");
	
	@Getter int level;
	@Getter String title;
	@Getter String prefix;
	private RankType(int rank, String title, String prefix) {
		this.level = rank;
		this.title = title;
		this.prefix = prefix;
	}
}
