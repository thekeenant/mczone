package co.mczone.lobby;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;

import org.bukkit.scheduler.BukkitRunnable;

import co.mczone.lobby.api.MiniGame;
import co.mczone.lobby.api.OpenServer;
import co.mczone.lobby.api.Query;
import co.mczone.lobby.api.ServerRange;
import co.mczone.lobby.api.ServerSign;
import co.mczone.lobby.util.Util;

public class MiniGameScheduler extends BukkitRunnable {
	@Getter int time = -1;
	boolean isRunning = false;
	@Override
	public void run() {
		time += 1;

		for (final MiniGame g : MiniGame.getList()) {
			Date now = new Date();
			if (g.getLastSearch() != null && now.getTime() - g.getLastSearch().getTime() < 3500)
				return;
			
			new BukkitRunnable() {
				public void run() {
					if (g.isSearching())
						return;
					
					g.setSearching(true);
					
					int find = 0;
					if (g.getOpen().size() != 3) {
						find = 3 - g.getOpen().size();
					}
					
					List<OpenServer> check = g.getOpen();
					
					Iterator<OpenServer> list = g.getOpen().iterator();
					while (list.hasNext()) {
						if (check.size() != g.getOpen().size())
							continue;
						OpenServer serv = list.next();
						Query q = new Query(serv.getAddress().split(":")[0], Integer.valueOf(serv.getAddress().split(":")[1]));
						q.fetchData();
						if (!Util.isOpen(q)) {
							find += 1;
							for (ServerSign s : g.getSigns()) {
								if (s.getCurrent() == serv) {
									s.setCurrent(null);
									s.setUsed(false);
									s.update();
								}
							}
							list.remove();
						}
					}
					
					find = Math.min(find, 3);
					for (ServerRange range : g.getServers()) {
						if (find == 0) 
							break;
						for (int port = range.getMinPort(); port < range.getMaxPort(); port++) {
							if (find <= 0) 
								break;
							
							Query query = new Query(range.getIp(), port);
							query.fetchData();
							if (Util.isOpen(query)) {
								boolean alreadyListed = false;
								for (OpenServer o : g.getOpen())
									if (o.getAddress().equalsIgnoreCase(range.getIp() + ":" + port))
										alreadyListed = true;
								if (alreadyListed)
									continue;
								
								OpenServer open = new OpenServer(range.getIp() + ":" + port, query.getMotd(), query.getPlayersOnline(), query.getMaxPlayers());
								g.getOpen().add(open);
								
								for (ServerSign s : g.getSigns()) {
									boolean alreadyUsed = false;
									for (ServerSign si : g.getSigns())
										if (si.getCurrent() == null || open == null)
											alreadyUsed = false;
										else if (si.getCurrent().getAddress().equals(open.getAddress()))
											alreadyUsed = true;
									
									if (alreadyUsed) {
										break;
									}
									
									if (!s.isUsed()) {
										s.setUsed(true);
										s.setCurrent(open);
										break;
									}
								}
								find -= 1;
							}
						}
					}
					
					
					for (ServerSign s : g.getSigns()) {
						s.update();
					}
					g.setSearching(false);
					g.setLastSearch(new Date());
				}
			}.runTaskAsynchronously(Lobby.getInstance());
		}
		
	}
}
