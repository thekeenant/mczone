package co.mczone.pets.cmds;

import java.util.Arrays;

import org.bukkit.DyeColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.google.common.base.Joiner;

import co.mczone.cmds.SubCommand;
import co.mczone.pets.api.PetInstance;
import co.mczone.util.Chat;

public class PetColorCmd implements SubCommand {

	@Override
	public boolean execute(CommandSender sender, String[] args) {    	
    	if (args.length < 2) {
    		Chat.player(sender, "&2[Pets] &cUsage: /pet color [pet name] [color]");
    		return true;
    	}

    	Player p = (Player) sender;
    	String username = p.getName();
    	for (PetInstance pet : PetInstance.getList()) {
    		if (pet.getName().equalsIgnoreCase(args[0])) {
	    		if (pet.getOwner().equalsIgnoreCase(username)) {
	    			if (pet.getType() != EntityType.SHEEP) {
	    				Chat.player(p, "&2[Pets] &cYou can only change the color of sheep.");
	    				return true;
	    			}
	    			String[] nameArray = Arrays.copyOfRange(args, 1, args.length);
	    			String c = Joiner.on("_").join(nameArray);
	    			DyeColor color = null;
	    			try {
	    				color = DyeColor.valueOf(c.toUpperCase());
	    			}
	    			catch (IllegalArgumentException e) {
	    				Chat.player(p, "&2[Pets] &cUnknown color: " + c + "!");
	    				Chat.player(p, "&cColors: &7&o" + Joiner.on(", ").join(DyeColor.values()).toLowerCase().replace("_", " "));
	    				return true;
	    			}
	    			
	    			Chat.player(p, "&2[Pets] &aYou have changed the color of &e" + pet.getName() + "&a to " + c.replace("_", " "));
	    			pet.setColor(color);
	    			return true;
	    		}
    		}
    	}
    	
    	Chat.player(p, "&2[Pets] &cYou do not own a pet by the name of &e" + args[0] + "\n      &7&oBuy customized pets at www.mczone.co/shop");
        return true;
	}
	
	@Override
	public String getAbout() {
		return "Change the color of a pet sheep that you own";
	}
}
