package co.mczone.lobby.api;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import net.minecraft.server.v1_5_R3.NBTTagCompound;
import net.minecraft.server.v1_5_R3.NBTTagList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_5_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import co.mczone.util.Chat;

public class GameIcon {
	@Getter
	static List<GameIcon> list = new ArrayList<GameIcon>();
	@Getter Material material;
	@Getter String title;
	@Getter Location to;
	@Getter boolean glow = false;
	
	public GameIcon(Material material, String title, Location to, boolean glow) {
		this.material = material;
		this.title = title;
		this.to = to;
		this.glow = glow;
		list.add(this);
	}
	
	public GameIcon(Material material, String title, Location to) {
		this.material = material;
		this.title = title;
		this.to = to;
		list.add(this);
	}
	
	public ItemStack getItemStack() {
		ItemStack stack = new ItemStack(material);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(Chat.colors(title));
		stack.setItemMeta(meta);
		if (glow)
			return addGlow(stack);
		else
			return stack;
	}

	public static GameIcon get(ItemStack item) {
		for (GameIcon game : list) {
			if (Chat.stripColor(game.getTitle()).equalsIgnoreCase(Chat.stripColor(item.getItemMeta().getDisplayName())))
				return game;
		}
		return null;
	}
	
	public static ItemStack addGlow(ItemStack item){   
	    net.minecraft.server.v1_5_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
	    NBTTagCompound tag = null;
	    if (!nmsStack.hasTag()) {
	        tag = new NBTTagCompound();
	        nmsStack.setTag(tag);
	    }
	    if (tag == null) tag = nmsStack.getTag();
	    NBTTagList ench = new NBTTagList();
	    tag.set("ench", ench);
	    nmsStack.setTag(tag);
	    return CraftItemStack.asCraftMirror(nmsStack);
	}
}
