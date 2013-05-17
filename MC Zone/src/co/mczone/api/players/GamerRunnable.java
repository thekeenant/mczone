package co.mczone.api.players;

import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import lombok.Setter;

public class GamerRunnable extends BukkitRunnable {
	@Getter @Setter	public Gamer gamer;
	@Getter @Setter boolean sync = false;
	
	@Override
	public void run() {
		
	}

}
