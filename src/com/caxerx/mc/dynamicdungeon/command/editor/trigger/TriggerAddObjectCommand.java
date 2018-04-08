package com.caxerx.mc.dynamicdungeon.command.editor.trigger;

import com.caxerx.mc.dynamicdungeon.DungeonObjectBuilder;
import com.caxerx.mc.dynamicdungeon.command.CommandArgumentException;
import com.caxerx.mc.dynamicdungeon.command.CommandNode;
import com.caxerx.mc.dynamicdungeon.command.CommandSenderException;
import com.caxerx.mc.dynamicdungeon.command.manager.DungeonManager;
import com.caxerx.mc.dynamicdungeon.command.manager.DungeonSelectManager;
import com.caxerx.mc.dynamicdungeon.dungeonobject.DDField;
import com.caxerx.mc.dynamicdungeon.dungeonobject.DungeonObject;
import com.caxerx.mc.dynamicdungeon.dungeonobject.trigger.DungeonTrigger;
import com.caxerx.mc.lib.userinput.ChatInput;
import kotlin.Pair;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TriggerAddObjectCommand extends CommandNode {
    private Class<? extends DungeonTrigger> clz;

    public TriggerAddObjectCommand(CommandNode parent, String command, Class<? extends DungeonTrigger> clz) {
        super(parent, command, "dynamicdungeon.admin", "Add a " + command + " trigger object to selected dungeon", null);
        this.clz = clz;
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
        HashMap<Integer, String> preInputArgs;

        try {
            preInputArgs = new HashMap<>();
            for (String arg : args) {
                String[] preinput = arg.split(":");
                preInputArgs.put(Integer.parseInt(preinput[0]), preinput[1]);
            }
        } catch (Exception e) {
            throw new CommandArgumentException("Pre-input arguments format");
        }

        List<Pair<String, Class<?>>> inputConstraint = new ArrayList<>();
        DungeonObjectBuilder.getAllField(clz).forEach(field -> inputConstraint.add(new Pair<>(field.getAnnotation(DDField.class).name(), field.getType())));
        preInputArgs.keySet().forEach(idx -> inputConstraint.remove((int) idx));
        new ChatInput((Player) sender, inputConstraint, input -> {
            preInputArgs.forEach(input::add);
            String dungeon = DungeonSelectManager.INSTANCE.getSelectedDungeon((Player) sender).getFirst();
            DungeonTrigger obj = DungeonObjectBuilder.getDungeonObject(clz, input);
            DungeonManager.INSTANCE.getDungeon(dungeon).add(obj);
            DungeonManager.INSTANCE.saveToFile();
            DungeonSelectManager.INSTANCE.selectTrigger((Player) sender, obj.getTriggerName());
            sender.sendMessage(clz.getSimpleName() + " Trigger Created and Selected");
        });
        return true;
    }

    @Override
    public List<String> executeTabCompletion(CommandSender sender, List<String> args) {
        return null;
    }
}
