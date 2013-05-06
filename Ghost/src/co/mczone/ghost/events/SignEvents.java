package co.mczone.ghost.events;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import co.mczone.events.custom.SignClickEvent;
import co.mczone.ghost.Ghost;
import co.mczone.ghost.api.Match;
import co.mczone.ghost.api.TeamColor;
import co.mczone.util.Chat;

public class SignEvents implements Listener {
	public SignEvents() {
		Ghost.getInstance().getServer().getPluginManager().registerEvents(this, Ghost.getInstance());
	}
	
	@EventHandler
	public void onSignClick(SignClickEvent event) {
		Sign sign = event.getSign();
		Player p = event.getPlayer();
		Match match = null;
		for (Match m : Match.getList()) {
			if (m.getSign().getX() == sign.getX() && m.getSign().getY() == sign.getY() && m.getSign().getZ() == sign.getZ()) {
				match = m;
			}
		}
		
		if (match == null)
			return;
		
		match.updateSign();
		
		if (match.getPlayers().size() >= Match.MAX_PER_TEAM * 2) {
			Chat.player(p, "&cThat match is currently full.");
			return;
		}
		
		TeamColor team = match.join(p);
		Chat.player(p, "&aYou have joined team " + team.getColor() + team.name());
	}
}
