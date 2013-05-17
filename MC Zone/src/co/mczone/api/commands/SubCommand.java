package co.mczone.api.commands;

import org.bukkit.command.CommandSender;

public interface SubCommand {
    public boolean execute(CommandSender sender, String[] args);
    public String getAbout();
}
