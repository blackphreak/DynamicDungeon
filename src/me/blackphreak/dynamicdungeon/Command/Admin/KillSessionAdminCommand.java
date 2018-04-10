package me.blackphreak.dynamicdungeon.Command.Admin;

import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import com.caxerx.mc.dynamicdungeon.command.CommandArgumentException;
import com.caxerx.mc.dynamicdungeon.command.CommandNode;
import me.blackphreak.dynamicdungeon.gb;
import org.bukkit.command.CommandSender;

import java.util.List;

public class KillSessionAdminCommand extends CommandNode {
    public KillSessionAdminCommand(CommandNode parent) {
        super(parent, "killsession", "dynamicdungeon.admin", "kill an existing session.", "<SessionID>");
    }

    @Override
    public boolean executeCommand(CommandSender sender, List<String> args) {
        if (args.size() == 0) {
            throw new CommandArgumentException("Session ID");
        }

        try {
            DungeonSession s1 = gb.dungeons.get(Integer.valueOf(args.get(0)));
            if (s1 == null) {
                sender.sendMessage("Dungeon Session not found.");
            } else {
                s1.killSession(sender);
            }
        } catch (NumberFormatException e) {
            throw new CommandArgumentException("Session ID");
        }
        return true;
    }

    @Override
    public List<String> executeTabCompletion(CommandSender sender, List<String> args) {
        return null;
    }
}
