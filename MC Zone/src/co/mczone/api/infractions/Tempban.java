package co.mczone.api.infractions;

import java.util.Date;

import lombok.Getter;

public class Tempban extends Infraction {
	@Getter Date expires;
	public Tempban(String username, String reason, Date date, Date expires) {
		this.username = username;
		this.reason = reason;
		this.date = date;
		this.expires = expires;
		list.put(this, username);
	}
	
	public String forHumanExpireTime() {
		return human.format(expires);
	}
}
