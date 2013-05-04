package co.mczone.sg.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.api.players.Gamer;
import co.mczone.sg.SurvivalGames;
import co.mczone.util.Chat;

import lombok.Getter;
import lombok.Setter;

public class GamerSG extends Gamer {
	@Getter static List<GamerSG> gamers = new ArrayList<GamerSG>();
	@Getter String name;
	@Getter boolean spectator;
	@Getter @Setter boolean moveable;
	@Getter @Setter Location deathLocation;
	@Getter @Setter Location spawnBlock;
	@Getter @Setter int spectating = -1;

	public GamerSG(String name) {
		super(name);
		list.add(this);
	}

	public static List<GamerSG> getTributes() {
		List<GamerSG> gamers = new ArrayList<GamerSG>();
		Iterator<GamerSG> iter = getList().iterator();
		while (iter.hasNext()) {
			GamerSG g = (GamerSG) iter.next();
			if (!g.isSpectator())
				gamers.add(g);
		}
		return gamers;
	}
	
	public static GamerSG get(String username) {
		for (Gamer g : getList()) 
			if (g instanceof GamerSG && g.getName().equals(username))
				return (GamerSG) g;
		return null;
	}
	
	public void setSpectator(boolean set) {
		Player p = getPlayer();
		if (set) {
			spectator = true;
			p.setCanPickupItems(false);
			p.setAllowFlight(true);
			p.setFlying(true);
		}
		else {
			spectator = false;
			p.setFlying(false);
			p.setAllowFlight(false);
			p.setCanPickupItems(true);
		}
	}
	
	public Player getPlayer() {
		for (Player p : Bukkit.getOnlinePlayers())
			if (p.getName().equals(name))
				return p;
		return null;
	}
	
	public void heal() {
		Player p = getPlayer();
		p.setHealth(20);
		p.setFoodLevel(20);
		p.setSaturation(1F);
		p.setGameMode(GameMode.SURVIVAL);
	}
    
    public void clearInventory() {
    	Player player = getPlayer();
    	player.getInventory().clear();
    	player.getInventory().setArmorContents(null);
    	for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());
    }
    
	public static void hideSpectators() {		
		Bukkit.getScheduler().scheduleSyncDelayedTask(SurvivalGames.getInstance(), new BukkitRunnable() {
			@Override
			public void run() {
				List<GamerSG> list = GamerSG.getList();
				List<GamerSG> listB = GamerSG.getList();
				for (GamerSG g : list) {
					for (GamerSG t : listB) {
						if (g == null || g.getPlayer() == null || !g.getPlayer().isOnline()) {
							g.remove();
							continue;
						}
						if (t == null || t.getPlayer() == null || !t.getPlayer().isOnline()) {
							t.remove();
							continue;
						}
						
						if (t.isSpectator())
							g.getPlayer().hidePlayer(t.getPlayer());
						else
							g.getPlayer().showPlayer(t.getPlayer());
					}
				}
			}
		});
	}
	
	public void giveBook() {
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
    	getPlayer().getInventory().addItem(book);
	}
	
	public void remove() {
		final GamerSG g = this;
		new BukkitRunnable() {
			@Override
			public void run() {
				GamerSG.getList().remove(g);
			}
		}.runTask(SurvivalGames.getInstance());
	}
}
