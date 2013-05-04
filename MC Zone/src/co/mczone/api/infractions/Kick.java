package co.mczone.api.infractions;

import java.util.Date;

public class Kick extends Infraction {
	public Kick(String username, String reason, Date date) {
		this.date = date;
		this.reason = reason;
		list.put(this, username);
	}
}
