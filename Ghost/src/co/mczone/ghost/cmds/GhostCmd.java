package co.mczone.ghost.cmds;

import org.bukkit.command.CommandExecutor;

import co.mczone.api.commands.BaseCommand;
import co.mczone.api.players.Gamer;
import co.mczone.api.players.Permissible;
import co.mczone.api.players.RankType;

public class GhostCmd extends BaseCommand implements CommandExecutor,Permissible {
    public GhostCmd() {
    	this.setTitle("&7&m&l -------- &bMC Zone Ghost Commands &7&m&l--------");
    	this.getSubCommands().put("edit", new GhostEditCmd());
    	this.getSubCommands().put("save", new GhostSaveCmd());
    	this.getSubCommands().put("set", new GhostSetCmd());
    	
    	this.setCarryPermissions(true);
    }

	@Override
	public boolean hasPermission(Gamer g) {
		if (g.getRank().getLevel() < RankType.ADMIN.getLevel())
			return false;
		return true;
	}
}
