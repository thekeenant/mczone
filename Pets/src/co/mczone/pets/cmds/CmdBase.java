package co.mczone.pets.cmds;

import co.mczone.pets.Pets;

public class CmdBase {
    public CmdBase() {
        Pets.getInstance().getCommand("pet").setExecutor(new PetCmd());
    }
}
