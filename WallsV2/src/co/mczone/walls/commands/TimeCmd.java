package co.mczone.walls.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.mczone.walls.State;
import co.mczone.walls.timers.PVPTask;
import co.mczone.walls.timers.PreTask;
import co.mczone.walls.timers.PrepTask;
import co.mczone.walls.utils.Chat;

public class TimeCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (State.PVP) {
            Chat.player(sender, "&cThe walls have dropped and the battle has started!");
            return true;
        }
        
        String time = "";
        if (State.PRE) {
            time = Chat.time(PreTask.TIME);
            Chat.player(sender, "&eThe game will begin in " + time + "!");
        }
        else if (State.PREP) {
            time = Chat.time(PrepTask.TIME);
            Chat.player(sender, "&eThe walls will drop in " + time + "!");
        }
        else if (State.PVP) {
            time = Chat.time(2700 - PVPTask.TIME);
            Chat.player(sender, "&eThe game will end in " + time + "!");
        }
        return true;
    }

}
