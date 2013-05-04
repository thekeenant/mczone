package co.mczone.sg;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.base.MCZone;
import co.mczone.base.api.shop.ShopAbility;
import co.mczone.base.api.shop.ShopEnchantment;
import co.mczone.base.api.shop.ShopItemStack;
import co.mczone.sg.api.ConfigAPI;
import co.mczone.sg.api.Game;
import co.mczone.sg.api.Map;
import co.mczone.sg.cmds.CmdBase;
import co.mczone.sg.events.ConnectEvents;
import co.mczone.sg.events.Events;

public class SurvivalGames extends JavaPlugin {
	@Getter static SurvivalGames instance;
	@Getter static Timer timer = new Timer();
	@Getter @Setter static Player winner;
	@Getter @Setter static List<ItemStack> items = new ArrayList<ItemStack>();
	@Getter @Setter ConfigAPI configAPI;
	
	@Getter @Setter static Game game;
	
	public void onEnable() {
		instance = this;
		configAPI = new ConfigAPI();
		new CmdBase();
		Map.load();
		

		Bukkit.getPluginManager().registerEvents(new ConnectEvents(), this);
		Bukkit.getPluginManager().registerEvents(new Events(), this);
		
		timer.scheduleAtFixedRate(new Scheduler(), 0, 1000);

		new ShopItemStack("steak", "Cooked Steak", new ItemStack(Material.COOKED_BEEF), 15);
		new ShopItemStack("apples", "Golden Apple", new ItemStack(Material.GOLDEN_APPLE), 30);
		new ShopItemStack("iron", "Iron Ingot", new ItemStack(Material.IRON_INGOT), 300);
		new ShopItemStack("diamond", "Diamond", new ItemStack(Material.DIAMOND), 500);
		new ShopItemStack("fishing", "Fishing Rod", new ItemStack(Material.FISHING_ROD), 10);
		new ShopAbility("speed", "Speed I (15 sec)", new PotionEffect(PotionEffectType.SPEED, 20 * 15, 1), 30);
		new ShopAbility("invisible", "Invisibility (15 sec)", new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 15, 1), 100);
		new ShopEnchantment("knockback", "Knockback I", Enchantment.KNOCKBACK, 1, 25);
		new ShopEnchantment("sharpness", "Sharpness I", Enchantment.DAMAGE_ALL, 1, 75);
		
		MCZone.setServerType("The Survival Games");
		MCZone.setTitanKickFullServer(true);

		ItemStack item = new ItemStack(Material.WOOD_SWORD);
		item.addEnchantment(Enchantment.KNOCKBACK, 1);
		for (int i = 0; i < 3; i++)
			items.add(item);
		
		item = new ItemStack(Material.WOOD_SWORD);
		item.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		for (int i = 0; i < 3; i++)
			items.add(item);
		
		item = new ItemStack(Material.BOW);
		item.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		for (int i = 0; i < 3; i++)
			items.add(item);
		
		item = new ItemStack(Material.BOW);
		item.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
		for (int i = 0; i < 3; i++)
			items.add(item);
		
		item = new ItemStack(Material.GOLD_SWORD);
		item.addEnchantment(Enchantment.FIRE_ASPECT, 1);
		for (int i = 0; i < 1; i++)
			items.add(item);

		for (int i = 0; i < 5; i++)
			items.add(new ItemStack(Material.WOOD_SWORD));
		for (int i = 0; i < 5; i++)
			items.add(new ItemStack(Material.GOLD_SWORD));
		for (int i = 0; i < 4; i++)
			items.add(new ItemStack(Material.STONE_SWORD));
		for (int i = 0; i < 4; i++)
			items.add(new ItemStack(Material.STONE_AXE));
		for (int i = 0; i < 2; i++)
			items.add(new ItemStack(Material.CAKE));
		for (int i = 0; i < 5; i++)
			items.add(new ItemStack(Material.GOLD_AXE));
		for (int i = 0; i < 5; i++)
			items.add(new ItemStack(Material.WOOD_AXE));
		for (int i = 0; i < 3; i++)
			items.add(new ItemStack(Material.IRON_AXE));
		for (int i = 0; i < 2; i++)
			items.add(new ItemStack(Material.BOWL));
		for (int i = 0; i < 6; i++)
			items.add(new ItemStack(Material.BUCKET));
		for (int i = 0; i < 2; i++)
			items.add(new ItemStack(Material.BOAT));
		for (int i = 0; i < 2; i++)
			items.add(new ItemStack(Material.LEATHER_BOOTS));
		for (int i = 0; i < 5; i++)
			items.add(new ItemStack(Material.LEATHER_LEGGINGS));
		for (int i = 0; i < 6; i++)
			items.add(new ItemStack(Material.LEATHER_CHESTPLATE));
		for (int i = 0; i < 6; i++)
			items.add(new ItemStack(Material.LEATHER_HELMET));
		for (int i = 0; i < 3; i++)
			items.add(new ItemStack(Material.IRON_CHESTPLATE));
		for (int i = 0; i < 3; i++)
			items.add(new ItemStack(Material.IRON_HELMET));
		for (int i = 0; i < 3; i++)
			items.add(new ItemStack(Material.IRON_LEGGINGS));
		for (int i = 0; i < 3; i++)
			items.add(new ItemStack(Material.IRON_LEGGINGS));
		for (int i = 0; i < 10; i++)
			items.add(new ItemStack(Material.WHEAT));
		for (int i = 0; i < 10; i++)
			items.add(new ItemStack(Material.APPLE));
		for (int i = 0; i < 3; i++)
			items.add(new ItemStack(Material.GOLDEN_APPLE));
		for (int i = 0; i < 5; i++)
			items.add(new ItemStack(Material.ARROW, 8));
		for (int i = 0; i < 6; i++)
			items.add(new ItemStack(Material.STRING));
		for (int i = 0; i < 3; i++)
			items.add(new ItemStack(Material.IRON_SWORD));
		for (int i = 0; i < 8; i++)
			items.add(new ItemStack(Material.IRON_INGOT));
		for (int i = 0; i < 5; i++)
			items.add(new ItemStack(Material.DIAMOND));
		for (int i = 0; i < 3; i++)
			items.add(new ItemStack(Material.BOW));
		for (int i = 0; i < 2; i++)
			items.add(new ItemStack(Material.POTION, 1, (short) 16417));
		for (int i = 0; i < 3; i++)
			items.add(new ItemStack(Material.POTION, 1, (short) 16389));
		for (int i = 0; i < 3; i++)
			items.add(new ItemStack(Material.POTION, 1, (short) 8195));
		for (int i = 0; i < 3; i++)
			items.add(new ItemStack(Material.POTION, 1, (short) 8195));
		for (int i = 0; i < 3; i++)
			items.add(new ItemStack(Material.FLINT));
		for (int i = 0; i < 5; i++)
			items.add(new ItemStack(Material.RAW_BEEF));
		for (int i = 0; i < 2; i++)
			items.add(new ItemStack(Material.CARROT_ITEM, 3));
		for (int i = 0; i < 6; i++)
			items.add(new ItemStack(Material.STICK, 1));
		for (int i = 0; i < 2; i++)
			items.add(new ItemStack(Material.SHEARS, 1));
		for (int i = 0; i < 3; i++)
			items.add(new ItemStack(Material.BREAD, 4));
		
		
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Entity e : getMainWorld().getEntities()) {
					if (e instanceof Monster)
						e.remove();
				}
			}
		}.runTask(SurvivalGames.getInstance());
	}
	
	public static World getMainWorld() {
		return Bukkit.getWorld("world");
	}
	
	public void onDisable() {
		for (Player p : Bukkit.getOnlinePlayers())
			p.kickPlayer("Restaring server!");
		
		if (Map.getCurrent() != null)
			Bukkit.unloadWorld(Map.getCurrent().getWorldName(), false);
	}
	
}
