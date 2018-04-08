package com.caxerx.mc.dynamicdungeon.command.editor.trigger;

import com.caxerx.mc.dynamicdungeon.command.CommandNode;
import com.caxerx.mc.dynamicdungeon.command.DefaultCommand;
import com.caxerx.mc.dynamicdungeon.command.editor.trigger.action.ActionAddDefaultCommand;

public class TriggerDefaultCommand extends DefaultCommand {
    public TriggerDefaultCommand(CommandNode parent) {
        super(parent, "trigger", "dynamicdungeon.admin", "Edit triggers", null);
        addSub(new TriggerAddDefaultCommand(this));
        addSub(new TriggerSelectCommand(this));
        addSub(new ActionAddDefaultCommand(this));
    }
}
