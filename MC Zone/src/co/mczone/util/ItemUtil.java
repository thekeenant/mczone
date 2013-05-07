package co.mczone.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class ItemUtil {
	public static ItemStack deserializeItem(String raw) {
		String[] info;
        String first;
        Integer amount;
        Integer id;
        String itemName;
        Short durability = 0;
        ItemStack i;
        String[] args = raw.split(",");
        if (args.length>1) {
            first = args[0];
            amount = Integer.parseInt(args[1]);
        }
        else {
            first = raw;
            amount = 1;
        }
        if (first.contains(":")) {
            info = first.split(":");
            itemName = info[0];
            if (Material.getMaterial(itemName.toUpperCase())==null) {
                Chat.log(Level.SEVERE, "Bad Item: " + first);
                return null;
            }
            id = Material.getMaterial(itemName.toUpperCase()).getId();
            durability = Short.parseShort(info[1]);
            i = new ItemStack(id, amount, durability);
        }
        else {
            if (Material.getMaterial(first.toUpperCase())==null) {
                Chat.log(Level.SEVERE, "Bad Item: " + first);
                return null;
            }
            id = Material.getMaterial(first.toUpperCase()).getId();
            i = new ItemStack(Material.getMaterial(first.toUpperCase()), amount);
        }
        if (args.length >= 3) {
        	List<String> enchantments = new ArrayList<String>();
        	for (int ec = 0; ec <= args.length -1; ec++) {
        		if (ec >= 2)
        			enchantments.add(args[ec]);
        	}
        	for (String ench : enchantments) {
                if (Enchantment.getByName(ench.split(":")[0].toUpperCase())==null) {
                    Chat.log(Level.SEVERE, "Bad Enchantment: " + first);
                    continue;
                }
                Enchantment e = Enchantment.getByName(ench.split(":")[0].toUpperCase());
                int level = Integer.parseInt(ench.split(":")[1]);
                i.addUnsafeEnchantment(e, level);
        	}
        }
        return i;
	}
	
	public static String serialize(ItemStack item) {
		String name = item.getType().name().toLowerCase();
		String data = "";
		if (item.getDurability() != (short) 0)
			data = ":" + item.getDurability();
		
		String amount = "";
		if (item.getAmount() > 1)
			amount = "," + item.getAmount();
		
		String ench = "";
		if (item.getEnchantments().size() > 0) {
			for (Entry<Enchantment, Integer> e : item.getEnchantments().entrySet()) {
				ench += e.getKey().getName().toLowerCase() + ":" + e.getValue() + ",";
			}
			ench = ench.substring(ench.length() - 1);
		}
		
		return name + data + amount + ench;		
	}
}
