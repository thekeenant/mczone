package co.mczone.ghost.api;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import co.mczone.api.players.Gamer;
import co.mczone.api.server.Hive;
import co.mczone.ghost.Ghost;
import co.mczone.ghost.schedules.ArenaSchedule;
import co.mczone.util.Chat;

import lombok.Getter;
import lombok.Setter;

public class Arena {
	public static int MAX_PER_TEAM = Ghost.getConf().getInt("max-per-team", 1);
	public static int MIN_PER_TEAM = Ghost.getConf().getInt("min-per-team", 1);
	public static boolean TRACK_GAMES = Ghost.getConf().getBoolean("track-games", false);
	@Getter static List<Arena> list = new ArrayList<Arena>();
	
	@Getter String name;
	@Getter int id;
	
	@Getter @Setter Block signBlock;
	Sign sign;
	
	@Getter int gameID;
	
	@Getter Scoreboard scoreboard;
	@Getter Objective sidebar;
	@Getter Team red;
	@Getter Team blue;
	
	@Getter ArenaState state;
	@Getter ArenaSchedule schedule;
	
	// Force world over the map
	@Getter String worldName;
	
	@Getter @Setter boolean starting;
	
	@Getter int index;
	@Getter List<Map> maps;
	
	public Arena(String name, String worldName, int id, Block sign, List<Map> maps) {
		this.name = name;
		this.id = id;
		this.worldName = worldName;		
		this.maps = maps;
		this.index = 0;
		this.signBlock = sign;
		
		this.state = ArenaState.WAITING;
		this.schedule = new ArenaSchedule(this);
		this.schedule.runTaskTimerAsynchronously(Ghost.getInstance(), 0, 20);
		
		registerTeams();
		loadWorld();
		
		list.add(this);
	}
	
	public void next() {
		int max = maps.size() - 1;
		int next = index + 1;
		if (next > max)
			next = 0;
		
		index = next;
	}
	
	public void startGame() {
    	sendMessage("&4# # # # # # # # # # # # # # # #");
    	sendMessage("&4# # &6The match has started &4# #");
    	sendMessage("&4# # # # # # # # # # # # # # # #");
		setState(ArenaState.STARTED);
		schedule.setTime(0);
		setStarting(false);
		
		for (Entity e : getWorld().getEntities())
			if (e instanceof Monster)
				e.remove();

		
		String query = "INSERT INTO ghost_player (username, game_id, kit, team) VALUES ";		
		boolean track = Arena.TRACK_GAMES;
		if (track) {
			String game = "INSERT INTO ghost_games (start) VALUES (now())";
			Hive.getInstance().getDatabase().syncUpdate(game);
			
			ResultSet r = Hive.getInstance().getDatabase().query("SELECT id FROM ghost_games ORDER BY id DESC");
			try {
				while (r.next())
					gameID = r.getInt("id");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		for (Player p : getRedPlayers()) {
			Gamer g = Gamer.get(p);
			p.teleport(this.getRedSpawn());
			
			p.setFallDistance(0.0F);
			p.setVelocity(new Vector(0,0,0));
			p.setFlying(false);
			p.setAllowFlight(false);
			p.setFallDistance(0.0F);
			
			p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2147483647, 0), true);
			g.setVariable("inMatch", true);
			
			String kit = "";
			if (g.getVariable("kit") != null)
				kit = ((Kit) g.getVariable("kit")).getName().toLowerCase();
			
			query += "('" + p.getName() + "'," + gameID + ",'" + kit + "','red'),";
		}
		
		for (Player p : getBluePlayers()) {
			Gamer g = Gamer.get(p);
			p.teleport(this.getBlueSpawn());
			p.setFlying(false);
			p.setAllowFlight(false);
			p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2147483647, 0), true);
			g.setVariable("inMatch", true);
			
			String kit = "";
			if (g.getVariable("kit") != null)
				kit = ((Kit) g.getVariable("kit")).getName().toLowerCase();
			
			query += "('" + p.getName() + "'," + gameID + ",'" + kit + "','blue'),";
		}
		
		query = query.substring(0, query.length() - 1);
		
		if  (track)
			Hive.getInstance().getDatabase().update(query);
	}
	
	public void endGame() {
		int blue = getBluePlayers().size();
		int red = getRedPlayers().size();
		
		if (red == 0 || blue > red)
        	Chat.server("  &4\u00BB &eBlue Team has won in &bARENA " + id + "&e on &b" + getCurrent().getTitle().toUpperCase() + " &4\u00AB");
		else if (blue == 0 || red > blue)
        	Chat.server("  &4\u00BB &eRed Team has won in &bARENA " + id + "&e on &b" + getCurrent().getTitle().toUpperCase() + " &4\u00AB");		
		
		scoreboard.clearSlot(DisplaySlot.SIDEBAR);
		setState(ArenaState.WAITING);
		schedule.setTime(0);
		schedule.resetCountdown();
		
		next();
		List<Player> players = getPlayers();
		registerTeams();
		
		for (Player p : players) {
			Gamer g = Gamer.get(p);
			p.teleport(getSpawn());
			g.setInvisible(false);
			g.removePotionEffects();
			
			p.setHealth(20);
			Kit.giveKit(p);
			
			if (g.getVariable("team") == null)
				join(p);
			else
				join(p, (String) g.getVariable("team"));
		}
		
		if (TRACK_GAMES) {
			Hive.getInstance().getDatabase().update("UPDATE ghost_games SET end=now() WHERE id=" + gameID);
		}
	}
	
	public void loadWorld() {
		new WorldCreator(worldName).createWorld();
		new WorldCreator(getCurrent().worldName).createWorld();
		getWorld().setAutoSave(false);
		setState(ArenaState.WAITING);
		
		for (Entity e : getWorld().getEntities())
			if (e instanceof Monster)
				e.remove();
	}
	
	private void registerTeams() {
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		red = scoreboard.registerNewTeam("red");
		blue = scoreboard.registerNewTeam("blue");

		// Apparently, opacity doesn't work with friendly fire >:(
		red.setDisplayName("Red");
		//red.setAllowFriendlyFire(false);
		red.setCanSeeFriendlyInvisibles(true);
		red.setPrefix(ChatColor.RED + "");

		blue.setDisplayName("Blue");
		//blue.setAllowFriendlyFire(false);
		blue.setCanSeeFriendlyInvisibles(true);
		blue.setPrefix(ChatColor.BLUE + "");

		sidebar = scoreboard.registerNewObjective("test", "dummy");
		sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
		sidebar.setDisplayName("Teams (0 means dead)");
	}
	
	public void updateScoreboard() {
		if (state == ArenaState.WAITING && !starting)
			setScore("&aCountdown", schedule.getCountdown());
		else
			clearScore("&aCountdown");
			
		// Team Red
		for (OfflinePlayer p : red.getPlayers()) {
			String name = p.getName();
			if (Gamer.get(p.getName()).isInvisible())
				name = ChatColor.RED + "" + ChatColor.STRIKETHROUGH + p.getName();
			
			if (name.length() > 16)
				name = name.substring(0, 16);
			
			if (p.isOnline())
				// Invisible means dead
				if (Gamer.get(p.getName()).isInvisible()) {
					clearScore(p.getName());
					setScore(name, 2);
					continue;
				}
			setScore(name, 2);
		}
		
		// Team Blue
		for (OfflinePlayer p : blue.getPlayers()) {
			String name = p.getName();
			if (Gamer.get(p.getName()).isInvisible())
				name = ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + p.getName();
			
			if (name.length() > 16)
				name = name.substring(0, 16);
			
			if (p.isOnline())
				// Invisible means dead
				if (Gamer.get(p.getName()).isInvisible()) {
					clearScore(p.getName());
					setScore(name, 1);
					continue;
				}
			setScore(name, 1);
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
	
	public List<Player> getBluePlayers() {
		List<Player> list = new ArrayList<Player>();
		for (OfflinePlayer p : blue.getPlayers())
			if (p.isOnline() && !Gamer.get(p.getName()).isInvisible())
					list.add(p.getPlayer());
		return list;
	}
	
	public World getWorld() {
		return Bukkit.getWorld(getCurrent().worldName);
	}
	
	public TeamColor join(Player p) {
		String team = (this.getRedPlayers().size() > this.getBluePlayers().size()) ? "blue" : "red";
		return join(p, team);
	}
	
	public TeamColor join(Player p, String team) {
		if (team.equals("spec")) {
			// something special?
		}
		
		if (team.equals("red"))
			red.addPlayer(p);
		else if (team.equals("blue"))
			blue.addPlayer(p);
		
		Gamer g = Gamer.get(p);
		g.setVariable("arena", this);
		g.setVariable("team", team);
	
		p.setFlySpeed(0.1F);
		
		p.setAllowFlight(true);
		p.setFlying(true);
		p.setScoreboard(scoreboard);
		p.teleport(getSpawn());
		return TeamColor.valueOf(team.toUpperCase());
	}

	public void leave(Player p) {
		clearScore(p.getName());
		Gamer g = Gamer.get(p);
		g.clearVariable("inMatch");
		g.clearVariable("team");
		g.setFlying(false);
		g.setAllowFlight(false);
		g.removePotionEffects();
		scoreboard.getPlayerTeam(Bukkit.getOfflinePlayer(p.getName())).removePlayer(Bukkit.getOfflinePlayer(p.getName()));
		g.clearScoreboard();
	}

	public void updateSign() {
		if (getSign() == null)
			return;
		
		Sign sign = getSign();
		sign.setLine(0, "[ARENA " + getId() + "]");
		sign.setLine(1, Chat.colors("&o" + getState().getColor() + getState().name()));
		sign.setLine(2, Chat.colors("&l" + getCurrent().getTitle().toUpperCase()));
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
			if (a.getName().equalsIgnoreCase(string))
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
	
	public Map getCurrent() {
		return maps.get(index);
	}
	
	// Maps
	
	public Location getRedSpawn() {
		Location l = getCurrent().getRedSpawn();
		l.setWorld(Bukkit.getWorld(worldName));
		return l;
	}
	
	public Location getBlueSpawn() {
		Location l = getCurrent().getBlueSpawn();
		l.setWorld(Bukkit.getWorld(worldName));
		return l;
	}
	
	public Location getSpawn() {
		Location l = getCurrent().getSpawn();
		l.setWorld(Bukkit.getWorld(worldName));
		return l;
	}
}
