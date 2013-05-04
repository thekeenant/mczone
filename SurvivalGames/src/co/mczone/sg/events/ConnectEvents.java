package co.mczone.sg.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

import co.mczone.sg.Scheduler;
import co.mczone.sg.SurvivalGames;
import co.mczone.sg.api.GamerSG;
import co.mczone.sg.api.Map;
import co.mczone.sg.api.State;
import co.mczone.util.Chat;

public class ConnectEvents implements Listener {	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerLogin(PlayerLoginEvent event) {
		
	}
	
	@EventHandler
	public void onServerPing(ServerListPingEvent event) {
		if (Scheduler.getState() == State.PREP) {
			event.setMotd("Game starting soon!");
		}
		else {
			event.setMotd(GamerSG.getTributes() + " tributes remaining");
		}
	}
	
	@EventHandler  
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		GamerSG g = new GamerSG(p.getName());
		g.heal();
		g.clearInventory();
		g.giveBook();
		GamerSG.hideSpectators();
		

		Chat.player(p, "&7--------------------------------------------");
		Chat.player(p, "&aWelcome to MC Zone's &2The Survival Games");
		Chat.player(p, "&6&oRead the book in your inventory to get started");
		Chat.player(p, "&7--------------------------------------------");
		
		if (Scheduler.getState() == State.PVP) {
			g.setSpectator(true);
		}
		if (Scheduler.getState() == State.WAITING) {
			if (GamerSG.getTributes().size() < 24) {
				g.setSpectator(false);
				g.setSpawnBlock(Map.getCurrent().getNextSpawn());
				int y = Map.getCurrent().getWorld().getHighestBlockYAt(g.getSpawnBlock());
				g.getSpawnBlock().setY(y + 1);
				g.getPlayer().teleport(g.getSpawnBlock());
				g.setMoveable(false);
				g.clearInventory();
				p.teleport(Map.getCurrent().getWorld().getSpawnLocation());
			}
			else {
				g.setSpectator(true);
				p.teleport(Map.getCurrent().getWorld().getSpawnLocation());
			}
		}
		else {
			p.teleport(SurvivalGames.getInstance().getConfigAPI().getLocation("lobby"));;
		}
		event.setJoinMessage(null);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (Scheduler.getState() != State.PVP) {
			event.setQuitMessage(null);
			GamerSG.get(event.getPlayer().getName()).remove();
		}
	}
}
