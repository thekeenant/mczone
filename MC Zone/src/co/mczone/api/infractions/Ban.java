package co.mczone.api.infractions;

import java.util.Date;

public class Ban extends Infraction {
	public Ban(String username, String reason, Date date) {
		this.date = date;
		this.reason = reason;
		list.put(this, username);
	}
}
