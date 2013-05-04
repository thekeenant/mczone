package co.mczone.schedules;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.api.server.Hive;
import co.mczone.api.infractions.*;

public class InfractionSchedule extends BukkitRunnable {	
	@Override
	public void run() {
		Infraction.getList().clear();
		ResultSet r = Hive.getInstance().getDatabase().query("SELECT * FROM infractions");
		try {
			while (r.next()) {
				String username = r.getString("username");
                if (r.getInt("active")==0)
                    continue;
                
                
                Date date = r.getTimestamp("date");
                Date end = r.getTimestamp("end");
                String reason = r.getString("reason");
                
                if (r.getString("type").equalsIgnoreCase("tempban"))
                    new Tempban(username, reason, date, end);
                else if (r.getString("type").equalsIgnoreCase("ban"))
                    new Ban(username, reason, date);
                else if (r.getString("type").equalsIgnoreCase("kick"))
                    new Kick(username, reason, date);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
