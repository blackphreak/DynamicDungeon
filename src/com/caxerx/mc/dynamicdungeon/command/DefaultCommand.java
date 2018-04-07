package com.caxerx.mc.dynamicdungeon.command;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class DefaultCommand extends CommandNode {
    public DefaultCommand(CommandNode parent, String command, String permission, String description, String usage) {
        super(parent, command, permission, description, usage);
    }

    @Override
    public boolean executeCommand(CommandSender sender, List<String> args) {
        HelpOutputBuilder builder = new HelpOutputBuilder(this);
        getSubCommands().stream().filter(cmd -> sender.hasPermission(cmd.getPermission())).forEach(builder::append);
        sender.spigot().sendMessage(builder.build());
        return true;
    }

    @Override
    public List<String> executeTabCompletion(CommandSender sender, List<String> args) {
        ArrayList<String> result = new ArrayList<>();
        getSubCommands().forEach(sub -> result.add(sub.getAlias().get(0)));
        return result;
    }
}
