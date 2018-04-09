package com.caxerx.mc.dynamicdungeon.command.editor;

import com.caxerx.mc.dynamicdungeon.DungeonObjectBuilder;
import com.caxerx.mc.dynamicdungeon.command.CommandArgumentException;
import com.caxerx.mc.dynamicdungeon.command.CommandNode;
import com.caxerx.mc.dynamicdungeon.command.CommandSenderException;
import com.caxerx.mc.dynamicdungeon.command.manager.DungeonManager;
import com.caxerx.mc.dynamicdungeon.command.manager.DungeonSelectManager;
import com.caxerx.mc.lib.userinput.ChatInput;
import kotlin.Pair;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import me.blackphreak.dynamicdungeon.dungeonobject.DungeonObject;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditorAddObjectCommand extends CommandNode {
    private Class<? extends DungeonObject> clz;

    public EditorAddObjectCommand(CommandNode parent, String command, Class<? extends DungeonObject> clz) {
        super(parent, command, "dynamicdungeon.admin", "Add a " + command + " object to selected dungeon", null);
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
            //Location location = ((Player) sender).getLocation();
            //input.add(0, DungeonLocation.createFromBukkitLocation(location).subtract(DungeonLocation.createFromWorldEditVector(DungeonSelectManager.INSTANCE.getSelectedDungeon((Player) sender).getSecond().getMinimumPoint())).toString());
            preInputArgs.forEach(input::add);
            String dungeon = DungeonSelectManager.INSTANCE.getSelectedDungeon((Player) sender).getFirst();
            DungeonManager.INSTANCE.getDungeon(dungeon).add(DungeonObjectBuilder.getDungeonObject(clz, input));
            DungeonManager.INSTANCE.saveToFile();
            sender.sendMessage(clz.getSimpleName() + " Object Created");
        });
        return true;
    }

    @Override
    public List<String> executeTabCompletion(CommandSender sender, List<String> args) {
        return null;
    }
}
