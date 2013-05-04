package co.mczone.lobby;

import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.api.players.Gamer;
import co.mczone.lobby.events.Events;
import co.mczone.parkour.Parkour;
import co.mczone.util.Chat;

public class PlayerScheduler extends BukkitRunnable {
	@Override
	public void run() {
		Iterator<Entry<String, Date>> i = Events.getJoinTimes().entrySet().iterator();
		while (i.hasNext()) {
			Entry<String, Date> e = i.next();
			Date now = new Date();
			Date last = e.getValue();
			final Player p = Bukkit.getPlayerExact(e.getKey());
			if (p == null || !p.isOnline()) {
				i.remove();
				continue;
			}
			
			Gamer g = Gamer.get(p.getName());
			if (g.getRank().getLevel() > 0)
				continue;
			
			if (Parkour.getCurrent().containsKey(p.getName()))
				continue;
			
			int delay = 300; // Seconds
			boolean kick = false;
			if (now.getTime() - last.getTime() > delay * 1000) {
				kick = true;
			}
			else if (now.getTime() - last.getTime() > 30 * 1000) {
				if (!Events.getMovement().contains(p.getName()))
					kick = true;
			}
			
			if (kick) {
				Chat.player(p, "&4[Lobby] &cYou have been kicked for being AFK in the MC Zone lobby.");
				new BukkitRunnable() {
					@Override
					public void run() {
						p.kickPlayer(Chat.colors("&4[Lobby] &cYou have been kicked for being AFK in the MC Zone lobby."));
					}
					
				}.runTask(Lobby.getInstance());
				i.remove();
			}
			
		}
	}
}
