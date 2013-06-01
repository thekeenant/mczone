package co.mczone.walls.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.mczone.walls.Team;
import co.mczone.walls.utils.Chat;

public class TeamChatCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Chat.player(sender, "&cThis command is only available for players!");
            return true;
        }
        Player p = (Player) sender;
        if (args.length == 0) {
        	Chat.player(sender, "&cUse command like this: /team [message]");
        	return true;
        }
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
        	sb.append(arg);
        	sb.append(" ");
        }
        Team team = Team.getTeam(p);
        for (String s : team.getMembers()) {
        	Player t = Bukkit.getPlayerExact(s);
        	Chat.player(t, "&2/team &7" + team.getColor() + p.getName() + "&f: " + sb.toString());
        	return true;
        }
        
        return true;
    }

}
