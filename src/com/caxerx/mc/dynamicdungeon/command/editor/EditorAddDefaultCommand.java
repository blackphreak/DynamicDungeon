package com.caxerx.mc.dynamicdungeon.command.editor;

import com.caxerx.mc.dynamicdungeon.command.CommandNode;
import com.caxerx.mc.dynamicdungeon.command.DefaultCommand;
import com.caxerx.mc.dynamicdungeon.dungeonobject.base.DungeonHologramDecorate;
import com.caxerx.mc.dynamicdungeon.dungeonobject.base.DungeonMobSpawner;
import com.caxerx.mc.dynamicdungeon.dungeonobject.base.DungeonSchematicDecorate;
import com.caxerx.mc.dynamicdungeon.dungeonobject.base.DungeonSpawn;

public class EditorAddDefaultCommand extends DefaultCommand {
    public EditorAddDefaultCommand(CommandNode parent) {
        super(parent, "add", "dynamicdungeon.admin", "Add a object to selected dungeon", null);
        addSub(new EditorAddObjectCommand(this, "spawn", DungeonSpawn.class));
        addSub(new EditorAddObjectCommand(this, "mobspawner", DungeonMobSpawner.class));
        addSub(new EditorAddObjectCommand(this, "hologram", DungeonHologramDecorate.class));
        addSub(new EditorAddObjectCommand(this, "schematic", DungeonSchematicDecorate.class));
    }
}
