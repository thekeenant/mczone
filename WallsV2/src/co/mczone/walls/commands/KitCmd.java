package co.mczone.walls.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.mczone.walls.Kit;
import co.mczone.walls.State;
import co.mczone.walls.utils.Chat;

public class KitCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
        	Kit.list(sender);
            return true;
        }
        Player p = (Player) sender;
        
        if (!State.PRE) {
			Chat.player(p, "&cThe game has already begun!");
			return true;
		}
		if (args.length==0) {
			Chat.simple(p, "&bChoose your kit with &o/kit [kit name]&b!");
			Kit.list(p);
			return true;
		}
		if (Kit.findByName(args[0])==null) {
			Chat.player(p, "&cCouldn't find the kit, '" + args[0] + "'!");
			return true;
		}
		if (!Kit.findByName(args[0]).hasPermission(p.getName())) {
			Chat.player(p, "&cThat kit can be purchased at www.mczone.co/walls");
			return true;
		}
		
		Kit.kits.put(p, Kit.findByName(args[0]));
		Kit.giveKit(p);
		Chat.player(p, "&aYou have chosen the kit, &e" + Chat.capitalize(args[0].toLowerCase()) + "&a!");
		return true;
    }

}
