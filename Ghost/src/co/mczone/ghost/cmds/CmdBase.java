package co.mczone.ghost.cmds;

import co.mczone.ghost.Ghost;

public class CmdBase {
    public CmdBase() {
    	Ghost.getInstance().getCommand("arenas").setExecutor(new ArenasCmd());
    	Ghost.getInstance().getCommand("ghost").setExecutor(new GhostCmd());
    }
}
