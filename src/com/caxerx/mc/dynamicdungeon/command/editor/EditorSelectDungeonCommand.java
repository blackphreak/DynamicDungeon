package com.caxerx.mc.dynamicdungeon.command.editor;


import com.boydti.fawe.object.FawePlayer;
import com.caxerx.mc.dynamicdungeon.command.CommandArgumentException;
import com.caxerx.mc.dynamicdungeon.command.CommandNode;
import com.caxerx.mc.dynamicdungeon.command.CommandSenderException;
import com.caxerx.mc.dynamicdungeon.command.manager.DungeonEditingManager;
import com.caxerx.mc.dynamicdungeon.command.manager.DungeonManager;
import com.caxerx.mc.dynamicdungeon.command.manager.DungeonSelectManager;
import com.sk89q.worldedit.regions.Region;
import me.blackphreak.dynamicdungeon.Messages.msg;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EditorSelectDungeonCommand extends CommandNode {

    public EditorSelectDungeonCommand(CommandNode parent) {
        super(parent, "sd", "dynamicdungeon.admin", "Select a dungeon before operate", "<DungeonName>");
        addAlias("selectdungeon");
    }

    @Override
    public boolean executeCommand(CommandSender sender, List<String> args) {
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandSenderException("Player");
        }
        if (args.size() <= 0) {
            throw new CommandArgumentException("Dungeon Name");
        }
        Player p = (Player) sender;

        Region r = FawePlayer.wrap(p).getSelection();
        if (r == null) {
            msg.send(p, "You must select an area with AXE first!!");
            return true;
        }

        if (!DungeonManager.INSTANCE.hasDungeon(args.get(0))) {
            sender.sendMessage("Dungeon Not Exist, creating");
        }
        DungeonSelectManager.INSTANCE.selectDungeon(p, args.get(0), r);
        DungeonEditingManager.getInstnace().enterEditMode(p);
        sender.sendMessage("Dungeon Selected");
        DungeonManager.INSTANCE.createDungeon(args.get(0));
        return true;
    }

    @Override
    public List<String> executeTabCompletion(CommandSender sender, List<String> args) {
        return new ArrayList<>(DungeonManager.INSTANCE.getDungeons());
    }
}
