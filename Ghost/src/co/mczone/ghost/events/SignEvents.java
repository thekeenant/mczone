package co.mczone.ghost.events;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.api.players.Gamer;
import co.mczone.api.players.SignChangePacket;
import co.mczone.events.custom.SignClickEvent;
import co.mczone.ghost.Ghost;
import co.mczone.ghost.api.Kit;
import co.mczone.ghost.api.Match;
import co.mczone.ghost.api.TeamColor;
import co.mczone.util.Chat;

public class SignEvents implements Listener {
	
	public SignEvents() {
		Ghost.getInstance().getServer().getPluginManager().registerEvents(this, Ghost.getInstance());
	}
	
	@EventHandler
	public void onKitChoose(SignClickEvent event) {
		if (!event.isRightClick())
			return;
		
		final Sign sign = event.getSign();
		final Player p = event.getPlayer();
		if (!sign.getLine(0).equals("[Kit]"))
			return;
		
		final Kit kit = Kit.get(sign.getLine(1));
		if (kit == null) {
			Chat.player(p, "&cUnknown kit, " + sign.getLine(1));
			return;
		}
		
		Gamer g = Gamer.get(p);
		if (!g.hasPermission(kit)) {
			String[] arr = new String[4];
			arr[0] = sign.getLine(0);
			arr[1] = sign.getLine(1);
			arr[2] = Chat.colors("&lBuy this kit:");
			arr[3] = Chat.colors("&ewww.mczone.co");
			new SignChangePacket(p, sign, arr, 6).send();
			return;
		}
		
		new BukkitRunnable() {
			@Override
			public void run() {
				Gamer.get(p).setVariable("kit", kit);
				Kit.giveKit(p);
			}
			
		}.runTaskLater(Ghost.getInstance(), 2);
		
		
		String[] arr = new String[4];
		arr[0] = sign.getLine(0);
		arr[1] = sign.getLine(1);
		arr[2] = Chat.colors("&lKIT SELECTED");
		arr[3] = Chat.colors("&aJoin a Match!");
		new SignChangePacket(p, sign, arr, 6).send();
	}
	
	@EventHandler
	public void onMatchJoin(SignClickEvent event) {
		Block b = event.getBlock();
		Player p = event.getPlayer();
		Match match = null;
		for (Match m : Match.getList()) {
			if (m.getSign() == null)
				continue;
			if (m.getSign().getX() == b.getX() && m.getSign().getY() == b.getY() && m.getSign().getZ() == b.getZ()) {
				match = m;
			}
		}
		
		if (match == null)
			return;
		
		match.updateSign();
		
		if (match.getPlayers().size() >= Match.MAX_PER_TEAM * 2) {
			Chat.player(p, "&cThat match is currently full.");
			
			String[] arr = new String[4];
			arr[0] = event.getSign().getLine(0);
			arr[1] = event.getSign().getLine(1);
			arr[2] = event.getSign().getLine(2);
			arr[3] = "&l&cMATCH FULL";
			
			new SignChangePacket(p, event.getSign(), arr, 5);
			
			return;
		}
		
		TeamColor team = match.join(p);
		Chat.player(p, "&aYou have joined team " + team.getColor() + team.name());
	}
}
