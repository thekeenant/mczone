package co.mczone.sg.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import co.mczone.api.players.Gamer;
import co.mczone.api.server.Hive;
import co.mczone.events.custom.PlayerKilledEvent;
import co.mczone.sg.SurvivalGames;
import co.mczone.sg.api.Game;
import co.mczone.util.Chat;

public class GameEvents implements Listener {
	@Getter static GameEvents instance;
	
	@EventHandler
	public void onBreakBlock(BlockBreakEvent event) {
		List<Material> list = new ArrayList<Material>();
		list.add(Material.RED_MUSHROOM);
		list.add(Material.BROWN_MUSHROOM);
		list.add(Material.YELLOW_FLOWER);
		list.add(Material.RED_ROSE);
		list.add(Material.LEAVES);
		list.add(Material.LONG_GRASS);
		list.add(Material.WEB);
		if (!list.contains(event.getBlock().getType()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerKilled(PlayerKilledEvent event) {
		Player t = event.getTarget();
		Gamer g = Gamer.get(t);
		String broadcast = "&4[SG] &6" + event.getDeathMessage();
		broadcast = broadcast.replace(t.getName() + " ", g.getPrefix() + "&f" + t.getName() + " &6");
		if (event.isPlayerKill()) {
			Player p = event.getPlayer();
			g.giveCredits(1);
			g.kill(p, SurvivalGames.getGame().getGameID());
			broadcast = broadcast.replace(" " + p.getName(), " " + g.getPrefix() + "&f" + p.getName());
			for (Player pl : Bukkit.getOnlinePlayers()) {
				if (pl.getName().equals(p.getName()))
					Chat.player(pl, broadcast + " &8[&71 credit&8]");
				else
					Chat.player(pl, broadcast);
				Chat.player(pl, "&4[SG] &6There are " + (Game.getTributes().size() - 1) + " tributes remaining");
			}
		}
		else {
			Hive.getInstance().kill(t, "natural", SurvivalGames.getGame().getGameID());
			Chat.server(broadcast);
			Chat.server("&4[SG] &6There are " + (Game.getTributes().size() - 1) + " tributes remaining");
		}
		
		g.setVariable("death-location", t.getLocation());
		g.setInvisible(true);
		g.getPlayer().setAllowFlight(true);
		g.getPlayer().setFlying(true);
		t.setHealth(20);
		event.setDeathMessage(null);
		g.getPlayer().getWorld().strikeLightning(g.getPlayer().getLocation().add(0, 50, 0));
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		if (SurvivalGames.getWinner() != null)
			return;
		
		Player p = event.getPlayer();
		Gamer t = Gamer.get(event.getPlayer());
		
		if (!Game.getTributes().contains(t))
			return;

		Hive.getInstance().kill(p, "quit", SurvivalGames.getGame().getGameID());
		t.setVariable("death-location", event.getPlayer().getLocation());
		Chat.server("&4[SG &6" + Gamer.get(p).getPrefix()  + t.getName() + " &6has quit the game!");
		Chat.server("&4[SG] &6There are " + (Game.getTributes().size() - 1) + " tributes remaining");
	}
	
	@EventHandler
	public void onCompassUse(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (event.getItem() != null && event.getItem().getType() == Material.COMPASS) {
			Boolean found = false;
			for (int i = 0; i < 5000; i += 25) {
				List<Entity> entities = p.getNearbyEntities(i, 256, i);
				for (Entity e : entities) {
					if (!(e instanceof Player))
						continue;
					Player t = (Player) e;
					if (Gamer.get(t.getName()).isInvisible())
						continue;
					p.setCompassTarget(e.getLocation());
					Chat.player(p, "&2[SG] &aCompass points to &7" + ((Player) e).getDisplayName() + "&a!");
					found = true;
					break;
				}

				if (found)
					break;
			}
			if (!found) {
				Chat.player(p, "&cNo players in range. Compass points to spawn location.");
				Gamer g = Gamer.get(p.getName());
				p.setCompassTarget(g.getLocation("spawn-block"));
			}
		}
	}
	
	List<Block> opened = new ArrayList<Block>();
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event) {
		if (event.getInventory().getHolder() instanceof Chest) {
			Chest c = (Chest) event.getInventory().getHolder();
			if (opened.contains(c.getBlock()))
				return;
			c.getInventory().clear();
			int items = new Random().nextInt(5) + 1;
			int chestSize = c.getInventory().getSize();
			for (int i = 0; i < items; i++) {
				Collections.shuffle(SurvivalGames.getItems());
				c.getInventory().setItem(new Random().nextInt(chestSize), SurvivalGames.getItems().get(0));
				c.update(true);
			}
			opened.add(c.getBlock());
		}
		if (event.getInventory().getHolder() instanceof DoubleChest) {
			DoubleChest c = (DoubleChest) event.getInventory().getHolder();
			if (opened.contains(c.getLocation().getBlock()))
				return;
			c.getInventory().clear();
			int items = new Random().nextInt(5) + 1;
			int chestSize = c.getInventory().getSize();
			for (int i = 0; i < items; i++) {
				Collections.shuffle(SurvivalGames.getItems());
				c.getInventory().setItem(new Random().nextInt(chestSize), SurvivalGames.getItems().get(0));
			}
			opened.add(c.getLocation().getBlock());
		}
	}
}
