package co.mczone.sg.cmds;

import org.bukkit.command.CommandSender;
import co.mczone.util.Chat;
import org.bukkit.entity.Player;

import co.mczone.api.commands.SubCommand;
import co.mczone.api.players.Gamer;
import co.mczone.api.players.Permissible;
import co.mczone.api.players.RankType;
import co.mczone.sg.SurvivalGames;

public class AdminLobbyCmd implements SubCommand,Permissible {
    public boolean execute(CommandSender sender, String[] args) {
    	Player p = (Player) sender;
    	
    	SurvivalGames.getInstance().getConfigAPI().set("lobby", p.getLocation());
    	SurvivalGames.getInstance().saveConfig();
    	
    	Chat.player(sender, "&2[SG] &aUpdated lobby spawn point");
        return true;
    }

	@Override
	public boolean hasPermission(Gamer g) {
		if (g.getRank().getLevel() < RankType.ADMIN.getLevel())
			return false;
		return true;
	}
	
	@Override
	public String getAbout() {
		return "Set lobby spawn point";
	}
}
