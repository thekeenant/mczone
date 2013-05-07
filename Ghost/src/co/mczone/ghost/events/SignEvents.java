package co.mczone.ghost.events;

import java.util.List;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import co.mczone.api.players.Gamer;
import co.mczone.events.custom.SignClickEvent;
import co.mczone.ghost.Ghost;
import co.mczone.ghost.api.Kit;
import co.mczone.ghost.api.Match;
import co.mczone.ghost.api.TeamColor;
import co.mczone.util.Chat;
import co.mczone.util.WorldUtil;

public class SignEvents implements Listener {
	
	public SignEvents() {
		Ghost.getInstance().getServer().getPluginManager().registerEvents(this, Ghost.getInstance());
	}
	
	@EventHandler
	public void onKitChoose(SignClickEvent event) {
		Sign sign = event.getSign();
		Player p = event.getPlayer();
		if (!sign.getLine(0).equals("[Kit]"))
			return;
		
		Kit kit = Kit.get(sign.getLine(1));
		if (kit == null) {
			Chat.player(p, "&cUnknown kit, " + sign.getLine(1));
			return;
		}
		
		Gamer g = Gamer.get(p);
		if (!g.hasPermission(kit)) {
			Chat.player(p, "&4Buy this kit at &ewww.mczone.co/games/ghost");
			return;
		}
		
		List<String> lines = Chat.asList(sign.getLines());
		lines.set(3, "&lLOCKED");
		lines.set(4, "&ewww.mczone.co");
		lines = Chat.colors(lines);
		WorldUtil.sendSignChange(p, sign, lines);
		Gamer.get(p).setVariable("kit", kit);
		
	}
	
	@EventHandler
	public void onMatchJoin(SignClickEvent event) {
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
