package co.mczone.sg.cmds;

import org.bukkit.Bukkit;
import co.mczone.util.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.mczone.sg.Scheduler;
import co.mczone.sg.api.Map;
import co.mczone.sg.api.State;

public class VoteCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (args.length != 1) {
			Bukkit.dispatchCommand(sender, "maps");
    		return true;
    	}
    	if (Scheduler.getState() != State.PREP) {
    		Chat.player(sender, "&4[SG] &cThe voting period has ended.");
    		return true;
    	}
    	int id = 1;
    	try {
    		id = Integer.parseInt(args[0]);
    	}
    	catch(NumberFormatException e) {
    		Chat.player(sender, "&4[SG] &cThat is not a valid id. Type /maps to see your choices.");
    		return true;
    	}
    	
    	Map map = Map.getByID(id);
    	if (map == null) {
    		Chat.player(sender, "&4[SG] &cCould not find a map with the ID of " + id);
    		return true;
    	}
    	if (map.getVotes().contains(sender.getName()))
    		map.getVotes().remove(sender.getName());
    	
    	for (Map m : Map.getList()) {
    		if (m.getVotes().contains(sender.getName()))
    			m.getVotes().remove(sender.getName());
    	}
    	
    	map.getVotes().add(sender.getName());
    	Chat.player(sender, "&2[SG] &aThe map, &f" + map.getTitle() + " &anow has " + map.getVotes().size() + " votes");
		return true;
    }
}