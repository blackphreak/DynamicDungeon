package com.caxerx.mc.dynamicdungeon.command.editor;

import com.caxerx.mc.dynamicdungeon.command.CommandNode;
import com.caxerx.mc.dynamicdungeon.command.DefaultCommand;
import com.caxerx.mc.dynamicdungeon.command.editor.trigger.TriggerDefaultCommand;

public class EditorDefaultCommand extends DefaultCommand {
    public EditorDefaultCommand() {
        super(null, "dde", "dynamicdungeon.admin", "Default command of dungeon editor", null);
        addSub(new EditorSelectDungeonCommand(this));
        addSub(new EditorAddDefaultCommand(this));
        addSub(new TriggerDefaultCommand(this));
    }
}
