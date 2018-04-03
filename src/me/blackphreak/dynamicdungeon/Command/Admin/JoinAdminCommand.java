package me.blackphreak.dynamicdungeon.Command.Admin;

import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.Command.CommandArgumentException;
import me.blackphreak.dynamicdungeon.Command.CommandNode;
import me.blackphreak.dynamicdungeon.Command.CommandSenderException;
import me.blackphreak.dynamicdungeon.gb;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class JoinAdminCommand extends CommandNode {
    public JoinAdminCommand(CommandNode parent) {
        super(parent, "join", "dynamicdungeon.admin", "join the existing session.", "<SessionID>");
    }

    @Override
    public boolean executeCommand(CommandSender sender, List<String> args) {
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandSenderException("Player");
        }

        if (args.size() == 0) {
            throw new CommandArgumentException("Session ID");
        }

        Player p = (Player) sender;
        try {
            DungeonSession s = gb.dungeons.get(Integer.valueOf(args.get(0)));
            s.join(p);
        } catch (NumberFormatException e) {
            throw new CommandArgumentException("Session ID");
        } catch (NullPointerException e) {
            sender.sendMessage("Dungeon Session not found.");
        }

        return true;
    }

    @Override
    public List<String> executeTabCompletion(CommandSender sender, List<String> args) {
        return null;
    }
}
