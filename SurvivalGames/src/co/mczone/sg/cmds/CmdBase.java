package co.mczone.sg.cmds;

import co.mczone.api.server.Hive;
import co.mczone.sg.SurvivalGames;

public class CmdBase {
    public CmdBase() {
    	Hive.getInstance().registerCommand(SurvivalGames.getInstance(), "admin", new AdminCmd());
    	Hive.getInstance().registerCommand(SurvivalGames.getInstance(), "help", new HelpCmd());
    	Hive.getInstance().registerCommand(SurvivalGames.getInstance(), "vote", new VoteCmd());
    	Hive.getInstance().registerCommand(SurvivalGames.getInstance(), "maps", new MapsCmd());
    	Hive.getInstance().registerCommand(SurvivalGames.getInstance(), "lobby", new SpawnCmd());
    	Hive.getInstance().registerCommand(SurvivalGames.getInstance(), "world", new WorldCmd());
    }
}
