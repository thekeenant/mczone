package co.mczone.lobby.events;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import co.mczone.util.Chat;
import co.mczone.api.players.Gamer;
import co.mczone.api.players.Rank;
import co.mczone.api.players.RankType;
import co.mczone.lobby.Lobby;
import co.mczone.lobby.api.Game;
import co.mczone.lobby.api.MiniGame;
import co.mczone.lobby.api.ServerSign;
import co.mczone.lobby.util.Util;

public class GameEvents implements Listener {
	public GameEvents() {
		Lobby.getInstance().getServer().getPluginManager().registerEvents(this, Lobby.getInstance());
	}

	private static final int FRAME = Material.LEAVES.getId();
	private static final int FLOOR = Material.WOOL.getId();
    private static final int SIGN = Material.WALL_SIGN.getId();
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		
		if (event.getClickedBlock().getType() != Material.WALL_SIGN)
			return;
		
		Sign sign = (Sign) event.getClickedBlock().getState();
    	Game game = null;
    	for (Game g : Game.getList()) {
    		String line_1 = Chat.stripColor(sign.getLine(0));
    		String line_2 = Chat.stripColor(sign.getLine(1));
    		if (g.getLine_1().equalsIgnoreCase(line_1))
        		if (g.getLine_2().equalsIgnoreCase(line_2))
    				game = g;
    	}
    	
    	if (game != null) {
    		boolean exists = false;
    		for (Block b : game.getSigns()) {
    			if (b.getX() == sign.getX() && b.getY() == sign.getY() && b.getZ() == sign.getZ()) {
    				exists = true;
    				break;
    			}
    		}
    		
    		if (!exists) {
    			game.getSigns().add(sign.getBlock());
    		}
    		
    		Rank r = Gamer.get(event.getPlayer()).getRank();
    		if (game.getPlayers() >= game.getMaxPlayers()) {
    			if (r.getType() == RankType.USER) {
    				Chat.player(event.getPlayer(), "&4[Lobby] &cThat server is full! Donate to join full servers!");
    				Chat.player(event.getPlayer(), "&4[Lobby] &7Visit &ewww.mczone.co/shop &7to upgrade.");
    				return;
    			}
    		}
    		
    		Chat.player(event.getPlayer(), "&7&oConnecting you to " + game.getTitle());
    		Util.connect(event.getPlayer(), game.getAddress());
    		
    		return;
    	}
	}
    
	@EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Location to = event.getTo();
        Location from = event.getFrom();
        World world = to.getWorld();

        if (to.clone().add(0, -1, 0).getBlock().getTypeId() != FLOOR)
        	return;
        
        if (from.clone().add(0, -1, 0).getBlock().getTypeId() == FLOOR)
        	return;
        
        for (Block block : getPortalNear(world, to.getBlockX(), to.getBlockY(), to.getBlockZ())) {
            for (BlockFace bf : BlockFace.values()) {
                Block relative = block.getRelative(bf);
                if (relative.getTypeId() == SIGN) {
                	
                	// Specific server
                	Sign sign = (Sign) relative.getState();
                	Game game = null;
                	for (Game g : Game.getList()) {
                		String line_1 = Chat.stripColor(sign.getLine(0));
                		String line_2 = Chat.stripColor(sign.getLine(1));
                		if (g.getLine_1().equalsIgnoreCase(line_1))
                    		if (g.getLine_2().equalsIgnoreCase(line_2))
                				game = g;
                	}
                	
                	if (game != null) {
                		Chat.player(event.getPlayer(), "&7&oConnecting you to " + game.getTitle());
                		Util.connect(event.getPlayer(), game.getAddress());
                		return;
                	}
                	
                	// Minigame with range of servers
                    MiniGame g = null;
                	ServerSign server = null;
                    for (MiniGame ga : MiniGame.getList()) {                        	
                    	for (ServerSign s : ga.getSigns()) {
                    		if (s.getBlock().getX() == relative.getX() && s.getBlock().getZ() == relative.getZ()) {
                    			server = s;
                    			g = ga;
                    			break;
                    		}
                    	}
                    }
                    
                    if (g == null || server == null)
                    	return;
                    
                    if (server.isUsed() == false) {
                    	sign.setLine(1, "");
                    	sign.setLine(2, Chat.colors("&0&lNo Servers"));
                    	sign.setLine(3, "");
                    	return;
                    }
                	
                	Util.connect(event.getPlayer(), server.getCurrent().getAddress());
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
        
        Chat.log(b0 + " and " + b1);

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
                        Chat.log(world.getBlockAt(x + b0 * i, y + j, z + b1 * i).getType().name());
                    }
                }
            }
        }
        return blocks;
    }
}
