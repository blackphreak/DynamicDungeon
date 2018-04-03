package me.blackphreak.dynamicdungeon.Command.Admin;

import me.blackphreak.dynamicdungeon.Command.CommandNode;
import org.bukkit.command.CommandSender;

import java.util.List;

public class DestroyAdminCommand extends CommandNode {
    public DestroyAdminCommand(CommandNode parent) {
        super(parent, "destroy", "dynamicdungeon.admin", "destroy an existing session.", null);
    }

    @Override
    public boolean executeCommand(CommandSender sender, List<String> args) {
        sender.sendMessage("Command Unimplemented");
        return true;
    }

    @Override
    public List<String> executeTabCompletion(CommandSender sender, List<String> args) {
        return null;
    }
}
