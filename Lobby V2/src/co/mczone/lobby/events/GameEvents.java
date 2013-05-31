package co.mczone.lobby.events;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import co.mczone.lobby.Lobby;
import co.mczone.lobby.api.Portal;
import co.mczone.lobby.api.Status;
import co.mczone.util.Chat;
import co.mczone.util.WorldUtil;

public class GameEvents implements Listener {
	public GameEvents() {
		Lobby.getInstance().getServer().getPluginManager().registerEvents(this, Lobby.getInstance());
	}
	
	private static final int FRAME = Material.OBSIDIAN.getId();
    private static final int SIGN = Material.WALL_SIGN.getId();
    
	@EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();
        Location to = event.getTo();
        Location from = event.getFrom();
        World world = to.getWorld();

        if (to.clone().add(0, -1, 0).getBlock().getType() != Material.REDSTONE_LAMP_ON)
        	return;
        
        if (from.clone().add(0, -1, 0).getBlock().getType() == Material.REDSTONE_LAMP_ON)
        	return;
        
        for (Block block : getPortalNear(world, to.getBlockX(), to.getBlockY(), to.getBlockZ())) {
            for (BlockFace bf : BlockFace.values()) {
                Block relative = block.getRelative(bf);
                if (relative.getTypeId() == SIGN) {
                	
                	// Specific server
                	Sign sign = (Sign) relative.getState();
                	Portal portal = null;
                	for (Portal po : Portal.getList()) {
                		if (WorldUtil.sameBlock(po.getSign().getBlock(), sign.getBlock())) {
                			portal = po;
                			break;
                		}
                	}
                	if (portal == null)
                		return;	
                	
                	if (portal.getCurrent().getStatus() == Status.CLOSED)
                		Chat.player(p, "&cThat server is currently unavailable!");
                	else                	
                		portal.getCurrent().connect(event.getPlayer());
                	
                	portal.updateSign();
                }
            }
        }
    }

    private Set<Block> getPortalNear(World world, int x, int y, int z) {
        byte b0 = 0;
        byte b1 = 0;
        if (world.getBlockTypeIdAt(x - 1, y, z) == FRAME || world.getBlockTypeIdAt(x + 1, y, z) == FRAME) {
            b0 = 1;
        }
        if (world.getBlockTypeIdAt(x, y, z - 1) == FRAME || world.getBlockTypeIdAt(x, y, z + 1) == FRAME) {
            b1 = 1;
        }

        Set<Block> blocks = new HashSet<Block>();

        if (world.getBlockTypeIdAt(x - b0, y, z - b1) == 0) {
            x -= b0;
            z -= b1;
        }

        for (byte i = -1; i <= 2; ++i) {
            for (byte j = -1; j <= 3; ++j) {
                boolean flag = i == -1 || i == 2 || j == -1 || j == 3;

                if (i != -1 && i != 2 || j != -1 && j != 3) {
                    if (flag) {
                        blocks.add(world.getBlockAt(x + b0 * i, y + j, z + b1 * i));
                    }
                }
            }
        }
        return blocks;
    }
}
