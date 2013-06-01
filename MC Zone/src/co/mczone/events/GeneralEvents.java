package co.mczone.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import co.mczone.MCZone;
import co.mczone.api.players.Gamer;
import co.mczone.api.server.Hive;
import co.mczone.events.custom.SignClickEvent;
import co.mczone.util.Chat;

public class GeneralEvents implements Listener {
	
	public GeneralEvents() {
		MCZone.getInstance().getServer().getPluginManager().registerEvents(this, MCZone.getInstance());
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();
		Gamer g = Hive.getInstance().getGamer(p);
		
		String prefix = "";
		String name = p.getName();
		String msg = event.getMessage();
		
		if (g.getRank() != null)
			prefix = g.getRank().getType().getPrefix();
		
		String result = Chat.colors(prefix + "&7" + name + "&f: ") + msg;
		if (g.getRank().getType().getLevel() >= 5)
			result = Chat.colors(result);
		
		// Symbol % has formatting issues
		event.setFormat(result.replace("%", "%%"));
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Gamer g = Gamer.get(event.getPlayer());
		if (g.isInvisible())
			Gamer.updateHidden();
	}
	
	@EventHandler
	public void onSignClick(PlayerInteractEvent event) {
		if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR)
			return;
		
		Block b = event.getClickedBlock();
		if (b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN) {
			Sign sign = (Sign) b.getState();
			SignClickEvent ev = new SignClickEvent(event.getPlayer(), sign, event.getClickedBlock(), event.getAction() == Action.RIGHT_CLICK_BLOCK);
			Bukkit.getServer().getPluginManager().callEvent(ev);
		}
	}
}
