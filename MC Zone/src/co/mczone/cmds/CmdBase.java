package co.mczone.cmds;

import co.mczone.MCZone;

public class CmdBase {
    public CmdBase() {
        MCZone.getInstance().getCommand("ban").setExecutor(new BanCmd());
        MCZone.getInstance().getCommand("unban").setExecutor(new UnbanCmd());
        MCZone.getInstance().getCommand("tempban").setExecutor(new TempbanCmd());
        MCZone.getInstance().getCommand("kick").setExecutor(new KickCmd());
    }
}
