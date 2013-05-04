package co.mczone.sg.cmds;

import java.util.Arrays;
import co.mczone.util.Chat;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.mczone.sg.api.SubCommand;

public class AdminCmd implements CommandExecutor {
    public static HashMap<String, SubCommand> subCommands = new HashMap<String, SubCommand>();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        AdminCmd.subCommands.put("addspawn", new AdminAddSpawnCmd());
        AdminCmd.subCommands.put("delspawn", new AdminDeleteSpawnCmd());
        AdminCmd.subCommands.put("lobby", new AdminLobbyCmd());
        AdminCmd.subCommands.put("spawns", new AdminSpawnsCmd());
        
        if (!CmdBase.isAdmin(sender))
            return true;
        
        if (args.length == 0) {
            Chat.player(sender, "&bAdmin Commands:");
            for (String s : subCommands.keySet()) {
                Chat.player(sender, "&7/admin " + s);
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
