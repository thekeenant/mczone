package co.mczone.skywars.api;

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
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import co.mczone.api.players.Gamer;
import co.mczone.skywars.SkyWars;
import co.mczone.skywars.schedules.ArenaSchedule;
import co.mczone.util.Chat;

import lombok.Getter;
import lombok.Setter;

public class Arena {
	public static int MAX_PER_TEAM = SkyWars.getConf().getInt("max-per-team", 1);
	@Getter static List<Arena> list = new ArrayList<Arena>();
	
	@Getter int id;
	@Getter @Setter String worldName;
	@Getter @Setter String title;
	@Getter @Setter Block signBlock;
	Sign sign;
	
	@Getter Scoreboard scoreboard;
	@Getter Objective sidebar;
	@Getter Team red;
	@Getter Team blue;
	@Getter Team green;
	
	@Getter ArenaState state;
	@Getter ArenaSchedule schedule;

	@Getter @Setter Location spawn;
	@Getter @Setter Location redSpawn;
	@Getter @Setter Location blueSpawn;
	@Getter @Setter Location greenSpawn;
	
	@Getter ConfigurationSection config;
	
	@Getter @Setter boolean starting;
	
	public Arena(ConfigurationSection config, int id, String title, String world, Block sign, Location spawn, Location redSpawn, Location blueSpawn, Location greenSpawn) {
		this.config = config;
		this.id = id;
		this.title = title;
		this.worldName = world;
		this.spawn = spawn;
		this.redSpawn = redSpawn;
		this.blueSpawn = blueSpawn;
		this.greenSpawn = greenSpawn;
		this.signBlock = sign;
		
		this.state = ArenaState.WAITING;
		this.schedule = new ArenaSchedule(this);
		this.schedule.runTaskTimerAsynchronously(SkyWars.getInstance(), 0, 20);
		
		registerTeams();
		loadWorld();
		
		list.add(this);
	}
	
	public void startGame() {
    	sendMessage("&4# # # # # # # # # # # # # # # #");
    	sendMessage("&4# # &6The match has started &4# #");
    	sendMessage("&4# # # # # # # # # # # # # # # #");
		setState(ArenaState.STARTED);
		schedule.setTime(0);
		setStarting(false);

		for (Player p : getRedPlayers()) {
			p.teleport(this.getRedSpawn());
			p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2147483647, 0), true);
			Gamer.get(p).setVariable("inMatch", true);
		}
		
		for (Player p : getBluePlayers()) {
			p.teleport(this.getBlueSpawn());
			p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2147483647, 0), true);
			Gamer.get(p).setVariable("inMatch", true);
		}
		
		for (Player p : getGreenPlayers()) {
			p.teleport(this.getGreenSpawn());
			p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2147483647, 0), true);
			Gamer.get(p).setVariable("inMatch", true);
		}
	}
	
	public void endGame() {
		int blue = getBluePlayers().size();
		int red = getRedPlayers().size();
		int green = getGreenPlayers().size();
		
		if (red == 0 || (blue > red && blue > green))
        	Chat.server("  &4\u00BB &eBlue Team has won in &bARENA " + id + "&e on &b" + getTitle().toUpperCase() + " &4\u00AB");
		else if (blue == 0 || (red > blue && red > green))
        	Chat.server("  &4\u00BB &eRed Team has won in &bARENA " + id + "&e on &b" + getTitle().toUpperCase() + " &4\u00AB");
		else
        	Chat.server("  &4\u00BB &eGreen Team has won in &bARENA " + id + "&e on &b" + getTitle().toUpperCase() + " &4\u00AB");
		
		scoreboard.clearSlot(DisplaySlot.SIDEBAR);
		setState(ArenaState.LOADING);
		schedule.setTime(0);
		schedule.resetCountdown();
		
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player p : getPlayers()) {
					Gamer g = Gamer.get(p);
					g.clearVariable("inMatch");
					p.teleport(SkyWars.getLobby().getSpawn());
					g.setInvisible(false);
					g.removePotionEffects();
					getTeam(p).removePlayer(p);
					p.setHealth(20);
					Kit.giveKit(p);
					
					g.setFlying(false);
					g.setAllowFlight(false);
				}
				registerTeams();
			}
		}.runTask(SkyWars.getInstance());
		
		new BukkitRunnable() {
			@Override
			public void run() {
				setState(ArenaState.WAITING);
			}
		}.runTaskLater(SkyWars.getInstance(), 60);
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
		green = scoreboard.registerNewTeam("green");

		// Apparently, opacity doesn't work with friendly fire >:(
		red.setDisplayName("Red");
		red.setCanSeeFriendlyInvisibles(true);
		red.setPrefix(ChatColor.RED + "");

		blue.setDisplayName("Blue");
		blue.setCanSeeFriendlyInvisibles(true);
		blue.setPrefix(ChatColor.BLUE + "");

		green.setDisplayName("Green");
		green.setCanSeeFriendlyInvisibles(true);
		green.setPrefix(ChatColor.GREEN + "");

		sidebar = scoreboard.registerNewObjective("test", "dummy");
		sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
		sidebar.setDisplayName("Teams");
	}
	
	public void updateScoreboard() {
		if (state == ArenaState.WAITING && !starting)
			setScore("&aCountdown", schedule.getCountdown());
		else
			clearScore("&aCountdown");
			
		// Team Red
		for (OfflinePlayer p : red.getPlayers()) {
			if (p.isOnline())
				// Invisible means dead
				if (Gamer.get(p.getName()).isInvisible()) {
					setScore(ChatColor.STRIKETHROUGH + p.getName(), 3);
					continue;
				}
			setScore(p.getName(), 3);
		}
		
		// Team Blue
		for (OfflinePlayer p : blue.getPlayers()) {
			if (p.isOnline())
				// Invisible means dead
				if (Gamer.get(p.getName()).isInvisible()) {
					setScore(ChatColor.STRIKETHROUGH + p.getName(), 2);
					continue;
				}
			setScore(p.getName(), 2);
		}
		
		// Team Green
		for (OfflinePlayer p : green.getPlayers()) {
			if (p.isOnline())
				// Invisible means dead
				if (Gamer.get(p.getName()).isInvisible()) {
					setScore(ChatColor.STRIKETHROUGH + p.getName(), 1);
					continue;
				}
			setScore(p.getName(), 1);
		}
	}
	
	public void setScore(String key, int value) {
		Score score = sidebar.getScore(Bukkit.getOfflinePlayer(Chat.colors(key)));
		score.setScore(value);
	}
	
	public void clearScore(String key) {
		getScoreboard().resetScores(Bukkit.getOfflinePlayer(Chat.colors(key)));
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
				if (p.isOnline() && !Gamer.get(p.getName()).isInvisible())
					list.add(p.getPlayer());
		return list;
	}
	
	public List<Player> getGreenPlayers() {
		List<Player> list = new ArrayList<Player>();
		for (OfflinePlayer p : green.getPlayers())
				if (p.isOnline() && !Gamer.get(p.getName()).isInvisible())
					list.add(p.getPlayer());
		return list;
	}
	
	public List<Player> getBluePlayers() {
		List<Player> list = new ArrayList<Player>();
		for (OfflinePlayer p : blue.getPlayers())
			if (p.isOnline() && !Gamer.get(p.getName()).isInvisible())
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
		
		Gamer.get(p).setVariable("arena", this);
		
		p.setScoreboard(scoreboard);
		p.teleport(spawn);
		sendMessage("  &7&o + " + p.getName() + " has joined the arena");
		return TeamColor.valueOf(team.toUpperCase());
	}

	public void leave(Player p) {
		clearScore(p.getName());
		Gamer.get(p).clearVariable("inMatch");
		Gamer.get(p).setFlying(false);
		Gamer.get(p).setAllowFlight(false);
		scoreboard.getPlayerTeam(p).removePlayer(p);
		sendMessage("  &7&o - " + p.getName() + " has left the arena");
	}

	public void updateSign() {
		if (getSign() == null)
			return;
		
		Sign sign = getSign();
		sign.setLine(0, "[ARENA " + getId() + "]");
		sign.setLine(1, Chat.colors("&o" + getState().getColor() + getState().name()));
		sign.setLine(2, Chat.colors("&l" + getTitle().toUpperCase()));
		sign.setLine(3, getPlayers().size() + "/" + Arena.MAX_PER_TEAM * 2 + " players");
		sign.update(true);
	}
	
	public void sendMessage(String msg) {
		Chat.player(getPlayers(), msg);
	}
	
	public static Arena getArena(Player player) {
		return (Arena) Gamer.get(player).getVariable("arena");
	}
	
	public void setState(ArenaState state) {
		this.state = state;
		updateSign();
	}
	
	public Sign getSign() {
		if (signBlock.getType() == Material.WALL_SIGN || signBlock.getType() == Material.SIGN)
			this.sign = (Sign) signBlock.getState();
		return this.sign;
	}

	public static Arena get(String string) {
		for (Arena a : getList())
			if (a.getWorldName().equalsIgnoreCase(string))
				return a;
		return null;
	}

	public List<Player> getSpectators() {
		List<Player> list = new ArrayList<Player>();
		for (Player p : getPlayers())
			if (Gamer.get(p).isInvisible())
				list.add(p);
		return list;
	}
}