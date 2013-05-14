package co.mczone.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.mczone.api.players.Gamer;
import co.mczone.util.Chat;

public class ListCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Chat.player(sender, "&6Currently &c" + Bukkit.getOnlinePlayers().length + "/" + Bukkit.getMaxPlayers() + " &6players online:");
        String players = "";
        for (Player p : Bukkit.getOnlinePlayers()) {
        	Gamer g = Gamer.get(p);
        	players += g.getPrefix() + "&7" + p.getDisplayName() + "&f, ";
        }
        if (players.length() > 4)
        	players = players.substring(0, players.length() - 4);
        
        Chat.player(sender, "&6Player List: " + players);
    	
        return true;
    }
}
