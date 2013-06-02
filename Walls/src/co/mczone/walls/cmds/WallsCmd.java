package co.mczone.walls.cmds;

import org.bukkit.command.CommandExecutor;

import co.mczone.api.commands.BaseCommand;
import co.mczone.api.players.Gamer;
import co.mczone.api.players.Permissible;
import co.mczone.api.players.RankType;

public class WallsCmd extends BaseCommand implements CommandExecutor,Permissible {
    public WallsCmd() {
    	this.setTitle("&bMC Zone Walls Commands");
    	this.getSubCommands().put("edit", new WallsEditCmd());
    	this.getSubCommands().put("save", new WallsSaveCmd());
    	this.getSubCommands().put("set", new WallsSetCmd());
    	
    	this.setCarryPermissions(true);
    }

	@Override
	public boolean hasPermission(Gamer g) {
		if (g.getRank().getLevel() < RankType.ADMIN.getLevel())
			return false;
		return true;
	}
}
