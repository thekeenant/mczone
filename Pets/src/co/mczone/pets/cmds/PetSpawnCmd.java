package co.mczone.pets.cmds;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.mczone.api.commands.SubCommand;
import co.mczone.pets.api.PetInstance;
import co.mczone.util.Chat;

public class PetSpawnCmd implements SubCommand {

	@Override
	public boolean execute(CommandSender sender, String[] args) {
    	Player p = (Player) sender;
    	String username = p.getName();
    	if (args.length != 1) {
    		Chat.player(sender, "&2[Pets] &cUsage: /pet spawn [pet name]");
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

    	for (PetInstance pet : PetInstance.getList()) {
    		if (pet.getOwner().equalsIgnoreCase(username)) {
        		if (!pet.getName().equalsIgnoreCase(args[0]) && pet.isSpawned()) {
        			Chat.player(p, "&2[Pets] &cYou can only have one pet spawned at a time.");
        			return true;
        		}
    		}
    	}
    	
    	for (PetInstance pet : PetInstance.getList()) {
    		if (pet.getName().equalsIgnoreCase(args[0])) {
	    		if (pet.getOwner().equalsIgnoreCase(username)) {
	    			if (pet.isSpawned()) {
	    				Chat.player(p, "&2[Pets] &e" + pet.getName() + " &cis already spawned!");
	    				return true;
	    			}
	    			else {
	    				Chat.player(p, "&2[Pets] &aYou have spawned your pet, &e" + pet.getName());
	    				pet.spawn();
	    				pet.setToSpawn(true);
	    				return true;
	    			}
	    		}
    		}
    	}
    	
    	Chat.player(p, "&2[Pets] &cYou do not own a pet by the name of " + args[0] + "\n    &7&oBuy customized pets at www.mczone.co/shop");
        return true;
	}

	@Override
	public String getAbout() {
		return "Spawn a pet that you own";
	}
}
