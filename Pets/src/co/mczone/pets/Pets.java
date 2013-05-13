package co.mczone.pets;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import co.mczone.api.database.MySQL;
import co.mczone.api.server.Hive;
import co.mczone.pets.api.*;
import co.mczone.pets.cmds.CmdBase;
import co.mczone.pets.events.*;
import co.mczone.pets.utils.Control;
import co.mczone.util.Chat;

public class Pets extends JavaPlugin {
	@Getter static Pets instance;
	@Getter static MySQL DB;
	
	@Getter static HashMap<String, PetInstance> pets = new HashMap<String, PetInstance>();
	
	public void onEnable() {
		instance = this;
		DB = Hive.getInstance().getDatabase();
		
		new CmdBase();
		
		new BukkitRunnable() {

			@Override
			public void run() {
				loadPets();
			}
			
		}.runTaskTimerAsynchronously(this, 0, 20 * 45);

		Bukkit.getPluginManager().registerEvents(new PlayerEvents(), this);
		Bukkit.getPluginManager().registerEvents(new PetEvents(), this);
		startFollowTask();
	}
	
	public void onDisable() {
		for (PetInstance pet : PetInstance.getList()) {
			if (pet.getEntity() != null)
				pet.getEntity().remove();
		}
	}
	
	static List<String> owners = new ArrayList<String>();
	public static void loadPets() {
		List<PetInstance> before = PetInstance.getList();
		List<PetInstance> after = new ArrayList<PetInstance>();
		int beforeSize = PetInstance.getList().size();
		ResultSet r = DB.query("SELECT * FROM pets");
		try {
			while (r.next()) {
				int id = r.getInt("id");
				String owner = r.getString("owner");
				String name = r.getString("name");
				
				boolean spawned = r.getInt("spawned") == 1 ? true : false;

				EntityType type = null;
				try {
					type = EntityType.valueOf(r.getString("type").toUpperCase());
				}
				catch(IllegalArgumentException e) {
					continue;
				}
				
				Age age = null;
				if (r.getString("age") != null)
					age = Age.valueOf(r.getString("age").toUpperCase());
				
				DyeColor dye = null;
				if (r.getString("color") != null)
					dye = DyeColor.valueOf(r.getString("color").toUpperCase());
				
				ChatColor nameColor = ChatColor.valueOf(r.getString("name_color").toUpperCase());
				
				PetInstance pet = PetInstance.load(id, type, owner, name, spawned);
				if (pet != null) {
					pet.setAge(age);
					pet.setColor(dye);
					if (nameColor != null)
						pet.setNameColor(nameColor);
					pet.update();
					
					owners.add(owner.toLowerCase());
					after.add(pet);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		List<PetInstance> remove = new ArrayList<PetInstance>();
		for (PetInstance b : before) {
			boolean found = false;
			for (PetInstance a : after) {
				if (a.getId() == b.getId())
					found = true;
			}
			if (!found) {
				remove.add(b);
			}
		}
		
		for (PetInstance pet : remove) {
			pet.despawn();
			PetInstance.getList().remove(pet);
		}
		
		
		int afterSize = PetInstance.getList().size();
		Chat.log("Loaded " + beforeSize + " to " + afterSize + " pet donations");
	}
	
	public boolean isPetOwner(Player p) {
		return owners.contains(p.getName().toLowerCase());
	}
	
    public void startFollowTask() {
    	new BukkitRunnable() {
			@Override
			public void run() {
                for (final Player p : Bukkit.getOnlinePlayers()) {
                    if (isPetOwner(p)) {
                    	for (PetInstance pet : PetInstance.getList()) {
                    		if (pet.getOwner().equalsIgnoreCase(p.getName()) == false)
                    			continue;
                    		
                    		if (pet.isSpawned()) {
                    			final Entity e = pet.getEntity();
                    			if (e == null)
                    				continue;
                    			
                    			if (e.isDead()) {
                    				pet.spawn();
                    				return;
                    			}
                    			double distance = p.getLocation().distance(e.getLocation());
                    			if (distance > 3.0) {
		                    		Control.walkToPlayer(e, p);
                    			}
                    		}
                    	}
                    }
                }
			}
    	}.runTaskTimerAsynchronously(this, 0, 20);
    }
}
