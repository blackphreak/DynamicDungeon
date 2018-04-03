package me.blackphreak.dynamicdungeon.Command.Admin;

import me.blackphreak.dynamicdungeon.MapBuilding.Hub.SaveDungeon;
import me.blackphreak.dynamicdungeon.Command.CommandArgumentException;
import me.blackphreak.dynamicdungeon.Command.CommandNode;
import me.blackphreak.dynamicdungeon.Command.CommandSenderException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SaveAdminCommand extends CommandNode {
    public SaveAdminCommand(CommandNode parent) {
        super(parent, "save", "dynamicdungeon.admin", "saving a new dungeon.", "<DungeonSessionName>");
    }

    @Override
    public boolean executeCommand(CommandSender sender, List<String> args) {
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandSenderException("Player");
        }
        if (args.size() == 0) {
            throw new CommandArgumentException("Dungeon Name");
        }
        sender.sendMessage("Saving DungeonSchematic[" + args.get(0) + "] ...");
        SaveDungeon.saveDungeon((Player) sender, args.get(0));
        sender.sendMessage("Saved!");
        return true;
    }

    @Override
    public List<String> executeTabCompletion(CommandSender sender, List<String> args) {
        return null;
    }
}
