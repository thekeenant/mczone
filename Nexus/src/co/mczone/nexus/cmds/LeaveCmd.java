package co.mczone.nexus.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.mczone.api.players.Gamer;
import co.mczone.nexus.Nexus;
import co.mczone.nexus.api.Team;
import co.mczone.util.Chat;

public class LeaveCmd implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Gamer g = Gamer.get(sender);
		
		Team team = Nexus.getRotary().getCurrentMap().getTeam(g);
		
		if (team == null) {
			Chat.player(g, "&cYou are already a spectator.");
			return true;
		}
		
		team.remove(g);
		
		g.setVariable("spectator", true);
		g.clearInventory();
		g.setFlying(false);
		g.setAllowFlight(false);
		g.setHealth(20);
		g.setFoodLevel(20);
		g.setSaturation(99);
		g.removePotionEffects();
		g.teleport(Nexus.getRotary().getCurrentMap().getSpawnLocation());
		Chat.player(g, "&7You are now a &fspectator &7watching the game in progress.");
		return true;		
	}

}
