package co.mczone.sg.cmds;

import org.bukkit.command.CommandExecutor;

import co.mczone.api.commands.BaseCommand;

public class AdminCmd extends BaseCommand implements CommandExecutor {
	
	public AdminCmd() {
    	this.setTitle("&7&m&l -------- &bMC Zone SG Commands &7&m&l--------");
		
        getSubCommands().put("addspawn", new AdminAddSpawnCmd());
        getSubCommands().put("delspawn", new AdminDeleteSpawnCmd());
        getSubCommands().put("lobby", new AdminLobbyCmd());
        getSubCommands().put("spawns", new AdminSpawnsCmd());
	}
}
