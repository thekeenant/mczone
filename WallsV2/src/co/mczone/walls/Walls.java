package co.mczone.walls;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.api.server.GameType;
import co.mczone.api.server.Hive;
import co.mczone.walls.commands.*;
import co.mczone.walls.events.*;
import co.mczone.walls.timers.*;
import co.mczone.walls.utils.*;

public class Walls extends JavaPlugin {
	public static Team BLUE;
	public static Team RED;
	public static Team GREEN;
	public static Team YELLOW;
	public static int ID;
	public static String IDQ;
	
	public static Walls instance;
	
	@Getter static List<String> spectators = new ArrayList<String>();
	
	public void onLoad() {
		instance = this;
		Files.regen();
	}

    static Events Events;
    static PreEvents PreEvents;
    static GameEvents GameEvents;
    static KitEvents KitEvents;
    
	public void onEnable() {
		Hive.getInstance().setType(GameType.WALLS);
        
		GREEN = new Team(ChatColor.GREEN);
		RED = new Team(ChatColor.RED);
		YELLOW = new Team(ChatColor.YELLOW);
		BLUE = new Team(ChatColor.BLUE);

        GameEvents = new GameEvents();
		Events = new Events();
		PreEvents = new PreEvents();
		
		new Config();

        getCommand("team").setExecutor(new TeamChatCmd());
        getCommand("help").setExecutor(new HelpCmd());
        getCommand("time").setExecutor(new TimeCmd());
        getCommand("teleport").setExecutor(new TeleportCmd());
        getCommand("kit").setExecutor(new KitCmd());
        getCommand("blue").setExecutor(new TeamCmd());
        getCommand("red").setExecutor(new TeamCmd());
        getCommand("yellow").setExecutor(new TeamCmd());
        getCommand("green").setExecutor(new TeamCmd());
		PreTask.TIMER.schedule(new PreTask(), 0, 1000);

        Kit.load();
        
    	new BukkitRunnable() {
        	public void run() {
        		for (Player p : Bukkit.getOnlinePlayers()) {
        			if (Kit.getKit(p).getName().equalsIgnoreCase("scout"))
        				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, 1), false);
        		}
        	}
        }.runTaskTimerAsynchronously(this, 0, 120);
	}
	
	public void onDisable() {
	    for (Player p : Utils.getPlayers()) {
	        p.kickPlayer(ChatColor.YELLOW + "Server restarting for a new game!");
	    }
	}
    
    public static void prep() {
        HandlerList.unregisterAll(Events);
        HandlerList.unregisterAll(PreEvents);
        KitEvents = new KitEvents();
        Events = new Events();
        
        PreparedStatement ps = Hive.getInstance().getDatabase().prepare("INSERT INTO walls_games (start) values (now())");
        try {
            ps.executeUpdate();
        }
        catch (SQLException e1) {
            e1.printStackTrace();
        }
        try {
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                ID = rs.getInt(1);
                IDQ = "id=" + ID;
            }
        }
        catch (SQLException e1) {
            e1.printStackTrace();
        }

        State.startPrep();
        PreTask.TIMER.cancel();
        PrepTask.TIMER.schedule(new PrepTask(), 0, 1000);
        String base = "INSERT INTO walls_players (game_id,username,team,kit) VALUES ";
        for (Player p : Utils.getPlayers()) {
            p.teleport(Team.getTeam(p).getSpawn());
            base += String.format("(%d,'%s','%s','%s'),", ID, p.getName(), Team.getTeam(p).getColor().name().toLowerCase(), Kit.getKit(p).getName());
        }
        base = base.substring(0, base.length() - 1);
        Chat.log(base);
        Hive.getInstance().getDatabase().update(base);
        
        Chat.server("&aGame starting! Start preparing for battle!");
        
        new BukkitRunnable() {
			@Override
			public void run() {
		        Walls.updateSpectators();
			}
        }.runTaskTimerAsynchronously(Walls.instance, 0, 10);
    }
    
    public static void pvp() {
        Chat.server("&aThe walls have dropped! Let the battle begin!");
        Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Utils.dropWalls());
        State.startPVP();
        PrepTask.TIMER.cancel();
        PVPTask.TIMER.schedule(new PVPTask(), 0, 1000);
    }
    
    public static List<Player> winners = new ArrayList<Player>();
    public static void checkWinner() {
    	int losers = 0;
    	List<Team> alive = new ArrayList<Team>();
    	for (Team team : Team.list) {
    		if (team.getMembers().size()==0)
    			losers += 1;
    		else
    			alive.add(team);
    	}
    	
    	if (losers == 3) {
    		Team team = alive.get(0);
    		for (String s : team.getMembers()) {
    			Player p = Bukkit.getPlayerExact(s);
    			if (p == null || !p.isOnline())
    			    continue;
    			winners.add(p);
				Hive.getInstance().getDatabase().update("UPDATE walls_players SET winner=1 WHERE game_id=" + ID + " AND username='" + p.getName() + "'");
				Hive.getInstance().getDatabase().update("UPDATE players SET walls_wins=walls_wins+1,walls_games=walls_games+1 WHERE username='" + p.getName() + "'");
    			p.kickPlayer(ChatColor.GREEN + "You have won the game!");
    		}
    		
    		Hive.getInstance().getDatabase().update("UPDATE walls_games SET winner='" + alive.get(0).getColor().name().toLowerCase() + "',start=start,end=now() WHERE " + IDQ);
        	new BukkitRunnable() {
        	    @Override
        	    public void run() {            
                    Bukkit.shutdown();
        	    }
        	}.runTaskLater(Walls.instance, 200);
        	
            PrepTask.TIMER.cancel();
            PVPTask.TIMER.cancel();
    	}
    }

	public static void updateSpectators() {
		for (String list : getSpectators()) {
			Player t = Bukkit.getPlayerExact(list);
			if (t == null || !t.isOnline())
				continue;
			
			t.setAllowFlight(true);
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (getSpectators().contains(p.getName()))
					continue;
				p.hidePlayer(t);
			}
		}
	}
}
