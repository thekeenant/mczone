package co.mczone.api.infractions;

import java.util.Date;

import co.mczone.util.Chat;

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
	
	@Override
	public String getKickMessage() {
		String msg = "";
		msg += "&4[Tempban]\n\n";
		msg += "&cDate: &e" + forHumanDate() + "\n";
		msg += "&cExpires: &e" + forHumanExpireTime() + "\n";
		msg += "&cReason: &f" + reason;
		msg += "\n\n\n";
		msg += "&7Email &oinfo@mczone.co &7to appeal.";
		return Chat.colors(msg);
	}
}
