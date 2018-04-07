package me.blackphreak.dynamicdungeon.Command.Admin;

import me.blackphreak.dynamicdungeon.MapBuilding.BuilderV3;
import com.caxerx.mc.dynamicdungeon.command.CommandArgumentException;
import com.caxerx.mc.dynamicdungeon.command.CommandNode;
import com.caxerx.mc.dynamicdungeon.command.CommandSenderException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class BuildAdminCommand extends CommandNode {
    public BuildAdminCommand(CommandNode parent) {
        super(parent, "build", "dynamicdungeon.admin", "start creating a new session.", null);
    }

    @Override
    public boolean executeCommand(CommandSender sender, List<String> args) {
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandSenderException("Player");
        }

        if (args.size() == 0) {
            throw new CommandArgumentException("Dungeon Name");
        }
        Player p = (Player) sender;
        BuilderV3.build(p, args.get(0));
        sender.sendMessage("building DungeonSession: " + args.get(0));

        return true;
    }

    @Override
    public List<String> executeTabCompletion(CommandSender sender, List<String> args) {
        return null;
    }
}
