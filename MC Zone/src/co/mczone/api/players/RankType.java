package co.mczone.api.players;

import lombok.Getter;

public enum RankType {
	USER(0, "", ""),
	VIP(1, "VIP", "&6[VIP]&f "),
	ELITE(2, "Elite", "&a[Elite]&f "),
	TITAN(3, "Titan", "&b[Titan]&f "),
	LEGEND(4, "Titan", "&b[Titan]&f "),
	
	MOD(5, "Mod", "&c[Mod]&f "),
	OFFICER(6, "Officer", "&1[Officer]&f "),
	ADMIN(7, "Admin", "&4[Admin]&f "),
	OWNER(8, "Owner", "&2[Owner]&f "),
	CONSOLE(500, "Console", "&4[Console]&f ");
	
	@Getter int level;
	@Getter String title;
	@Getter String prefix;
	private RankType(int rank, String title, String prefix) {
		this.level = rank;
		this.title = title;
		this.prefix = prefix;
	}
}
