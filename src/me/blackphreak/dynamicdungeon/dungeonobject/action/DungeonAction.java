package me.blackphreak.dynamicdungeon.dungeonobject.action;

import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import me.blackphreak.dynamicdungeon.dungeonobject.DungeonObject;

public abstract class DungeonAction extends DungeonObject {
    @DDField(name = "Trigger By")
    private String triggerBy;

    public abstract void action(DungeonSession dg);
}
