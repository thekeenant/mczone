package co.mczone.sg;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.api.players.Gamer;
import co.mczone.api.server.Hive;
import co.mczone.sg.api.Game;
import co.mczone.sg.api.GamerSG;
import co.mczone.sg.api.Map;
import co.mczone.sg.api.State;
import co.mczone.sg.events.GameEvents;
import co.mczone.util.Chat;

import lombok.Getter;
import lombok.Setter;

public class Scheduler extends TimerTask {
	@Getter @Setter static int seconds = 0;
	@Getter @Setter static State state = State.PREP;
	int countdown = SurvivalGames.getInstance().getConfigAPI().getInt("countdown", 120);
	int compass = SurvivalGames.getInstance().getConfigAPI().getInt("compass", 1200);
	int minPlayers = 5;
	
	
	boolean deathmatch = false;
		
	@Override
	public void run() {
		new BukkitRunnable() {
			@Override
			public void run() {
				SurvivalGames.getMainWorld().setTime(6000);
			}
		}.runTask(SurvivalGames.getInstance());
		if (getState() == State.PREP) {
			if (seconds % 30 == 0 && seconds < countdown || (seconds >= countdown - 5 && seconds < countdown))
				Chat.server("&2[SG] &aVoting period ends in &4" + Chat.time(countdown - seconds) + "");
			
			if (seconds > countdown) {
				if (GamerSG.getTributes().size() < minPlayers) {
					Chat.server("&4[SG] &eRestarting countdown, need more players!");
					seconds = 0;
					return;
				}
				Map.setCurrent(Map.getTopVoted());
				Map m = Map.getCurrent();
				m.loadWorld();
				Chat.server("&2[SG] &6Map chosen: &f" + m.getTitle() + " &8[&7" + m.getVotes().size() + " votes&8]");
				
				seconds = 0;
				setState(State.WAITING);
				return;
			}
			
			if (GamerSG.getTributes().size() >= 24 && seconds < (countdown - 30)) {
				Chat.server("&2[SG] &aShortening time &410 seconds");
				seconds = countdown - 10;
			}
		}
		else if (getState() == State.WAITING) {
			Shop.setAllow(true);
			if (seconds >= 30) {
				Bukkit.getPluginManager().registerEvents(new GameEvents(), SurvivalGames.getInstance());
	        	Chat.server("&4# # # # # # # # # # # # # # # #");
	        	Chat.server("&4# # &6The Match has started! &4# #");
	        	Chat.server("&4# # # # # # # # # # # # # # # #");
	        	
		        Hive.getInstance().getDatabase().syncUpdate("INSERT INTO sg_games (start) values (now())");
				ResultSet r = Hive.getInstance().getDatabase().query("SELECT id FROM sg_games ORDER BY start DESC LIMIT 1");
				try {
					while (r.next()) {
						Game game = new Game();
						game.setGameID(r.getInt("id"));
						SurvivalGames.setGame(game);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				new BukkitRunnable() {
					@Override
					public void run() {
						SurvivalGames.getGame().start();
					}
				}.runTask(SurvivalGames.getInstance());
	        	
				seconds = 0;
				setState(State.PVP);
				return;
			}
			
			if (seconds % 5 == 0 || seconds >= 20)
				Chat.server("&2[SG] &aGame beginning in &4" + Chat.time(30 - seconds));
		}
		else if (getState() == State.PVP) {
			Shop.setAllow(false);
			List<GamerSG> gamers = GamerSG.getTributes();
			if (gamers.size() == 0)
				Bukkit.shutdown();
			if (gamers.size() == 1) {
				if (SurvivalGames.getWinner() == null) {
					Player p = gamers.get(0).getPlayer();
					SurvivalGames.setWinner(p);
					Chat.player(p, "&2[SG] &eYou have won this match! &8[&75 credits&8]");
					Gamer.get(p).giveCredits(5);
					seconds = 0;
					SurvivalGames.getGame().finishGame(p);
					return;
				}
				else {
					if (seconds % 5 == 0)
						Chat.server("&2[SG] &eServer restarting for a new game!");
					
					if (seconds >= 24) {
						new BukkitRunnable() {
							@Override
							public void run() {
								for (GamerSG g : GamerSG.getList()) {
									if (SurvivalGames.getWinner().getName().equals(g.getName())) {
										g.getPlayer().kickPlayer(Chat.colors("&2[SG] &eYou have won this match! &8[&710 credits&8]"));
										Gamer.get(g.getPlayer()).giveCredits(10);
									}
									else
										g.getPlayer().kickPlayer(Chat.colors("&2[SG] &eServer restarting for a new game!"));
									Bukkit.shutdown();
								}
							}
						}.runTask(SurvivalGames.getInstance());
					}
				}
			}
			
			if (seconds % 5 == 0) {
				GamerSG.hideSpectators();
			}
			
			if (deathmatch == false && (Bukkit.getOnlinePlayers().length <= 5 || seconds == compass)) {
				deathmatch = true;
				Chat.server("&4[SG] &eCompasses have been dropped at your location!");
				Chat.server("&4[SG] &eLeft-click with a compass to find an enemy.");
				new BukkitRunnable() {
					@Override
					public void run() {
						for (Player p : Bukkit.getOnlinePlayers())
							p.getWorld().dropItem(p.getLocation(), new ItemStack(Material.COMPASS));
					}
				}.runTask(SurvivalGames.getInstance());
			}
		}
		
		
		seconds += 1;
	}
}
