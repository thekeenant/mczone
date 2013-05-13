package co.mczone.pets.api;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Zombie;

import co.mczone.pets.Pets;

import lombok.Getter;
import lombok.Setter;

public class PetInstance {
	@Getter static List<PetInstance> list = new ArrayList<PetInstance>();
	@Getter int id;
	@Getter String owner;
	@Getter String name;
	@Getter @Setter ChatColor nameColor = ChatColor.WHITE;
	@Getter LivingEntity entity;
	@Getter boolean tamed;
	boolean shouldSpawn;
	@Getter @Setter boolean spawned;
	
	// Features
	@Getter EntityType type;
	@Getter DyeColor color = DyeColor.WHITE;
	@Getter @Setter Age age = Age.BABY;
	
	public PetInstance(int id, EntityType type, String owner, String name, boolean isSpawned) {
		this.id = id;
		this.type = type;
		this.owner = owner;
		this.name = name;
		this.shouldSpawn = isSpawned;
		list.add(this);
	}
	
	public static PetInstance get(Entity e) {
		if (e == null)
			return null;
		for (PetInstance pet : getList())
			if (pet.getEntity() != null && pet.getEntity().getUniqueId().equals(e.getUniqueId()))
				return pet;
		return null;
	}
	
	
	public void despawn() {
		spawned = false;
		if (entity == null)
			return;
		entity.remove();
		entity = null;
	}
	
	public void setToSpawn(boolean spawn) {
		shouldSpawn = spawn;
		int spawnedDB = spawn == true ? 1 : 0;
		Pets.getDB().update("UPDATE pets SET spawned=" + spawnedDB + " WHERE id=" + id);
	}
	
	public void spawn() {
		setSpawned(true);
		Player owner = Bukkit.getPlayerExact(this.owner);
		if (owner == null)
			return;
		
		Location spawnTo = owner.getLocation();
        entity = (LivingEntity) spawnTo.getWorld().spawnEntity(spawnTo, getType());
        update();
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayerExact(owner);
	}

	public void goToOwner() {
		entity.teleport(getPlayer().getLocation());
	}

	public static PetInstance load(int id, EntityType type, String owner, String name, boolean spawned) {
		for (PetInstance p : list)
			if (id == p.getId()) {
				if (p.isSpawned()) {
					return p;
				}
				return p;
			}
		
		return new PetInstance(id, type, owner, name, spawned);
	}

	public void setName(String name) {
		this.name = name;
		if (entity != null)
			entity.setCustomName(nameColor + name);
        
		Pets.getDB().update("UPDATE pets SET name='" + name + "' WHERE id=" + id);
	}

	public void setColor(DyeColor color) {
		if (type != EntityType.SHEEP)
			return;
		this.color = color;
		if (entity != null)
			((Sheep) entity).setColor(color);
        
		Pets.getDB().update("UPDATE pets SET color='" + color.toString() + "' WHERE id=" + id);
	}

	public boolean isShouldSpawn() {
		ResultSet r = Pets.getDB().query("SELECT spawned FROM pets WHERE id=" + id);
		try {
			while (r.next()) {
				return r.getInt("spawned") == 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void update() {
		if (entity != null) {
			//((CraftEntity) entity).getHandle().setEquipment(4, new net.minecraft.server.v1_5_R2.ItemStack(Item.DIAMOND_HELMET));
			//Chat.server(((CraftEntity) entity).getHandle().getEquipment().toString());
			Player owner = Bukkit.getPlayerExact(this.owner);
			if (owner == null)
				return;
	        entity.setCustomName(nameColor + name);
	        entity.setCustomNameVisible(true);
	        if (entity instanceof Tameable) {
	        	tamed = true;
	        	((Tameable) entity).setTamed(true);
	        	((Tameable) entity).setOwner(owner);
	        }
	        if (entity instanceof Ageable && age != null) {
	        	if (getAge() == Age.BABY)
	        		((Ageable) entity).setBaby();
	        	((Ageable) entity).setAgeLock(true);
	        }
	        if (entity instanceof Zombie && age != null) {
	        	if (getAge() == Age.BABY)
	        		((Zombie) entity).setBaby(true);
	        }
	        if (entity instanceof Sheep && color != null) {
	        	((Sheep) entity).setColor(getColor());
	        }
		}
	}
}
