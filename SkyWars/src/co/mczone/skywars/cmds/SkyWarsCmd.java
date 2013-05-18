package co.mczone.skywars.cmds;

import org.bukkit.command.CommandExecutor;

import co.mczone.api.commands.BaseCommand;
import co.mczone.api.players.Gamer;
import co.mczone.api.players.Permissible;
import co.mczone.api.players.RankType;

public class SkyWarsCmd extends BaseCommand implements CommandExecutor,Permissible {
    public SkyWarsCmd() {
    	this.setTitle("&7&m&l -------- &bMC Zone Sky Wars Commands &7&m&l--------");
    	this.getSubCommands().put("edit", new SkyWarsEditCmd());
    	this.getSubCommands().put("save", new SkyWarsSaveCmd());
    	this.getSubCommands().put("set", new SkyWarsSetCmd());
    	
    	this.setCarryPermissions(true);
    }

	@Override
	public boolean hasPermission(Gamer g) {
		if (g.getRank().getLevel() < RankType.OFFICER.getLevel())
			return false;
		return true;
	}
}
