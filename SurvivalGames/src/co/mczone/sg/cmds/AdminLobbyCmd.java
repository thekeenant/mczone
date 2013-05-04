package co.mczone.sg.cmds;

import org.bukkit.command.CommandSender;
import co.mczone.util.Chat;
import org.bukkit.entity.Player;

import co.mczone.sg.SurvivalGames;
import co.mczone.sg.api.SubCommand;

public class AdminLobbyCmd implements SubCommand {
    public boolean execute(CommandSender sender, String[] args) {
    	if (!CmdBase.isPlayer(sender))
    		return true;
    	
    	Player p = (Player) sender;
    	
    	SurvivalGames.getInstance().getConfigAPI().set("lobby", p.getLocation());
    	SurvivalGames.getInstance().saveConfig();
    	
    	Chat.player(sender, "&2[SG] &aUpdated lobby spawn point");
        return true;
    }
}
