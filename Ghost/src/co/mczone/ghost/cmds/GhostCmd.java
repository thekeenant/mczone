package co.mczone.ghost.cmds;

import lombok.Getter;

import org.bukkit.command.CommandExecutor;

import co.mczone.cmds.BaseCommand;

public class GhostCmd extends BaseCommand implements CommandExecutor {
	@Getter String title = "&7 ----- &bMC Zone Ghost Commands &7-----";
	
    public GhostCmd() {
    	this.getSubCommands().put("edit", new GhostEditCmd());
    	this.getSubCommands().put("save", new GhostSaveCmd());
    	this.getSubCommands().put("set", new GhostSetCmd());
    }   
}
