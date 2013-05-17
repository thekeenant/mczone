package co.mczone.lobby.cmds;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import co.mczone.api.players.Rank;
import co.mczone.api.players.RankType;
import co.mczone.util.Chat;

public class HatCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (Rank.getRank(sender.getName()).getType().getLevel() < RankType.ELITE.getLevel()) {
    		Chat.player(sender, "&cYou don't have permission to do this.");
    		return true;
    	}
        if (args.length != 1) {
        	Chat.player(sender, "&cUsage: /hat [item id or name]");
        	return true;
        }
        
        boolean isInt = true;
        Material m = null;
        try {
        	m = Material.getMaterial(Integer.parseInt(args[0]));
        }
        catch(Exception e) {
        	isInt = false;
        	m = Material.getMaterial(args[0].toUpperCase());
        }
        
        if (m == null) {
        	if (!isInt) {
        		for (Material c : Material.values())
        			if (c.name().toLowerCase().startsWith(args[0].toLowerCase()))
        				m = c;
        	}
        }
        
        if (m == null) {
	        	Chat.player(sender, "&cUnknown item: " + args[0]);
	        	return true;
        }
        
        Player p = Bukkit.getPlayerExact(sender.getName());
        p.getInventory().setHelmet(new ItemStack(m));
        Chat.player(sender, "&6Set helmet to &c" + m.name().replace("_", " ").toLowerCase());
        
        
    	return true;
    }
}
