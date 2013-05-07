package co.mczone.api.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.entity.Player;

import co.mczone.api.database.MySQL;
import co.mczone.api.players.Gamer;

public class Hive {
	@Getter HashMap<String, Object> variables = new HashMap<String, Object>();
	@Getter MySQL database;
	@Getter static Hive instance;
	@Getter @Setter GameType type;
	
	public Hive(MySQL database) {
		instance = this;
		this.database = database;
		database.open();
	}
	
	public Gamer getGamer(Player p) {
		return getGamer(p.getName());
	}
	
	public Gamer getGamer(String name) {
		return Gamer.get(name);
	}
	
	public void kill(Player target, String cause) {
		String query = "INSERT INTO kills (server, player, target) VALUES ('" + type.getName() + "',";
		if (cause != null)
			query += "'" + cause + "','" + target.getName() + "'";
		else
			query += "'natural','" + target.getName() + "'";
		database.update(query + ")");
	}
	
	public boolean playerExists(String name) {
		ResultSet r = database.query("SELECT username FROM players WHERE username='" + name + "'");
		try {
			while (r.next())
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public String getUsername(String name) {
		ResultSet r = database.query("SELECT username FROM players WHERE username='" + name + "'");
		try {
			while (r.next())
				return r.getString("username");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	public Date getServerTime() {
        ResultSet r = Hive.getInstance().getDatabase().query("SELECT now()");
        try {
			while (r.next()) {
			    return r.getTimestamp(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
        return null;
	}
	
	public void setVariable(String key, Object value) {
		variables.put(key, value);
	}
	
	public Object getVariable(String key)  {
		return variables.get(key);
	}
}
