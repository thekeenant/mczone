package co.mczone.sg.api;

import org.bukkit.command.CommandSender;

public interface SubCommand {
    public boolean execute(CommandSender sender, String[] args);
}
