package co.mczone.walls.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.mczone.walls.utils.Chat;
import co.mczone.walls.utils.Utils;

public class ListCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String list = "";
        
        for (Player p : Utils.getPlayers()) {
            list += "&f" + p.getDisplayName() + "&f, ";
        }
        list = list.substring(0, list.length() - 2);
        
        Chat.simple(sender, "&6Currently &c" + Utils.getPlayers().length + "/" + Bukkit.getMaxPlayers() + " &6players online:");
        Chat.simple(sender, "&6Player List: " + list);
        return true;
    }

}
