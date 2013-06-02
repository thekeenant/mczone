package co.mczone.pets.cmds;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.mczone.api.commands.SubCommand;
import co.mczone.pets.api.PetInstance;
import co.mczone.util.Chat;

public class PetListCmd implements SubCommand {

	@Override
	public boolean execute(CommandSender sender, String[] args) {
    	Player p = (Player) sender;
    	String username = p.getName();
    	String prefix = "&aYour Pets: &f";
    	String pets = "";
    	for (PetInstance pet : PetInstance.getList()) {
    		if (pet.getOwner().equalsIgnoreCase(username)) {
	    		pets += "&e" + pet.getName() + "&f, ";
    		}
    	}
    	if (pets.length() > 2)
    		pets = pets.substring(0, pets.length() - 2);
    	
    	Chat.player(p, prefix + pets);
        return true;
	}
	
	@Override
	public String getAbout() {
		return "List the pets that you own";
	}
}
