package co.mczone.lobby.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.bukkit.entity.Player;

import co.mczone.lobby.Lobby;
import co.mczone.lobby.api.Query;
import co.mczone.util.Chat;

public class Util {
	public static String ucs2ToUTF8(byte[] ucs2Bytes) throws UnsupportedEncodingException {
		String unicode = new String(ucs2Bytes, "UTF-16");
		String utf8 = new String(unicode.getBytes("UTF-8"), "Cp1252");
		return utf8;
	}
	
	public static boolean isOpen(String hostname, int port) {
		Query q = new Query(hostname, port);
		if (!q.fetchData())
			return false;
		
		return isOpen(q);
	}
	
	public static boolean isOpen(Query q) {
		if (q == null)
			return false;
		
		String motd = q.getMotd();
		if (motd == null)
			return false;
		
		if (q.getPlayersOnline() >= q.getMaxPlayers())
			return false;
		
		if (!motd.contains("ready") && !motd.contains("start"))
			return false;
		
		return true;
	}
	
	
	public static void connect(Player p, String server) {
		if (Lobby.connectionTimes.get(p.getName()) != null) {
			if (new Date().getTime() / 1000 - Lobby.getConnectionTimes().get(p.getName()).getTime() / 1000 < 1)
				return;
		}
		Chat.log("Sending " + p.getName() + " to " + server);
		Lobby.connectionTimes.put(p.getName(), new Date());
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
		    out.writeUTF("Transfer");
		    out.writeUTF(server); // Target Server
		} catch (IOException e) {
		    // Can never happen
		}
		p.sendPluginMessage(Lobby.getInstance(), "BungeeCord", b.toByteArray());
	}
}
