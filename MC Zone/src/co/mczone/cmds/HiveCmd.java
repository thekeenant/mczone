package co.mczone.cmds;

import org.bukkit.command.CommandExecutor;

import co.mczone.api.commands.BaseCommand;

public class HiveCmd extends BaseCommand implements CommandExecutor {
	
	public HiveCmd() {
		this.setTitle("&4Hive Command");
		addCommand("server", new HiveServerCmd());
	}
}
