package co.mczone.walls.events;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import co.mczone.walls.Kit;
import co.mczone.walls.State;
import co.mczone.walls.Team;
import co.mczone.walls.Walls;
import co.mczone.walls.utils.Chat;

public class KitEvents implements Listener {
	public KitEvents() {
		Bukkit.getPluginManager().registerEvents(this, Walls.instance);
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if ((event.getDamager() instanceof Player)) {
			Player p = (Player)event.getDamager();

			if (Kit.getKit(p).getName().equalsIgnoreCase("woodcutter") && (p.getItemInHand().getType().name().contains("_AXE"))) {
				Random r = new Random();

				double multiplier = 1.0D + 1.0D * r.nextDouble() - r.nextDouble() + 0.5D;

				if (multiplier < 1.0D)
					multiplier = 1.0D;
				if (multiplier >= 2.0D) {
					multiplier = 2.0D;
				}
				event.setDamage((int)(event.getDamage() * multiplier));
			}
		}
	}

	@EventHandler
	public void Woodcutter(BlockBreakEvent event) {
		if (State.PRE)
			return;

		Player p = event.getPlayer();
		if (Kit.getKit(p).getName().equalsIgnoreCase("woodcutter")
				&& event.getBlock().getType().equals(Material.LOG)
				&& (p.getItemInHand().getType().toString().contains("AXE"))) {
			for (int i = 0; i <= 50; i++) {
				Location loc = event.getBlock().getLocation();
				loc.setY(loc.getY() + i);
				if (loc.getBlock().getType().equals(Material.LOG))
					loc.getBlock().breakNaturally();
				else
					break;
			}
		}
	}

	@EventHandler
	public void Stomper(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (Kit.getKit(p).getName().equalsIgnoreCase("stomper")) {
				if (e.getCause() == DamageCause.FALL) {
					List<Entity> nearbyEntities = e.getEntity()
							.getNearbyEntities(5, 5, 5);
					for (Entity target : nearbyEntities) {
						if (target instanceof Player) {
							Player t = (Player) target;
							if (Team.getTeam(p) == Team.getTeam(t))
								continue;
							if (t.isSneaking())
								t.damage(e.getDamage() / 2, e.getEntity());
							else
								t.damage(e.getDamage(), e.getEntity());
						}
					}
					e.setDamage(0);
				}
			}
		}
	}

	@EventHandler
	public void WaterPower(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		if (Kit.getKit(p).getName().equalsIgnoreCase("poseidon")) {
			Material m = p.getLocation().getBlock().getType();
			if (m == Material.STATIONARY_WATER || m == Material.WATER) {
				p.addPotionEffect(new PotionEffect(
						PotionEffectType.INCREASE_DAMAGE, 200, 1));
				p.setRemainingAir(300);
			}
		}
	}

	@EventHandler
	public void PowerArrowsAndSnowman(ProjectileHitEvent event) {
		Entity entity = event.getEntity();
		// ARROWS EXPLODE: 5
		if ((entity instanceof Arrow)) {
			Arrow arrow = (Arrow) entity;
			Entity shooter = arrow.getShooter();
			if ((shooter instanceof Player)) {
				Player p = (Player) shooter;
				if (Kit.getKit(p).getName().equalsIgnoreCase("cannon")) {
					Bukkit.getServer().getWorld("world")
							.createExplosion(arrow.getLocation(), 2.0F);
					arrow.remove();
				} else {
					return;
				}
			} else {
				return;
			}
		}

		// SNOWMAN: 6
		if ((entity instanceof Snowball)) {
			Snowball ball = (Snowball) entity;
			Entity shooter = ball.getShooter();
			if ((shooter instanceof Player)) {
				Player p = (Player) shooter;
				if (Kit.getKit(p).getName().equalsIgnoreCase("snowman")) {
					for (Entity e : ball.getNearbyEntities(3, 3, 3))
						if ((e instanceof Player)) {
							Player pl = (Player) e;
							if (pl.getName() != p.getName()) {
								pl.addPotionEffect(new PotionEffect(
										PotionEffectType.BLINDNESS, 100, 1));
								pl.addPotionEffect(new PotionEffect(
										PotionEffectType.CONFUSION, 160, 1));
							}
						}
				}
			} else {
				return;
			}
		}
	}

	@EventHandler
	public void Fireman(PlayerInteractEvent event) {
		// LAVA: 12
		Player p = event.getPlayer();
		if (!Kit.getKit(p).getName().equalsIgnoreCase("fireman") && event.getItem() != null	&& event.getItem().getType() == Material.LAVA_BUCKET) {
			event.setCancelled(true);
			Chat.player(p, "&cOnly players with the \"Fireman\" kit can use lava!");
		}
	}
}
