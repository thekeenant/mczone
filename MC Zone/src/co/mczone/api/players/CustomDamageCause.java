package co.mczone.api.players;

import lombok.Getter;

public enum CustomDamageCause {
	TNT_MINE("%t was blown up by %p"),
	TNT_GRENADE("%t was blown up by %p");
	
	@Getter String deathMessage;
	private CustomDamageCause(String dMessage) {
		deathMessage = dMessage;
	}
}
