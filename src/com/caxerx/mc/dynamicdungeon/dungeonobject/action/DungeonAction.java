package com.caxerx.mc.dynamicdungeon.object.action;

import com.caxerx.mc.dynamicdungeon.object.DDField;

public abstract class DungeonAction {
    @DDField(name = "Trigger By")
    private String triggerBy;

    abstract void action();
}
