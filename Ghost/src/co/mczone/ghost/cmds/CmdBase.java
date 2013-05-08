package co.mczone.ghost.cmds;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.mczone.api.players.Rank;
import co.mczone.ghost.Ghost;
import co.mczone.util.Chat;

public class CmdBase {
    public CmdBase() {
    	Ghost.getInstance().getCommand("matches").setExecutor(new MatchesCmd());
    	Ghost.getInstance().getCommand("ghost").setExecutor(new GhostCmd());
    }
    
    public static boolean isAdmin(CommandSender s) {
        if (Rank.getRank(s.getName()).getLevel() >= 7)
            return true;
        Chat.player(s, "&4[Parkour] &cYou don't have permission to use that command.");
        return false;
    }
    
    public static boolean isMod(CommandSender s) {
        if (Rank.getRank(s.getName()).getLevel() > 3)
            return true;
        Chat.player(s, "&4[Parkour] &cYou don't have permission to use that command.");
        return false;
    }
    
    public static boolean isPlayer(CommandSender s) {
        if (s instanceof Player) {
            return true;
        }
    	Chat.player(s, "&4[Parkour] &cOnly players can use that commmand.");
        return false;
    }
}
