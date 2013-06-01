package co.mczone.walls.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.mczone.api.players.Gamer;
import co.mczone.api.players.RankType;
import co.mczone.walls.State;
import co.mczone.walls.Team;
import co.mczone.walls.utils.Chat;

public class TeleportCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Chat.player(sender, "&cThis command is only available for players!");
            return true;
        }
        
        Player p = (Player) sender;

        if (Gamer.get(p).getRank().getType() == RankType.USER) {
            Chat.player(sender, "&cGo to www.mczone.co/upgrade to get this command!");
            return true;
        }
        
        if (args.length != 1) {
            Chat.player(sender, "&cMust specify a player: /teleport [player]");
            return true;
        }
        if (!State.PREP) {
            Chat.player(sender, "&cThis command can only be used during prep time!");
            return true;
        }
        Player t = Bukkit.getPlayer(args[0]);
        if (t == null) {
            Chat.player(sender, "&cPlayer, " + args[0] + " not found!");
            return true;
        }
        if (Team.getTeam(t) != Team.getTeam(p)) {
            Chat.player(sender, "&cThat player is not on your team!");
            return true;
        }
        
        Chat.player(sender, "&aTeleporting to player, " + t.getName() + "!");
        p.teleport(t.getLocation());
        return true;
    }

}
