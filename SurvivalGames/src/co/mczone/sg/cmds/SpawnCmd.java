package co.mczone.sg.cmds;

import org.bukkit.command.Command;
import co.mczone.util.Chat;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.mczone.api.players.Gamer;
import co.mczone.api.players.Permissible;
import co.mczone.api.players.RankType;
import co.mczone.sg.Scheduler;
import co.mczone.sg.SurvivalGames;
import co.mczone.sg.api.State;

public class SpawnCmd implements CommandExecutor, Permissible {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	Player p = (Player) sender;
    	
    	if (Scheduler.getState() != State.PREP) {
    		Chat.player(sender, "&4[SG] &cYou cannot use /spawn at this time");
    		return true;
    	}
    	
    	Chat.player(sender, "&7&oTeleporting to the lobby...");
    	p.teleport(SurvivalGames.getInstance().getConfigAPI().getLocation("lobby"));
    	
		return true;
    }

	@Override
	public boolean hasPermission(Gamer g) {
		if (g.getRank().getLevel() == RankType.CONSOLE.getLevel())
			return false;
		return true;
	}
}