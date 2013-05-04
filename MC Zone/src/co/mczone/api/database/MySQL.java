package co.mczone.api.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.MCZone;

public class MySQL extends Database {
	private String hostname = "";
	private String portnmbr = "";
	private String username = "";
	private String password = "";
	private String database = "";

	public MySQL(String hostname, String portnmbr, String database, String username, String password) {
		super();
		this.hostname = hostname;
		this.portnmbr = portnmbr;
		this.database = database;
		this.username = username;
		this.password = password;
	}
	
	public Connection open() {
		String url = "";
		try {
			url = "jdbc:mysql://"
					+ this.hostname
					+ ":"
					+ this.portnmbr
					+ "/"
					+ this.database
					+ "?zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
			this.connection = DriverManager.getConnection(url, this.username,
					this.password);
			return this.connection;
		} catch (SQLException e) {
		}
		return null;
	}
	
	public void close() {
		try {
			if (connection != null)
				connection.close();
		} catch (Exception e) {
		}
	}


	public Connection getConnection() {
		return this.connection;
	}

	public boolean checkConnection() {
		if (connection != null)
			return true;
		return false;
	}

	public ResultSet query(String query) {
		Statement statement = null;
		ResultSet result = null;
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean update(final String query) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Statement statement = null;
				try {
					statement = connection.createStatement();
					statement.executeUpdate(query);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(MCZone.getInstance());
		return true;
	}

	public boolean syncUpdate(final String query) {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public int resultInt(ResultSet result, int column) {
		if (result == null)
			return 0;

		try {
			result.next();
			int integer = result.getInt(column);
			result.close();

			return integer;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public String resultString(ResultSet result, int column) {
		if (result == null)
			return null;

		try {
			result.next();
			String string = result.getString(column);
			result.close();

			return string;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public PreparedStatement prepare(String query) {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			return ps;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ps;
	}

	public boolean insert(String table, String[] column, String[] value) {
		Statement statement = null;
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		for (String s : column) {
			sb1.append(s + ",");
		}
		for (String s : value) {
			sb2.append("'" + s + "',");
		}
		String columns = sb1.toString().substring(0,
				sb1.toString().length() - 1);
		String values = sb2.toString()
				.substring(0, sb2.toString().length() - 1);
		try {
			statement = this.connection.createStatement();
			statement.execute("INSERT INTO " + table + "(" + columns
					+ ") VALUES (" + values + ")");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteTable(String table) {
		Statement statement = null;
		try {
			if (table.equals("") || table == null) {
				return true;
			}
			statement = connection.createStatement();
			statement.executeUpdate("DROP TABLE " + table);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean createTable(String table, String query) {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate("create table if not exists " + table
					+ " (" + query + ");");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String escape(String str) throws Exception {
		if (str == null) {
			return null;
		}

		if (str.replaceAll(
				"[a-zA-Z0-9_!@#$%^&*()-=+~.;:,\\Q[\\E\\Q]\\E<>{}\\/? ]", "")
				.length() < 1) {
			return str;
		}

		String clean_string = str;
		clean_string = clean_string.replaceAll("\\\\", "\\\\\\\\");
		clean_string = clean_string.replaceAll("\\n", "\\\\n");
		clean_string = clean_string.replaceAll("\\r", "\\\\r");
		clean_string = clean_string.replaceAll("\\t", "\\\\t");
		clean_string = clean_string.replaceAll("\\00", "\\\\0");
		clean_string = clean_string.replaceAll("'", "\\\\'");
		clean_string = clean_string.replaceAll("\\\"", "\\\\\"");

		if (clean_string.replaceAll(
				"[a-zA-Z0-9_!@#$%^&*()-=+~.;:,\\Q[\\E\\Q]\\E<>{}\\/?\\\\\"' ]",
				"").length() < 1) {
			return clean_string;
		}

		java.sql.Statement stmt = connection.createStatement();
		String qry = "SELECT QUOTE('" + clean_string + "')";

		stmt.executeQuery(qry);
		java.sql.ResultSet resultSet = stmt.getResultSet();
		resultSet.first();
		String r = resultSet.getString(1);
		return r.substring(1, r.length() - 1);
	}

	public String quote(String str)
			throws Exception {
		if (str == null) {
			return "NULL";
		}
		return "'" + escape(str) + "'";
	}

	public String nameQuote(String str)
			throws Exception {
		if (str == null) {
			return "NULL";
		}
		return "`" + escape(str) + "`";
	}
}