package me.blackphreak.dynamicdungeon.Command.Admin;

import com.boydti.fawe.object.FawePlayer;
import com.caxerx.mc.dynamicdungeon.command.CommandArgumentException;
import com.caxerx.mc.dynamicdungeon.command.CommandNode;
import com.caxerx.mc.dynamicdungeon.command.CommandSenderException;
import com.sk89q.worldedit.regions.Region;
import me.blackphreak.dynamicdungeon.Messages.msg;
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
        
        p.performCommand("dde sd " + args.get(0));
        return true;
    }

    @Override
    public List<String> executeTabCompletion(CommandSender sender, List<String> args) {
        return null;
    }
}
