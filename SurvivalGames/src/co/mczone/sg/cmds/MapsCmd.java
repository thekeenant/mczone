package co.mczone.sg.cmds;

import org.bukkit.command.Command;
import co.mczone.util.Chat;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.mczone.sg.api.Map;

public class MapsCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	Chat.player(sender, "");
    	Chat.player(sender, "&7               &2[&aMAPS&2]               ");
    	for (Map m : Map.getList()) {
    		Chat.player(sender, "   &2[" + m.getId() + "] &f" + m.getTitle() + " &8[&7" + m.getVotes().size() + " votes&8]");
    	}
		return true;
    }
}