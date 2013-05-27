package co.mczone.pets.cmds;

import org.bukkit.command.CommandExecutor;

import co.mczone.api.commands.BaseCommand;
import co.mczone.api.players.Gamer;

public class PetCmd extends BaseCommand implements CommandExecutor {
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
