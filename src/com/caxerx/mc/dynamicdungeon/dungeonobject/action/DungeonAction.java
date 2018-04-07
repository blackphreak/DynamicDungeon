package com.caxerx.mc.dynamicdungeon.dungeonobject.action;

import com.caxerx.mc.dynamicdungeon.dungeonobject.DDField;

public abstract class DungeonAction {
    @DDField(name = "Trigger By")
    private String triggerBy;

    abstract void action();
}
