package co.mczone.nexus.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import co.mczone.api.players.Gamer;
import co.mczone.api.server.Hive;
import co.mczone.events.custom.PlayerDamageEvent;
import co.mczone.events.custom.PlayerKilledEvent;
import co.mczone.nexus.Nexus;
import co.mczone.nexus.api.Map;
import co.mczone.nexus.api.Team;
import co.mczone.util.Chat;

public class GameEvents implements Listener {

	public GameEvents() {
		Bukkit.getPluginManager().registerEvents(this, Nexus.getPlugin());
	}
	
	@EventHandler
	public void onPlayerKilled(PlayerKilledEvent event) {
		Gamer g = Gamer.get(event.getPlayer());
		Gamer t = Gamer.get(event.getTarget());

		if (event.isPlayerKill())
			g.kill(t, Nexus.getRotary().getGameID());
		else
			Hive.getInstance().kill(t.getPlayer(), "natural", Nexus.getRotary().getGameID());
		
		// Set killstreaks
		int streak = (Integer) g.getVariable("killstreak");
		if (streak > 1)
			Chat.player(g, "&eYou have reached a killstreak of &6" + streak + " &ekills");
		g.setVariable("killstreak", streak + 1);
		t.setVariable("killstreak", 0);
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Gamer g = Gamer.get(event.getPlayer());
		
		Team t = Nexus.getRotary().getCurrentMap().getTeam(g);
		g.teleport(t.getSpawnLocation());
		g.run("give-kit");
	}
	
	@EventHandler
	public void onPlayerDamage(PlayerDamageEvent event) {
		if (event.isDamageByEntity() == false || event.getDamager() instanceof Player == false)
			return;
		
		Gamer g = Gamer.get((Player) event.getDamager());
		Gamer t = Gamer.get(event.getTarget());
		
		Map map = Nexus.getRotary().getCurrentMap();
		if (map.getTeam(g) == map.getTeam(t))
			event.setCancelled(true);
	}
	
}
