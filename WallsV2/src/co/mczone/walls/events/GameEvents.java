package co.mczone.walls.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import co.mczone.api.server.Hive;
import co.mczone.walls.Config;
import co.mczone.walls.State;
import co.mczone.walls.Team;
import co.mczone.walls.Walls;
import co.mczone.walls.utils.Chat;

public class GameEvents implements Listener {
	public GameEvents() {
		Bukkit.getPluginManager().registerEvents(this, Walls.instance);
	}
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
    	String dmessage = event.getDeathMessage() + "!";
		Team.getTeam(event.getEntity()).leave(event.getEntity());
    	event.setDeathMessage(null);
        if (State.PRE)
            return;

		if (event.getEntity().getKiller() instanceof Player) {
			Hive.getInstance().getDatabase().update(String.format("INSERT INTO kills (server,game_id,player,target) VALUES ('%s',%d,'%s','%s')","walls",Walls.ID,event.getEntity().getKiller().getName(),event.getEntity().getName()));
        }
		else {
			Hive.getInstance().getDatabase().update(String.format("INSERT INTO kills (server,game_id,player,target) VALUES ('%s',%d,'%s','%s')","walls",Walls.ID,"natural",event.getEntity().getName()));
		}
		
        Chat.server(dmessage.replace(event.getEntity().getName(), event.getEntity().getDisplayName() + "&c"));
        Walls.getSpectators().add(event.getEntity().getName());
        Player p = event.getEntity();
        Chat.player(p, "&2You are now a spectator and can watch the game.");
        Walls.updateSpectators();
		p.setFlying(true);
    }
    
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
    	if (Walls.getSpectators().contains(event.getPlayer().getName()))
    		event.setCancelled(true);
    }
    
    @EventHandler
    public void onPlayerDropItem(PlayerPickupItemEvent event) {
    	if (Walls.getSpectators().contains(event.getPlayer().getName()))
    		event.setCancelled(true);
    }
    
    @EventHandler
    public void onPlayerDropItem(AsyncPlayerChatEvent event) {
    	if (Walls.getSpectators().contains(event.getPlayer().getName()))
    		event.getRecipients().clear();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        
        if (Team.getTeam(event.getPlayer()) == null)
        	return;
        
        if (!event.getPlayer().isDead() && !Walls.winners.contains(event.getPlayer()))
        	Team.getTeam(event.getPlayer()).leave(event.getPlayer());
        
        if (State.PRE)
            return;

        for (ItemStack i : event.getPlayer().getInventory().getContents())
        	if (i != null && i.getType() != Material.AIR)
        		Config.getWorld().dropItemNaturally(event.getPlayer().getLocation(), i);
        
        if (event.getPlayer().isDead() || Walls.winners.contains(event.getPlayer()))
            return;
		

        Hive.getInstance().getDatabase().update(String.format("INSERT INTO kills (server,game_id,player,target) VALUES ('%s',%d,'%s','%s')","walls",Walls.ID,"left",event.getPlayer().getName()));
        Chat.server(event.getPlayer().getDisplayName() + "&c has left the game! What a coward!");
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && Walls.getSpectators().contains(((Player) event.getEntity()).getName())) {
			event.setCancelled(true);
			return;
		}
		if (event.getDamager() instanceof Player && Walls.getSpectators().contains(((Player) event.getDamager()).getName())) {
			event.setCancelled(true);
			return;
		}
    	
        if (State.PRE)
            return;
        
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player p = (Player) event.getDamager();
            Player t = (Player) event.getEntity();
            
            if (Team.getTeam(p) == Team.getTeam(t)) {
                Chat.player(p, "&cYou are not allowed to hurt your own teammates!");
                event.setCancelled(true);
            }
        }
		if (event.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getDamager();
			if (arrow.getShooter() instanceof Player && event.getEntity() instanceof Player) {
				Player p = (Player) arrow.getShooter();
				Player t = (Player) event.getEntity();

	            if (Team.getTeam(p) == Team.getTeam(t)) {
	                Chat.player(p, "&cYou are not allowed to hurt your own teammates!");
	                event.setCancelled(true);
	            }
			}
		}
    }
}
