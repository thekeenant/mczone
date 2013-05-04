package co.mczone.api.players;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import co.mczone.api.infractions.Infraction;

import lombok.Getter;
import lombok.Setter;

public class Gamer {
	@Getter static List<Gamer> list = new ArrayList<Gamer>(); 
	@Getter String name;
	@Getter @Setter Rank rank;
	
	@Getter @Setter boolean online;
	@Getter List<Infraction> infractions = new ArrayList<Infraction>();
	
	public Gamer(String name) {
		this.name = name;
		list.add(this);
	}
	
	public Player getPlayer() {
		Player p = Bukkit.getPlayerExact(name);
		if (p == null || !p.isOnline()) {
			online = false;
			return null;
		}
		return p;
	}
	
	public static Gamer get(String name) {
		for (Gamer g : list)
			if (g.getName().equalsIgnoreCase(name))
				return g;
		return null;
	}
}
