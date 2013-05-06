package co.mczone.ghost.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import co.mczone.ghost.Ghost;
import co.mczone.ghost.schedules.MatchSchedule;
import co.mczone.util.Chat;
import co.mczone.util.FileUtil;

import lombok.Getter;
import lombok.Setter;

public class Match {
	public static int MAX_PER_TEAM = Ghost.getConf().getInt("max-per-team", 8);
	
	@Getter static List<Match> list = new ArrayList<Match>();
	@Getter int id;
	@Getter String worldName;
	@Getter String title;
	@Getter Block signBlock;
	@Getter Sign sign;
	
	@Getter Scoreboard scoreboard;
	@Getter Objective sidebar;
	@Getter Team red;
	@Getter Team blue;
	@Getter Team spec;
	
	@Getter @Setter MatchState state;
	@Getter MatchSchedule schedule;
	
	public Match(int id, String title, String world, Block sign) {
		this.id = id;
		this.title = title;
		this.worldName = world;
		this.signBlock = sign;
		
		if (signBlock.getType() == Material.WALL_SIGN || signBlock.getType() == Material.SIGN)
			this.sign = (Sign) signBlock.getState();
		
		this.state = MatchState.WAITING;
		this.schedule = new MatchSchedule(this);
		this.schedule.runTaskTimerAsynchronously(Ghost.getInstance(), 0, 20);
		
		registerTeams();
		loadWorld();
		
		list.add(this);
	}
	
	public void startGame() {
		setState(MatchState.STARTED);

		for (Player p : getRedPlayers()) {
			// p.teleport
		}
		
		for (Player p : getBluePlayers()) {
			// p.teleport
		}
	}
	
	public void endGame() {
		setState(MatchState.LOADING);
		schedule.setTime(0);
		
		// Kick all players out of the match
		for (Player p : getPlayers()) {
			p.teleport(Ghost.getLobby().getSpawnLocation());
		}
		
		// Run this a bit later, no lag :)
		new BukkitRunnable() {
			@Override
			public void run() {
				unloadWorld();
			}
		}.runTaskLater(Ghost.getInstance(), 120);
	}
	
	public void loadWorld() {
		FileUtil.delete(new File(worldName));
		try {
			FileUtil.copy(new File("backups", worldName), new File(worldName));
		} catch (IOException e) {
			Chat.log("Failed loading match world: " + worldName);
			e.printStackTrace();
		}
		new WorldCreator(worldName).createWorld();
		getWorld().setAutoSave(false);
		setState(MatchState.WAITING);
	}
	
	// Should by called on main thread!
	public void unloadWorld() {
		// Any players left for some reason?
		for (Player p : getWorld().getPlayers())
			p.teleport(Ghost.getLobby().getSpawnLocation());
		
		boolean saved = Bukkit.unloadWorld(worldName, false);
		if (!saved) {
			Chat.log(Level.SEVERE, "Failed to unload world: " + worldName);
		}
	}
	
	private void registerTeams() {
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		red = scoreboard.registerNewTeam("red");
		blue = scoreboard.registerNewTeam("blue");
		spec = scoreboard.registerNewTeam("spec");

		// Apparently, opacity doesn't work with friendly fire >:(
		red.setDisplayName("Red");
		//red.setAllowFriendlyFire(false);
		red.setCanSeeFriendlyInvisibles(true);
		red.setPrefix(ChatColor.RED + "");

		blue.setDisplayName("Blue");
		//blue.setAllowFriendlyFire(false);
		blue.setCanSeeFriendlyInvisibles(true);
		blue.setPrefix(ChatColor.BLUE + "");

		spec.setDisplayName("Spectators");
		//spec.setAllowFriendlyFire(false);
		spec.setCanSeeFriendlyInvisibles(true);
		spec.setPrefix(ChatColor.GRAY + "");

		sidebar = scoreboard.registerNewObjective("test", "dummy");
		sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
		sidebar.setDisplayName(ChatColor.GREEN + "Sidebar");
	}
	
	public void updateScoreboard() {
		List<OfflinePlayer> dead = new ArrayList<OfflinePlayer>();
		
		// Team Red
		for (OfflinePlayer p : red.getPlayers()) {
			if (p.isOnline())
				if (((Player) p).isDead()) {
					dead.add(p);
					continue;
				}
			setScore(p.getName(), 2);
		}
		
		// Team Blue
		for (OfflinePlayer p : blue.getPlayers()) {
			if (p.isOnline())
				if (((Player) p).isDead()) {
					dead.add(p);
					continue;
				}
			setScore(p.getName(), 1);
		}
		
		// Dead/Spectators		
		for (OfflinePlayer p : dead) {
			setScore(p.getName(), 0);
		}
	}
	
	public void setScore(String key, int value) {
		Score score = sidebar.getScore(Bukkit.getOfflinePlayer(key));
		score.setScore(value);
	}
	
	public List<Player> getPlayers() {
		List<Player> list = new ArrayList<Player>();
		for (Team team : scoreboard.getTeams())
			for (OfflinePlayer p : team.getPlayers())
				if (p.isOnline())
					list.add(p.getPlayer());
		return list;
	}
	
	public List<Player> getRedPlayers() {
		List<Player> list = new ArrayList<Player>();
		for (OfflinePlayer p : red.getPlayers())
				if (p.isOnline())
					list.add(p.getPlayer());
		return list;
	}
	
	public List<Player> getBluePlayers() {
		List<Player> list = new ArrayList<Player>();
		for (OfflinePlayer p : blue.getPlayers())
				if (p.isOnline())
					list.add(p.getPlayer());
		return list;
	}
	
	public World getWorld() {
		return Bukkit.getWorld(worldName);
	}
	
	public TeamColor join(Player p) {
		String team = (this.getRedPlayers().size() > this.getBluePlayers().size()) ? "blue" : "red";
		return join(p, team);
	}
	
	public TeamColor join(Player p, String team) {
		if (team.equals("red"))
			red.addPlayer(p);
		else if (team.equals("blue"))
			blue.addPlayer(p);
		else
			spec.addPlayer(p);
		
		p.setScoreboard(scoreboard);
		sendMessage("  &7&o + " + p.getName() + " has joined the match");
		return TeamColor.valueOf(team.toUpperCase());
	}

	public void leave(Player p) {
		scoreboard.getPlayerTeam(Bukkit.getOfflinePlayer(p.getName())).removePlayer(p);
		p.setScoreboard(null);
		sendMessage("  &7&o - " + p.getName() + " has left the match");
	}

	public void updateSign() {
		if (getSign() == null)
			return;
		
		Sign sign = getSign();
		sign.setLine(0, "[Match-" + getId() + "]");
		sign.setLine(1, Chat.colors("&o" + getState().getColor() + getState().name()));
		sign.setLine(2, Chat.colors("&l" + getTitle()));
		sign.setLine(3, getPlayers().size() + "/" + Match.MAX_PER_TEAM * 2 + " players");	
	}
	
	public void sendMessage(String msg) {
		Chat.player(getPlayers(), msg);
	}
	
	public static Match getMatch(Player player) {
		for (Match match : list)
			if (match.getPlayers().contains(player))
				return match;
		return null;
	}
}
