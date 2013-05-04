package co.mczone.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import co.mczone.MCZone;
import co.mczone.api.players.Gamer;
import co.mczone.api.server.Hive;
import co.mczone.util.Chat;

public class GeneralEvents implements Listener {
	public GeneralEvents() {
		MCZone.getInstance().getServer().getPluginManager().registerEvents(this, MCZone.getInstance());
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();
		Gamer g = Hive.getInstance().getGamer(p);
		
		String prefix = "";
		String name = p.getName();
		String msg = event.getMessage();
		
		if (g.getRank() != null)
			prefix = g.getRank().getType().getPrefix();
		
		String result = Chat.colors(prefix + "&7" + name + "&f: ") + msg;
		if (g.getRank().getType().getLevel() >= 5)
			result = Chat.colors(result);
		
		// Symbol % has formatting issues
		event.setFormat(result.replace("%", "%%"));
	}
}
