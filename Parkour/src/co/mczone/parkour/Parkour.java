package co.mczone.parkour;

import java.util.HashMap;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import co.mczone.api.ConfigAPI;
import co.mczone.api.players.Gamer;
import co.mczone.api.server.Hive;
import co.mczone.parkour.cmds.*;
import co.mczone.util.Chat;

public class Parkour extends JavaPlugin implements Listener {
	@Getter static Parkour instance;
	@Getter ConfigAPI configAPI;
	
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		instance = this;
		configAPI = new ConfigAPI(this);
		
		loadCourses();
		
		Hive.getInstance().registerCommand(this, "parkour", new ParkourCmd());
	}
	
	public void loadCourses() {
		Course.getList().clear();
		for (String id : configAPI.getConfig().getKeys(false)) {
			if (id.equalsIgnoreCase("void"))
				continue;
			
			String title = configAPI.getString(id + ".title");
			
			
			if (configAPI.getConfig().getConfigurationSection(id).getKeys(false).size() < 5) {
				new Course(id, title);
				continue;
			}
			
			
			Location start = configAPI.getLocation(id + ".start");
			int buttonX = configAPI.getInt(id + ".button.x");
			int buttonY = configAPI.getInt(id + ".button.y");
			int buttonZ = configAPI.getInt(id + ".button.z");
			String buttonWorld = configAPI.getString(id + ".button.world");
			
			int endX = configAPI.getInt(id + ".end.x");
			int endY = configAPI.getInt(id + ".end.y");
			int endZ = configAPI.getInt(id + ".end.z");
			String endWorld = configAPI.getString(id + ".end.world");
			
			String next = null;
			if (configAPI.contains(id + ".next"))
				next = configAPI.getString(id + ".next");

			if (buttonWorld == null || endWorld == null)
				continue;
			
			World s = Bukkit.getWorld(buttonWorld);
			if (s == null)
				continue;
			World e = Bukkit.getWorld(endWorld);
			if (e == null)
				continue;
			
			Course c = new Course(id, title, start, s.getBlockAt(buttonX, buttonY, buttonZ), e.getBlockAt(endX, endY, endZ));
			c.setNextCourse(next);
		}
	}
	
	public void onDisable() {
		this.saveConfig();
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
			return;
		
		Player p = event.getPlayer();
		Gamer g = Gamer.get(p);
		
		Block b = event.getClickedBlock();
		for (Course c : Course.getList()) {
			Block test1 = c.getButton();
			Block test2 = c.getEnd();

			if (test1 != null && test1.getX() == b.getX() && test1.getY() == b.getY() && test1.getZ() == b.getZ()) {
				g.setVariable("parkour", c);
				p.teleport(c.getStart());
				return;
			}
			
			if (test2 != null && test2.getX() == b.getX() && test2.getY() == b.getY() && test2.getZ() == b.getZ()) {
				Chat.server(Gamer.get(p.getName()).getRank().getPrefix() + p.getDisplayName() + " &ejust beat &2&l" + c.getTitle() + "");
				if (c.getNextCourse() != null) {
					Course next = Course.get(c.getNextCourse());
					if (next != null) {
						g.setVariable("parkour", next);
						p.teleport(next.getStart());
						return;
					}
				}
				p.teleport(c.getStart());
				return;
			}
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		Gamer g = Gamer.get(p);
		if (p.getLocation().getY() < configAPI.getInt("void", 66)) {
			if (g.getVariable("parkour") != null) {
				p.teleport(((Course) g.getVariable("parkour")).getStart());
			}
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		
		if (event.getCause() != DamageCause.VOID)
			return;
		
		Player p = (Player) event.getEntity();
		Gamer g = Gamer.get(p);
		if (g.getVariable("parkour") != null) {
			p.teleport(((Course) g.getVariable("parkour")).getStart());
		}
	}
}
