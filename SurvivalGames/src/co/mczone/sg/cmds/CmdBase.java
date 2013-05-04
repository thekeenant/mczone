package co.mczone.sg.cmds;

import org.bukkit.command.CommandSender;
import co.mczone.util.Chat;
import org.bukkit.entity.Player;

import co.mczone.api.players.Rank;
import co.mczone.sg.SurvivalGames;

public class CmdBase {
    public CmdBase() {
        SurvivalGames.getInstance().getCommand("admin").setExecutor(new AdminCmd());
        SurvivalGames.getInstance().getCommand("help").setExecutor(new HelpCmd());
        SurvivalGames.getInstance().getCommand("vote").setExecutor(new VoteCmd());
        SurvivalGames.getInstance().getCommand("maps").setExecutor(new MapsCmd());
        SurvivalGames.getInstance().getCommand("lobby").setExecutor(new SpawnCmd());
        SurvivalGames.getInstance().getCommand("world").setExecutor(new WorldCmd());
    }
    
    public static boolean isAdmin(CommandSender s) {
        if (Rank.getRank(s.getName()).getLevel() >= 7)
            return true;
        Chat.player(s, "&4[SG] &cYou don't have permission to use that command.");
        return false;
    }
    
    public static boolean isMod(CommandSender s) {
        if (Rank.getRank(s.getName()).getLevel() > 3)
            return true;
        Chat.player(s, "&4[SG] &cYou don't have permission to use that command.");
        return false;
    }
    
    public static boolean isPlayer(CommandSender s) {
        if (s instanceof Player) {
            return true;
        }
    	Chat.player(s, "&4[SG] &cOnly players can use that commmand.");
        return false;
    }
}
