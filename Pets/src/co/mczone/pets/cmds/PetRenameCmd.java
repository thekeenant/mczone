package co.mczone.pets.cmds;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.mczone.api.commands.SubCommand;
import co.mczone.pets.api.PetInstance;
import co.mczone.util.Chat;

public class PetRenameCmd implements SubCommand {

	@Override
	public boolean execute(CommandSender sender, String[] args) {    	
    	if (args.length != 2) {
    		Chat.player(sender, "&2[Pets] &cUsage: /pet rename [old name] [new name]");
    		return true;
    	}

    	Player p = (Player) sender;
    	String username = p.getName();
    	for (PetInstance pet : PetInstance.getList()) {
    		if (pet.getName().equalsIgnoreCase(args[0])) {
	    		if (pet.getOwner().equalsIgnoreCase(username)) {
	    			String name = args[1];
	    			if (name.length() > 16)
	    				name = name.substring(0, 16);
	    			Chat.player(p, "&2[Pets] &aYou have renamed &e" + pet.getName() + "&a to &e" + name);
	    			pet.setName(name);
	    			return true;
	    		}
    		}
    	}
    	
    	Chat.player(p, "&2[Pets] &cYou do not own a pet by the name of &e" + args[0] + "\n     &7&oBuy customized pets at www.mczone.co/shop");
        return true;
	}
	
	@Override
	public String getAbout() {
		return "Rename a pet that you own";
	}
}
