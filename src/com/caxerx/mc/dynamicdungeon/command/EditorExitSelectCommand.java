package com.caxerx.mc.dynamicdungeon.command;

import com.caxerx.mc.dynamicdungeon.command.manager.DungeonSelectManager;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class EditorExitSelectCommand extends CommandNode {

    public EditorExitSelectCommand(CommandNode parent) {
        super(parent, "exit", "dynamicdungeon.admin", "Exit dungeon edit mode", null);
    }

    @Override
    public boolean executeCommand(CommandSender sender, List<String> args) {
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandSenderException("Player");
        }
        DungeonSelectManager.INSTANCE.deselectDungeon((Player) sender);
        return true;
    }

    @Override
    public List<String> executeTabCompletion(CommandSender sender, List<String> args) {
        return null;
    }
}
