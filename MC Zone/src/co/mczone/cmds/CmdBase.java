package co.mczone.cmds;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.mczone.MCZone;
import co.mczone.util.Chat;

public class CmdBase {
    public CmdBase() {
        MCZone.getInstance().getCommand("ban").setExecutor(new BanCmd());
        MCZone.getInstance().getCommand("unban").setExecutor(new UnbanCmd());
        MCZone.getInstance().getCommand("tempban").setExecutor(new TempbanCmd());
        MCZone.getInstance().getCommand("kick").setExecutor(new KickCmd());
    }
    
    public static boolean returnConsole(CommandSender s) {
        if (s instanceof Player == false)
            return true;
        
    	Chat.player(s, "&cOnly players can use that commmand.");
        return false;
    }
}
