package me.blackphreak.dynamicdungeon.Command.Admin;

import com.boydti.fawe.object.FawePlayer;
import com.sk89q.worldedit.regions.Region;
import me.blackphreak.dynamicdungeon.MapBuilding.Editor.DungeonEditSessionManager;
import me.blackphreak.dynamicdungeon.Messages.msg;
import me.blackphreak.dynamicdungeon.Command.CommandArgumentException;
import me.blackphreak.dynamicdungeon.Command.CommandNode;
import me.blackphreak.dynamicdungeon.Command.CommandSenderException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class EditAdminCommand extends CommandNode {

    public EditAdminCommand(CommandNode parent) {
        super(parent, "edit", "dynamicdungeon.admin", "create & edit a new dungeon.", "<DungeonSessionName>");
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
        Region r = FawePlayer.wrap(p).getSelection();

        if (r == null) {
            msg.send(p, "You must select an area with AXE first!!");
            return true;
        }

        DungeonEditSessionManager.getInstance().newSession(p, args.get(0), r);
        return true;
    }

    @Override
    public List<String> executeTabCompletion(CommandSender sender, List<String> args) {
        return null;
    }
}
