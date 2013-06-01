package co.mczone.walls;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Team {
	@Getter ChatColor color;
	@Getter List<String> members = new ArrayList<String>();
	public static List<Team> list = new ArrayList<Team>();
	
	public Team(ChatColor color) {
		this.color = color;
		list.add(this);
	}
	
	public Location getSpawn() {
		if (Walls.RED==this)
			return new Location(Config.getWorld(),-717,73,-132);
		else if (Walls.YELLOW==this)
			return new Location(Config.getWorld(),-868,73,-132);
		else if (Walls.GREEN==this)
			return new Location(Config.getWorld(),-868,73,-212);
		else if (Walls.BLUE==this)
			return new Location(Config.getWorld(),-718,73,-212);
		else
			return null;
	}
	
	public static Team getTeam(Player p) {
		for (Team team : list) {
			if (team.getMembers().contains(p.getName()))
				return team;
		}
		return null;
	}

	public void join(Player p) {
        for (Team team : list) {
            if (team.getMembers().contains(p.getName()))
                team.getMembers().remove(p.getName());
        }
        
        p.setDisplayName(color + p.getName());
        
		members.add(p.getName());
	}
	
	public void leave(Player p) {
		members.remove(p.getName());
	}
    
    public Block getMin() {
        if (Walls.BLUE == this)
            return new Location(Config.getWorld(), -783, 0, -241).getBlock();
        if (Walls.RED == this)
            return new Location(Config.getWorld(), -783, 0, -162).getBlock();
        if (Walls.YELLOW == this)
            return new Location(Config.getWorld(), -873, 0, -162).getBlock();
        if (Walls.GREEN == this)
            return new Location(Config.getWorld(), -873, 0, -241).getBlock();
        else
            return null;
    }
    
    public Block getMax() {
        if (Walls.BLUE == this)
            return new Location(Config.getWorld(), -715, 0, -184).getBlock();
        if (Walls.RED == this)
            return new Location(Config.getWorld(), -715, 0, -104).getBlock();
        if (Walls.YELLOW == this)
            return new Location(Config.getWorld(), -805, 0, -104).getBlock();
        if (Walls.GREEN == this)
            return new Location(Config.getWorld(), -805, 0, -184).getBlock();
        return null;
    }
}
