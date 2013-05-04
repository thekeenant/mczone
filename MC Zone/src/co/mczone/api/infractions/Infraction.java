package co.mczone.api.infractions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import lombok.Getter;

public class Infraction {
    public static SimpleDateFormat human = new SimpleDateFormat("MM/dd/yyyy HH:mm z");
    
	@Getter static HashMap<Infraction, String> list = new HashMap<Infraction, String>();
	@Getter String username;
	@Getter Date date;
	@Getter String reason;
	
	public String forHumanDate() {
		return human.format(date);
	}
	
	public String getKickMessage() {
		return "";
	}
}
