package co.mczone.api.server;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import co.mczone.api.ConfigAPI;
import co.mczone.api.backend.DataSender;
import co.mczone.api.backend.Status;
import co.mczone.api.commands.HiveCommandExecutor;
import co.mczone.api.database.MySQL;
import co.mczone.api.players.Gamer;

public class Hive {
	@Getter HashMap<String, Object> variables = new HashMap<String, Object>();
	@Getter MySQL database;
	@Getter static Hive instance;
	@Getter @Setter GameType type;
	
	@Getter ConfigAPI config;
	
	DataSender sender;
	@Getter Status status;
	
	public Hive(MySQL database, ConfigAPI config) {
		instance = this;
		this.database = database;
		this.config = config;
		
		database.open();
		
		sender = new DataSender();
	}
	
	public void setStatus(Status status) {
		this.status = status;
		updateStatus();
	}
	
	public int getServerID() {
		return Bukkit.getPort();
	}
	
	public void updateStatus() {
		int id = getServerID();
		
		if (type == null)
			return;
		
		// Chat.log("Updating status of " + type.getName() + ":" + id + " to " + Bukkit.getOnlinePlayers().length + " players with status " + status.name() + "!");
		try {
			sender.send(type.getName(), id, Bukkit.getOnlinePlayers().length, status.getValue());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Gamer getGamer(Player p) {
		return getGamer(p.getName());
	}
	
	public Gamer getGamer(String name) {
		return Gamer.get(name);
	}
	
	public void kill(Player target, String cause, int game_id) {
		String query = "INSERT INTO kills (server, player, target, game_id) VALUES ('" + type.getName() + "',";
		if (cause != null)
			query += "'" + cause + "','" + target.getName() + "'";
		else
			query += "'natural','" + target.getName() + "'";
		database.update(query + ", " + game_id + ")");
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

	public void registerCommand(PluginCommand command, CommandExecutor executor) {
		command.setExecutor(new HiveCommandExecutor());
		HiveCommandExecutor.getCommands().put(command.getName(), executor);
	}
	
	public void registerCommand(JavaPlugin plugin, String name, CommandExecutor executor) {
		registerCommand(plugin.getCommand(name), executor);
	}
	
	public String getServerIP(GameType server) {
		if (config.contains(server.getName()))
			return config.getString(server.getName());
		return server.getIp();
	}
}
