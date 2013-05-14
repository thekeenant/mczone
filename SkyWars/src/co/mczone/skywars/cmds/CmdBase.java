package co.mczone.skywars.cmds;

import co.mczone.skywars.SkyWars;

public class CmdBase {
    public CmdBase() {
    	SkyWars.getInstance().getCommand("skywars").setExecutor(new SkyWarsCmd());
    }
}
