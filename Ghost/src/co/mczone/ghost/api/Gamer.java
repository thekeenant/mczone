package co.mczone.ghost.api;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Gamer {
	
	@Getter static List<Gamer> list = new ArrayList<Gamer>();
	@Getter String name;
	@Getter boolean invisible = false;
	
	@Getter @Setter boolean online;
	
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
	
	public static Gamer get(Player p) {
		return get(p.getName());
	}
	
	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
		updateHidden();
	}
	
	public static void updateHidden() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			Gamer gp = Gamer.get(p);
			for (Player t : Bukkit.getOnlinePlayers()) {
				Gamer gt = Gamer.get(t);
				if (gp.getName().equals(gt.getName()))
					continue;
			}
		}
	}
	
}
