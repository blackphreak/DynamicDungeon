package com.caxerx.mc.dynamicdungeon.command.editor.trigger.action;

import com.caxerx.mc.dynamicdungeon.command.CommandNode;
import com.caxerx.mc.dynamicdungeon.command.DefaultCommand;
import me.blackphreak.dynamicdungeon.dungeonobject.action.CheckPointAction;

public class ActionAddDefaultCommand extends DefaultCommand {

    public ActionAddDefaultCommand(CommandNode parent) {
        super(parent, "action", "dynamicdungeon.admin", "add a action to selected trigger", null);
        addSub(new ActionAddObjectCommand(this,"checkpoint",CheckPointAction.class));
    }
}
