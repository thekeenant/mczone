package co.mczone.cmds;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.mczone.api.players.Gamer;
import co.mczone.api.server.Hive;
import co.mczone.api.shop.Shop;
import co.mczone.api.shop.ShopItem;
import co.mczone.util.Chat;

public class ShopCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Gamer g = Gamer.get(sender);
        Shop shop = Hive.getInstance().getShop();
        
        if (args.length > 0) {
        	ShopItem item = shop.get(args[0]);
        	
        	if (!shop.isAllow()) {
        		Chat.player(sender, "&cYou are not allowed to purchase items at this time");
        		return true;
        	}
        	if (item == null) {
        		Chat.player(sender, "&cCouldn't find the item, " + args[0]);
        		return true;
        	}
        	
    		int count = 1;
    		if (args.length >= 2) {
    			int s = 1;
    			try {
        			s = Integer.parseInt(args[1]);
    			}
    			catch(NumberFormatException nfe) {
    				Chat.player(g, "&cPlease supply a number after the item");
    				return true;
    			}
    			count = s;
    		}
    		if (item.buy(g, count)) {
    			Chat.player(g, "&aYou have purchased " + item.getDescription());
    		}
    		else {
    			Chat.player(g, "&aYou don't have enough credits for that!");
    			return true;
    		}
        	
        	return true;
        }
        else {
        	
        	if (shop.getList().size() == 0) {
        		Chat.player(sender, "&cThere are no items in the shop available.");
        		return true;
        	}
        	
        	List<ShopItem> list = shop.getList();
        	Collections.sort(list, new Comparator<ShopItem>() {
				public int compare(ShopItem c1, ShopItem c2) {
					if (c1.getPrice() > c2.getPrice()) return 1;
					if (c1.getPrice() < c2.getPrice()) return -1;
					return 0;
				}
			});
        	
        	int credits = g.getCredits();
	        Chat.player(sender, "&7        -----   &b&oCredit Shop   &7-----");
	        String msg = "";
	        for (ShopItem i : list) {
	        	ChatColor color = credits >= i.getPrice() ? ChatColor.GREEN : ChatColor.RED;
	        	msg += "  &7/shop " + i.getTitle() + " : &f" + i.getDescription() + " &7for " + color + i.getPrice() + " credits\n";
	        }
	        Chat.player(sender, msg);
	        Chat.player(sender, "&bYou currently have &e" + credits + " credits");
	        return true;
        }
    }

}
