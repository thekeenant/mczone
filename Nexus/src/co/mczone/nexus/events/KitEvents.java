package co.mczone.nexus.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.api.players.CustomDamageCause;
import co.mczone.api.players.Gamer;
import co.mczone.nexus.Nexus;
import co.mczone.nexus.api.Kit;
import co.mczone.nexus.api.Mine;
import co.mczone.nexus.api.Team;

public class KitEvents implements Listener {
	
	public KitEvents() {
		Bukkit.getPluginManager().registerEvents(this, Nexus.getPlugin());
	}

	// Scout Fall Damage
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Gamer g = Gamer.get((Player) event.getEntity());
			if (g.getVariable("spectator") != null)
				return;
			
			Kit kit = (Kit) g.getVariable("kit");
			if (kit.getName().equals("scout") && event.getCause() == DamageCause.FALL) {
				event.setDamage(0);
			}
		}
	}

	// Tank: No Sprint
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		final Gamer g = Gamer.get(event.getPlayer());

		if (g.getVariable("spectator") != null)
			return;
		
		if (g.getVariable("kit") != Kit.get("tank"))
			return;
		
		g.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1, 100), true);
		
		if (g.getPlayer().isSprinting()) {
			new BukkitRunnable() {
				public void run() {
					g.getPlayer().setSprinting(false);
				}
			}.runTaskLater(Nexus.getPlugin(), 1);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event) {
		Gamer g = Gamer.get(event.getPlayer());
		Block b = event.getBlockPlaced();
		
		if (event.getBlockPlaced().getType() != Material.STONE_PLATE)
			return;
		
		event.setCancelled(false);
	
		Mine mine = new Mine(g, b);
		b.setMetadata("Mine", new FixedMetadataValue(Nexus.getPlugin(), Mine.getList().indexOf(mine)));
	}
	
	@EventHandler
	public void onThrowTNT(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		Gamer g = Gamer.get(p.getName());
		if (g.getVariable("spectator") != null)
			return;

		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (p.getItemInHand().getType() == Material.TNT) {
				// Frag Grenades
				TNTPrimed e = (TNTPrimed) event.getPlayer().getWorld().spawnEntity(p.getEyeLocation(),	EntityType.PRIMED_TNT);
				e.setFuseTicks(38);
				e.setYield(2.5F);
				e.setVelocity(event.getPlayer().getEyeLocation().getDirection());
				e.setMetadata("Player", new FixedMetadataValue(Nexus.getPlugin(), g.getPlayer().getName()));
				e.setMetadata("Cause", new FixedMetadataValue(Nexus.getPlugin(), CustomDamageCause.TNT_GRENADE.name()));
				
				event.setUseItemInHand(Event.Result.DENY);
				ItemStack stack = null;
				if (event.getItem().getAmount() > 1) {
					ItemStack s = event.getItem();
					s.setAmount(event.getItem().getAmount() - 1);
					stack = s;
				}
				p.getInventory().setItem(p.getInventory().getHeldItemSlot(), stack);
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Gamer g = Gamer.get(event.getPlayer());
		
		if (event.getAction() != Action.PHYSICAL)
			return;
		
		Block b = event.getClickedBlock();
		if (!b.hasMetadata("Mine"))
			return;
		
		Mine mine = Mine.getList().get(b.getMetadata("Mine").get(0).asInt());
		
		Team playerTeam = Nexus.getRotary().getCurrentMap().getTeam(g);
		Team targetTeam = Nexus.getRotary().getCurrentMap().getTeam(mine.getGamer());
		
		if (playerTeam == targetTeam)
			return;
		
		mine.setIgnited(true);
		
		TNTPrimed e = (TNTPrimed) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getEyeLocation(), EntityType.PRIMED_TNT);
		e.setFuseTicks(10);
		e.setYield(2.7F);
		e.setMetadata("Player", new FixedMetadataValue(Nexus.getPlugin(), mine.getGamer().getName()));
		e.setMetadata("Cause", new FixedMetadataValue(Nexus.getPlugin(), CustomDamageCause.TNT_MINE.name()));
		event.getClickedBlock().setType(Material.AIR);
	}
}
