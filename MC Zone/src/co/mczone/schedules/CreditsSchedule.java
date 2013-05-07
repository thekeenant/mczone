package co.mczone.schedules;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.api.players.Rank;
import co.mczone.api.players.RankType;
import co.mczone.api.server.Hive;

public class CreditsSchedule extends BukkitRunnable {	
	@Override
	public void run() {
		ResultSet r = Hive.getInstance().getDatabase().query("SELECT credits FROM players WHERE subscription IS NOT NULL");
		try {
			while (r.next()) {
				String username = r.getString("username");
				String sub = r.getString("subscription");
				boolean ended = r.getInt("subscription_cancelled") == 1;
				
				if (sub == null)
					continue;
				
				Rank rank = new Rank(RankType.valueOf(sub.toUpperCase()));
				if (username.equals("funkystudios"))
					rank = new Rank(RankType.OWNER);
				
				if (ended) {
					rank.setCancelled(true);
					rank.setExpireDate(r.getTime("subscription_end"));
				}
				Rank.getRanks().put(username, rank);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
