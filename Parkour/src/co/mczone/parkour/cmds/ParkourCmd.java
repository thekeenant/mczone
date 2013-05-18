package co.mczone.parkour.cmds;

import org.bukkit.command.CommandExecutor;

import co.mczone.api.commands.*;
import co.mczone.api.players.Gamer;
import co.mczone.api.players.Permissible;
import co.mczone.api.players.RankType;

public class ParkourCmd extends BaseCommand implements CommandExecutor,Permissible {
	
    public ParkourCmd() {
    	getSubCommands().put("new", new ParkourNewCmd());
    	getSubCommands().put("spawn", new ParkourSpawnCmd());
    	getSubCommands().put("start", new ParkourStartCmd());
    	getSubCommands().put("end", new ParkourEndCmd());
    	getSubCommands().put("next", new ParkourNextCmd());
    }

	@Override
	public boolean hasPermission(Gamer g) {
		if (g.getRank().getLevel() < RankType.ADMIN.getLevel())
			return false;
		return true;
	}

}
