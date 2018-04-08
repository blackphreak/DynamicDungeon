package com.caxerx.mc.dynamicdungeon.command.editor.trigger;

import com.caxerx.mc.dynamicdungeon.command.CommandNode;
import com.caxerx.mc.dynamicdungeon.command.DefaultCommand;
import com.caxerx.mc.dynamicdungeon.dungeonobject.trigger.InteractTrigger;
import com.caxerx.mc.dynamicdungeon.dungeonobject.trigger.LocationTrigger;
import com.caxerx.mc.dynamicdungeon.dungeonobject.trigger.MobKillTrigger;


public class TriggerAddDefaultCommand extends DefaultCommand {

    public TriggerAddDefaultCommand(CommandNode parent) {
        super(parent, "add", "dynamicdungeon.admin", "Add trigger", null);
        addSub(new TriggerAddObjectCommand(this, "interact", InteractTrigger.class));
        addSub(new TriggerAddObjectCommand(this, "location", LocationTrigger.class));
        addSub(new TriggerAddObjectCommand(this, "mobkill", MobKillTrigger.class));
    }

}
