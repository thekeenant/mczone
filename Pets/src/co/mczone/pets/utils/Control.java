package co.mczone.pets.utils;

import net.minecraft.server.v1_5_R3.Navigation;

import org.bukkit.craftbukkit.v1_5_R3.entity.CraftLivingEntity;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

public class Control {
    public static void walkToPlayer(Entity e, Player p) {
        // Tamed animals already handle their own following
        if (e instanceof Tameable) {
            if (((Tameable) e).isTamed()) {
                return;
            }
        }
        if (e.getPassenger() instanceof Player) {
            return;
        }

        // Moving the dragon is too buggy
        if (e instanceof EnderDragon) {
            return;
        }
        // Once this is set we can't unset it.
        //((Creature)e).setTarget(p);

        // If the pet is too far just teleport instead of attempt navigation
        if (e.getLocation().distance(p.getLocation()) > 20) {
            e.teleport(p);
        } else {
            Navigation n = ((CraftLivingEntity) e).getHandle().getNavigation();
            n.a(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), 0.30f);
        }
    }
}
