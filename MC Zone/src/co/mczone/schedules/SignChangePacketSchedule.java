package co.mczone.schedules;

import java.util.Date;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.api.players.SignChangePacket;

public class SignChangePacketSchedule extends BukkitRunnable {
	@Override
	public void run() {
		for (SignChangePacket packet : SignChangePacket.getList()) {
			if (packet.isReverted())
				continue;
			
			Player p = packet.getPlayer();
			if (p == null || !p.isOnline())
				continue;
			
			Date before = packet.getSentTime();
			Date now = new Date();
			int miliseconds = packet.getRevertTime() * 1000;
			
			if (now.getTime() - before.getTime() > miliseconds) {
				// Revert back to normal
				packet.revert();
			}
		}
	}

}
