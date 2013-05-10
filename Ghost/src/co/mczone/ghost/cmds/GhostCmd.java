package co.mczone.ghost.cmds;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.mczone.api.players.Rank;
import co.mczone.api.players.RankType;
import co.mczone.cmds.SubCommand;
import co.mczone.util.Chat;

public class GhostCmd implements CommandExecutor {

    public static HashMap<String, SubCommand> subCommands = new HashMap<String, SubCommand>();
    
    public GhostCmd() {
    	subCommands.put("edit", new GhostEditCmd());
    	subCommands.put("save", new GhostSaveCmd());
    	subCommands.put("set", new GhostSetCmd());
    }
    
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (Rank.getRank(sender.getName()).getType().getLevel() < RankType.ADMIN.getLevel()) {
    		Chat.player(sender, "&cYou don't have permission to use that command.");
    		return true;
    	}

    	if (args.length == 0) {
            Chat.player(sender, "&7 ----- &bMC Zone Ghost Commands &7-----");
            for (Entry<String, SubCommand> e : subCommands.entrySet()) {
            	String s = e.getKey();
            	SubCommand c = e.getValue();
                Chat.player(sender, "&6/ghost " + s + "&f: " + c.getAbout());
            }
            return true;
        }
        else {
            for (Entry<String, SubCommand> e : subCommands.entrySet()) {
                if (e.getKey().equalsIgnoreCase(args[0]))
                        return e.getValue().execute(sender, Arrays.copyOfRange(args, 1, args.length));
            }
            
            Chat.player(sender, "&cInvalid sub command.");
            return true;
        }
	}

}
