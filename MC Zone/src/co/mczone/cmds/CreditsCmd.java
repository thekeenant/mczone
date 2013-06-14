package co.mczone.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.mczone.api.players.Gamer;
import co.mczone.util.Chat;

public class CreditsCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Gamer g = Gamer.get(sender);
        Chat.player(g, "&bYou currently have &e" + g.getCredits() + " credits");
        return true;
    }

}
