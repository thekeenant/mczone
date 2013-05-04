package co.mczone.api.infractions;

import java.util.Date;

import co.mczone.util.Chat;

public class Ban extends Infraction {
	public Ban(String username, String reason, Date date) {
		this.date = date;
		this.reason = reason;
		list.put(this, username);
	}
	
	@Override
	public String getKickMessage() {
		String msg = "";
		msg += "&4[Ban]\n\n";
		msg += "&cDate: &e" + forHumanDate() + "\n";
		msg += "&cReason: &f" + reason;
		msg += "\n\n\n";
		msg += "&7Email &oinfo@mczone.co &7to appeal.";
		return Chat.colors(msg);
	}
}
