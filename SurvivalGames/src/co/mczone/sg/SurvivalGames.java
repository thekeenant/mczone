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
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.api.players.Gamer;
import co.mczone.api.players.GamerRunnable;
import co.mczone.api.server.GameType;
import co.mczone.api.server.Hive;
import co.mczone.sg.api.ConfigAPI;
import co.mczone.sg.api.Game;
import co.mczone.sg.api.Map;
import co.mczone.sg.cmds.CmdBase;
import co.mczone.sg.events.ConnectEvents;
import co.mczone.sg.events.Events;
import co.mczone.sg.events.VotePanelEvents;
import co.mczone.util.Chat;

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
		Bukkit.getPluginManager().registerEvents(new VotePanelEvents(), this);

		game = new Game();
		timer.scheduleAtFixedRate(new Scheduler(), 0, 1000);

		/*
		new ShopItemStack("steak", "Cooked Steak", new ItemStack(Material.COOKED_BEEF), 15);
		new ShopItemStack("apples", "Golden Apple", new ItemStack(Material.GOLDEN_APPLE), 30);
		new ShopItemStack("iron", "Iron Ingot", new ItemStack(Material.IRON_INGOT), 300);
		new ShopItemStack("diamond", "Diamond", new ItemStack(Material.DIAMOND), 500);
		new ShopItemStack("fishing", "Fishing Rod", new ItemStack(Material.FISHING_ROD), 10);
		new ShopAbility("speed", "Speed I (15 sec)", new PotionEffect(PotionEffectType.SPEED, 20 * 15, 1), 30);
		new ShopAbility("invisible", "Invisibility (15 sec)", new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 15, 1), 100);
		new ShopEnchantment("knockback", "Knockback I", Enchantment.KNOCKBACK, 1, 25);
		new ShopEnchantment("sharpness", "Sharpness I", Enchantment.DAMAGE_ALL, 1, 75);
		*/
		
		Hive.getInstance().setType(GameType.SURVIVAL_GAMES);
		
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
		
		Gamer.addFunction("give-items", new GamerRunnable() {
			@Getter @Setter boolean sync = true;
			
			@Override
			public void run() {
				ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
		        BookMeta data = (BookMeta) book.getItemMeta();
		        data.setAuthor(Chat.colors("&7&oMC Zone"));
		        data.setTitle(Chat.colors("&2&lSurvival Games"));
		        String page1 = "     ";
		        String page2 = "";
		        
		        // Page 1
		        page1 += "&3&lSurvival Games\n";
		        page1 += "    &6&oHow To Guide\n";
		        page1 += "\n";
		        page1 += "&0The Survival Games is a minigame based on the popular book series, The Hunger Games. Fight to the death against 23 other players in a map filled with traps, puzzles, and items. Commands are on the next page.";
		        page1 += "\n\n&7www.nxmc.org";
		        // Page 2
		        page2 += "       &4&oCommands\n";
		        page2 += "\n";
		        page2 += "&6/leave &0Spectate game\n";
		        page2 += "&6/help &0Show commands\n";
		        page2 += "&6/vote &0Vote for a map\n";
		        page2 += "&6/spawn &0TP to the lobby";
		        
		        data.setPages(Chat.colors(page1), Chat.colors(page2));
		        book.setItemMeta(data);
		        gamer.giveItem(book);
		        

				ItemStack item = new ItemStack(Material.EMERALD);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(Chat.colors("&aVoting Panel"));
				item.setItemMeta(meta);
				gamer.giveItem(item);
			}
		});
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
