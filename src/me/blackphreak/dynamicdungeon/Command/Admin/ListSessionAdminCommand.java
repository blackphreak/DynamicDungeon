package me.blackphreak.dynamicdungeon.Command.Admin;

import com.caxerx.mc.dynamicdungeon.command.CommandNode;
import me.blackphreak.dynamicdungeon.gb;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ListSessionAdminCommand extends CommandNode {
    public ListSessionAdminCommand(CommandNode parent) {
        super(parent, "lss", "dynamicdungeon.admin", "list all sessions.", null);
        addAlias("listsession");
    }

    @Override
    public boolean executeCommand(CommandSender sender, List<String> args) {
        gb.listOutSessions(sender);
        return true;
    }

    @Override
    public List<String> executeTabCompletion(CommandSender sender, List<String> args) {
        return null;
    }
}
