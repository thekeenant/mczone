package co.mczone.sg.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.server.ServerListPingEvent;

import co.mczone.api.players.Gamer;
import co.mczone.sg.Scheduler;
import co.mczone.sg.SurvivalGames;
import co.mczone.sg.api.Game;
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
			event.setMotd(Game.getTributes().size() + " tributes remaining");
		}
	}
	
	@EventHandler  
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		Gamer g = Gamer.get(p);
		g.getPlayer().setHealth(20);
		g.getPlayer().setFoodLevel(20);
		g.clearInventory();
		g.run("give-book");
		

		Chat.player(p, "&7--------------------------------------------");
		Chat.player(p, "&aWelcome to MC Zone's &2The Survival Games");
		Chat.player(p, "&6&oRead the book in your inventory to get started");
		Chat.player(p, "&7--------------------------------------------");
		
		if (Scheduler.getState() == State.PVP) {
			g.setInvisible(true);
		}
		if (Scheduler.getState() == State.WAITING) {
			if (Game.getTributes().size() < 24) {
				g.setInvisible(false);
				
				Location l = Map.getCurrent().getNextSpawn();
				l.add(0, Map.getCurrent().getWorld().getHighestBlockYAt(l) + 1, 0);
				g.setVariable("spawn-block", l);
				g.getPlayer().teleport(l);
				g.setVariable("moveable", false);
				g.clearInventory();
				p.teleport(Map.getCurrent().getWorld().getSpawnLocation());
			}
			else {
				g.setInvisible(true);
				p.teleport(Map.getCurrent().getWorld().getSpawnLocation());
			}
		}
		else {
			p.teleport(SurvivalGames.getInstance().getConfigAPI().getLocation("lobby"));;
		}
		event.setJoinMessage(null);
	}
}
