package me.blackphreak.dynamicdungeon.Command;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandNode {

    @Getter
    private String description;
    
    @Getter
    private String usage;

    @Getter
    private ArrayList<CommandNode> subCommands = new ArrayList<>();

    @Getter
    private ArrayList<String> alias = new ArrayList<>();

    @Getter
    private String permission;

    @Getter
    private CommandNode parent;

    public CommandNode(CommandNode parent, @NonNull String command, String permission, @NonNull String description, String usage) {
        this.parent = parent;
        this.alias.add(command);
        this.permission = permission;
        this.description = description;
        this.usage = usage;
    }

    public void addAlias(@NonNull String ali) {
        if (!alias.contains(ali)) {
            this.alias.add(ali);
        }
    }

    public void addAllAliases(@NonNull List<String> aliases) {
        aliases.forEach(ali -> {
            if (!alias.contains(ali)) {
                alias.add(ali);
            }
        });
    }

    public void addSub(@NonNull CommandNode c) {
        subCommands.add(c);
    }

    public abstract boolean executeCommand(@NonNull CommandSender sender, @NonNull List<String> args);

    public abstract List<String> executeTabCompletion(@NonNull CommandSender sender, @NonNull List<String> args);

    public boolean invokeCommand(@NonNull CommandSender sender, @NonNull List<String> args) {
        if (args == null || args.size() == 0) {
            if (permission == null || sender.hasPermission(permission)) {
                return executeCommand(sender, args);
            } else {
                throw new CommandPermissionException(permission);
            }
        }

        for (CommandNode subCommand : subCommands) {
            if (subCommand.match(args.get(0))) {
                List<String> passArg = new ArrayList<>(args);
                passArg.remove(0);
                return subCommand.invokeCommand(sender, passArg);
            }
        }

        if (permission == null || sender.hasPermission(permission)) {
            return executeCommand(sender, args);
        } else {
            throw new CommandPermissionException(permission);
        }
    }

    public List<String> invokeTabCompletion(@NonNull CommandSender sender, @NonNull List<String> args) {
        if (args == null || args.size() == 0) {
            if (permission != null && sender.hasPermission(permission)) {
                return executeTabCompletion(sender, args);
            } else {
                throw new CommandPermissionException(permission);
            }
        }

        for (CommandNode subCommand : subCommands) {
            if (subCommand.match(args.get(0))) {
                List<String> passArg = new ArrayList<>(args);
                passArg.remove(0);
                return subCommand.invokeTabCompletion(sender, passArg);
            }
        }

        if (permission == null || sender.hasPermission(permission)) {
            return executeTabCompletion(sender, args);
        } else {
            throw new CommandPermissionException(permission);
        }
    }

    public boolean match(@NonNull String args) {
        for (String ali : alias) {
            if (args.equalsIgnoreCase(ali)) {
                return true;
            }
        }
        return false;
    }
}
