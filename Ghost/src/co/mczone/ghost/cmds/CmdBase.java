package co.mczone.ghost.cmds;

import co.mczone.api.server.Hive;
import co.mczone.ghost.Ghost;

public class CmdBase {
    public CmdBase() {
    	Hive.getInstance().registerCommand(Ghost.getInstance(), "arenas", new ArenasCmd());
    	Hive.getInstance().registerCommand(Ghost.getInstance(), "ghost", new GhostCmd());
    }
}
