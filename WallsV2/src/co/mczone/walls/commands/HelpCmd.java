package co.mczone.walls.commands;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.mczone.walls.utils.Chat;

public class HelpCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("help")) {
            Chat.simple(sender, "&7---------------- &4Walls Commands&7 ----------------");
            HashMap<String,String> cmds = new HashMap<String,String>();
            cmds.put("/help", "You're looking at it right now!");
            cmds.put("/list", "View a list of the online players");
            cmds.put("/teamchat", "Switch into team chat mode");
            cmds.put("/tp", "Teleport to a member on your team");
            for (Entry<String, String> e : cmds.entrySet()) {
                Chat.simple(sender, "&6" + e.getKey() + "&f: " + e.getValue());
            }
            Chat.simple(sender, "&7-------------------------------------------------");
        }
        return true;
    }
}
