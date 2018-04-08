package com.caxerx.mc.dynamicdungeon.command.editor.trigger;

import com.caxerx.mc.dynamicdungeon.command.CommandArgumentException;
import com.caxerx.mc.dynamicdungeon.command.CommandNode;
import com.caxerx.mc.dynamicdungeon.command.CommandSenderException;
import com.caxerx.mc.dynamicdungeon.command.manager.DungeonManager;
import com.caxerx.mc.dynamicdungeon.command.manager.DungeonSelectManager;
import com.caxerx.mc.dynamicdungeon.dungeonobject.DungeonObject;
import com.caxerx.mc.dynamicdungeon.dungeonobject.trigger.DungeonTrigger;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class TriggerSelectCommand extends CommandNode {

    public TriggerSelectCommand(CommandNode parent) {
        super(parent, "sel", "dynamicdungeon.admin", "Select trigger in dungeon", "<TriggerName>");
    }

    @Override
    public boolean executeCommand(CommandSender sender, List<String> args) {
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandSenderException("Player");
        }
        if (!DungeonSelectManager.INSTANCE.isDungeonSelected((Player) sender)) {
            sender.sendMessage("Select dungeon first");
            return true;
        }
        if (args.size() <= 0) {
            throw new CommandArgumentException("Trigger Name");
        }
        String dunName = DungeonSelectManager.INSTANCE.getSelectedDungeon((Player) sender).getFirst();
        List<DungeonObject> dun = DungeonManager.INSTANCE.getDungeon(dunName);
        for (DungeonObject obj : dun) {
            if (obj instanceof DungeonTrigger) {
                String name = ((DungeonTrigger) obj).getTriggerName();
                if (name.equalsIgnoreCase(args.get(0))) {
                    DungeonSelectManager.INSTANCE.selectTrigger((Player) sender, args.get(0));
                    sender.sendMessage("Trigger selected");
                    return true;
                }
            }
        }
        sender.sendMessage("Trigger not exist");
        return true;
    }

    @Override
    public List<String> executeTabCompletion(CommandSender sender, List<String> args) {
        if (sender instanceof Player && DungeonSelectManager.INSTANCE.isDungeonSelected((Player) sender)) {
            String dunName = DungeonSelectManager.INSTANCE.getSelectedDungeon((Player) sender).getFirst();
            List<DungeonObject> dun = DungeonManager.INSTANCE.getDungeon(dunName);
            return dun.stream().filter(o -> o instanceof DungeonTrigger).map(o -> ((DungeonTrigger) o).getTriggerName()).collect(Collectors.toList());
        } else {
            return null;
        }

    }
}
