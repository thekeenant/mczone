package co.mczone.pets.cmds;

import co.mczone.api.commands.BaseCommand;
import co.mczone.api.players.Gamer;
import co.mczone.api.players.Permissible;

public class PetCmd extends BaseCommand implements Permissible {
    public PetCmd() {
    	this.setTitle("&7&m&l -------- &bMC Zone Pet Commands &7&m&l--------");
    	this.getSubCommands().put("spawn", new PetSpawnCmd());
        this.getSubCommands().put("rename", new PetRenameCmd());
        this.getSubCommands().put("list", new PetListCmd());
        this.getSubCommands().put("color", new PetColorCmd());
    }

	@Override
	public boolean hasPermission(Gamer g) {
		return true;
	}
}
