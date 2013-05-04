package co.mczone.api.server;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.entity.Player;

import co.mczone.api.GameType;
import co.mczone.api.database.MySQL;
import co.mczone.api.players.Gamer;

public class Hive {
	@Getter MySQL database;
	@Getter static Hive instance;
	@Getter @Setter GameType type;
	
	public Hive(MySQL database) {
		instance = this;
		this.database = database;
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
}
