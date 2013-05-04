package co.mczone.api.infractions;

import java.util.Date;

import co.mczone.util.Chat;

public class Kick extends Infraction {
	public Kick(String username, String reason, Date date) {
		this.date = date;
		this.reason = reason;
		list.put(this, username);
	}

	@Override
	public String getKickMessage() {
		String msg = "";
		msg += "&4[Kick]\n\n";
		msg += "&cDate: &e" + forHumanDate() + "\n";
		msg += "&cReason: &f" + reason;
		return Chat.colors(msg);
	}
}
