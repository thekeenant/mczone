package co.mczone.ghost.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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

import co.mczone.api.players.Gamer;
import co.mczone.ghost.Ghost;
import co.mczone.ghost.schedules.ArenaSchedule;
import co.mczone.util.Chat;

import lombok.Getter;
import lombok.Setter;

public class Arena {
	public static int MAX_PER_TEAM = Ghost.getConf().getInt("max-per-team", 1);
	
	@Getter static List<Arena> list = new ArrayList<Arena>();
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
	
	@Getter ArenaState state;
	@Getter ArenaSchedule schedule;

	@Getter Location spawn;
	@Getter Location redSpawn;
	@Getter Location blueSpawn;
	
	public Arena(int id, String title, String world, Block sign, Location spawn, Location redSpawn, Location blueSpawn) {
		this.id = id;
		this.title = title;
		this.worldName = world;
		this.spawn = spawn;
		this.redSpawn = redSpawn;
		this.blueSpawn = blueSpawn;
		this.signBlock = sign;
		
		if (signBlock.getType() == Material.WALL_SIGN || signBlock.getType() == Material.SIGN)
			this.sign = (Sign) signBlock.getState();
		
		this.state = ArenaState.WAITING;
		this.schedule = new ArenaSchedule(this);
		this.schedule.runTaskTimerAsynchronously(Ghost.getInstance(), 0, 20);
		
		registerTeams();
		loadWorld();
		
		list.add(this);
	}
	
	public void startGame() {
    	Chat.server("&4# # # # # # # # # # # # # # # #");
    	Chat.server("&4# # &6The match has started &4# #");
    	Chat.server("&4# # # # # # # # # # # # # # # #");
		setState(ArenaState.STARTED);

		for (Player p : getRedPlayers()) {
			p.teleport(this.getRedSpawn());
		}
		
		for (Player p : getBluePlayers()) {
			p.teleport(this.getBlueSpawn());
		}
	}
	
	public void endGame() {
		// Kick all players out of the match

		scoreboard.clearSlot(DisplaySlot.SIDEBAR);
		setState(ArenaState.LOADING);
		schedule.setTime(0);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player p : getPlayers()) {
					p.teleport(Ghost.getLobby().getSpawn());
					Gamer.get(p).setInvisible(false);
				}
				registerTeams();
			}
		}.runTask(Ghost.getInstance());
		
		new BukkitRunnable() {
			@Override
			public void run() {
				setState(ArenaState.WAITING);
			}
		}.runTaskLater(Ghost.getInstance(), 60);
	}
	
	public void loadWorld() {
		new WorldCreator(worldName).createWorld();
		getWorld().setAutoSave(false);
		setState(ArenaState.WAITING);
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
		sidebar.setDisplayName("Teams (0 means dead)");
	}
	
	@Getter List<String> dead = new ArrayList<String>();
	public void updateScoreboard() {
		// Team Red
		for (OfflinePlayer p : red.getPlayers()) {
			if (p.isOnline())
				if (dead.contains(p.getName()))
					continue;
			setScore(p.getName(), 2);
		}
		
		// Team Blue
		for (OfflinePlayer p : blue.getPlayers()) {
			if (p.isOnline())
				if (dead.contains(p.getName()))
					continue;
			setScore(p.getName(), 1);
		}
		
		// Dead/Spectators		
		for (String s : dead) {
			setScore(s, 0);
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
	
	public Team getTeam(Player p) {
		if (getRedPlayers().contains(p))
			return red;
		if (getBluePlayers().contains(p))
			return blue;
		return null;
	}
	
	public List<Player> getRedPlayers() {
		List<Player> list = new ArrayList<Player>();
		for (OfflinePlayer p : red.getPlayers())
				if (p.isOnline() && !dead.contains(p.getName()))
					list.add(p.getPlayer());
		return list;
	}
	
	public List<Player> getBluePlayers() {
		List<Player> list = new ArrayList<Player>();
		for (OfflinePlayer p : blue.getPlayers())
			if (p.isOnline() && !dead.contains(p.getName()))
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
		
		Gamer.get(p).setVariable("arena", this);
		
		p.setScoreboard(scoreboard);
		p.teleport(spawn);
		sendMessage("  &7&o + " + p.getName() + " has joined the arena");
		return TeamColor.valueOf(team.toUpperCase());
	}

	public void leave(Player p) {
		scoreboard.getPlayerTeam(Bukkit.getOfflinePlayer(p.getName())).removePlayer(p);
		sendMessage("  &7&o - " + p.getName() + " has left the arena");
	}

	public void updateSign() {
		if (getSign() == null)
			return;
		
		Sign sign = getSign();
		sign.setLine(0, "[ARENA-" + getId() + "]");
		sign.setLine(1, Chat.colors("&o" + getState().getColor() + getState().name()));
		sign.setLine(2, Chat.colors("&l" + getTitle()));
		sign.setLine(3, getPlayers().size() + "/" + Arena.MAX_PER_TEAM * 2 + " players");
		sign.update(true);
	}
	
	public void sendMessage(String msg) {
		Chat.player(getPlayers(), msg);
	}
	
	public static Arena getMatch(Player player) {
		return (Arena) Gamer.get(player).getVariable("match");
	}
	
	public void setState(ArenaState state) {
		this.state = state;
		updateSign();
	}
}