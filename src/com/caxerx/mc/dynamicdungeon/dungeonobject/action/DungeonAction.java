package com.caxerx.mc.dynamicdungeon.dungeonobject.action;

import com.caxerx.mc.dynamicdungeon.dungeonobject.DDField;
import com.caxerx.mc.dynamicdungeon.dungeonobject.DungeonObject;

public abstract class DungeonAction extends DungeonObject {
    @DDField(name = "Trigger By")
    private String triggerBy;

    abstract void action();
}
