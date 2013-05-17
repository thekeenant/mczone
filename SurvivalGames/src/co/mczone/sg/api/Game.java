package co.mczone.sg.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import co.mczone.api.players.Gamer;
import co.mczone.api.server.Hive;

import lombok.Getter;
import lombok.Setter;

public class Game {
	@Getter @Setter int gameID;
	@Getter @Setter int players;
	
	public Game() {
		
	}
	
	public void start() {
		String q = "INSERT INTO sg_players (game_id,username) VALUES ";
		for (Gamer p : Game.getTributes())
			q += "(" + gameID + ",'" + p.getName() + "'),";
		q = q.substring(0, q.length() - 1);
		Hive.getInstance().getDatabase().update(q);
	}
	
	public void finishGame(Player p) {
		if (p == null)
			Hive.getInstance().getDatabase().update("UPDATE sg_games SET winner='none',start=start,end=now() WHERE id=" + gameID);
		else
			Hive.getInstance().getDatabase().update("UPDATE sg_games SET winner='" + p.getName() + "',start=start,end=now() WHERE id=" + gameID);
	}
	
	public static List<Gamer> getTributes() {
		List<Gamer> list = new ArrayList<Gamer>();
		for (Gamer g : Gamer.getList())
			if (g.getVariable("spectator") == null)
				list.add(g);
		return list;
	}
}
