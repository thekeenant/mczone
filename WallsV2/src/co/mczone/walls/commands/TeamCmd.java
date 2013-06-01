package co.mczone.walls.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.mczone.api.players.Gamer;
import co.mczone.api.players.RankType;
import co.mczone.walls.State;
import co.mczone.walls.Team;
import co.mczone.walls.Walls;
import co.mczone.walls.utils.Chat;

public class TeamCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Chat.player(sender, "&cThis command is only available for players!");
            return true;
        }
        
        final Player p = (Player) sender;
        Gamer g = Gamer.get(p);
        if (g.getRank().getType() == RankType.USER) {
            Chat.player(sender, "&cGo to www.mczone.co/donate to get this command!");
            return true;
        }
        
        if (!State.PRE) {
            Chat.player(sender, "&cThe game has already started!");
            return true;
        }
        
		Team team = Walls.BLUE;
		if (cmd.getName().equalsIgnoreCase("red"))
			team = Walls.RED;
		if (cmd.getName().equalsIgnoreCase("green"))
			team = Walls.GREEN;
		if (cmd.getName().equalsIgnoreCase("yellow"))
			team = Walls.YELLOW;

		team.join(p);
		p.setDisplayName(team.getColor() + p.getName());
        Chat.setPlayerListName(p, team.getColor() + p.getName());
        p.setDisplayName(team.getColor() + p.getName());
        p.teleport(team.getSpawn());
        Chat.player(sender, "&aYou have changed to team " + team.getColor() + team.getColor().name() + "&a!");
        return true;
    }

}
