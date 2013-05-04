package co.mczone.lobby;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg0 instanceof Player) {
			((Player) arg0).teleport(Bukkit.getWorld("void").getSpawnLocation());
		}
		return false;
	}

}
