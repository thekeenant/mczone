package co.mczone.events;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import co.mczone.MCZone;
import co.mczone.api.Chat;
import co.mczone.api.infractions.Ban;
import co.mczone.api.infractions.Infraction;
import co.mczone.api.infractions.Tempban;
import co.mczone.api.players.Gamer;
import co.mczone.api.players.Rank;
import co.mczone.api.server.Hive;

public class ConnectEvents implements Listener {
	public ConnectEvents() {
		MCZone.getInstance().getServer().getPluginManager().registerEvents(this, MCZone.getInstance());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player p = event.getPlayer();
		Gamer g = Gamer.get(p.getName());
		if (g == null)
			g = new Gamer(p.getName());
		
		// Import current ranking
		if (Rank.getRanks().containsKey(p.getName()))
			g.setRank(Rank.getRanks().get(p.getName()));
		
		// Import infractions and check bans
		if (Infraction.getList().values().contains(p.getName())) {
			for (Infraction i : Infraction.getList().keySet()) {
				g.getInfractions().add(i);
			}
		}
		
		for (Infraction i : g.getInfractions()) {
			String msg = "";
			if (i instanceof Ban) {
				String title = "&4[Ban] &cDate: &f" + i.forHumanDate();
				msg += title + "\n";
			}
			else if (i instanceof Tempban) {
                Date now = null;
                ResultSet r = Hive.getInstance().getDatabase().query("SELECT now()");
                try {
					while (r.next()) {
					    now = r.getTimestamp(1);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
                if (now != null && now.before(((Tempban) i).getExpires())) {
					String title = "&4[Tempban] &cDate: &f" + i.forHumanDate();
					String sub = "&cExpires: &f" + ((Tempban) i).getExpires();
					msg += title + "\n" + sub + "\n";
                }
			}
			if (msg != "") {
				String footer = "\n\n&7Email &oinfo@mczone.co &7to appeal.";
				event.disallow(Result.KICK_OTHER, Chat.colors(msg + footer));
			}
		}
	}
}
