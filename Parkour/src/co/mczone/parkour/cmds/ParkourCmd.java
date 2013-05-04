package co.mczone.parkour.cmds;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.mczone.parkour.*;
import co.mczone.util.Chat;

public class ParkourCmd implements CommandExecutor {
    public static HashMap<String, SubCommand> subCommands = new HashMap<String, SubCommand>();
    
    public ParkourCmd() {
    	subCommands.put("new", new ParkourNewCmd());
    	subCommands.put("spawn", new ParkourSpawnCmd());
    	subCommands.put("start", new ParkourStartCmd());
    	subCommands.put("end", new ParkourEndCmd());
    	subCommands.put("next", new ParkourNextCmd());
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!CmdBase.isAdmin(sender))
            return true;
        
        if (args.length == 0) {
            Chat.player(sender, "&7[Parkour] &bAdmin Commands:");
            for (String s : subCommands.keySet()) {
                Chat.player(sender, "&7/parkour " + s);
            }
            return true;
        }
        else {
            for (Entry<String, SubCommand> e : subCommands.entrySet()) {
                if (e.getKey().equalsIgnoreCase(args[0]))
                        return e.getValue().execute(sender, Arrays.copyOfRange(args, 1, args.length));
            }
            
            Chat.player(sender, "&4[SG] &cInvalid sub command.");
            return true;
        }
    }

}
