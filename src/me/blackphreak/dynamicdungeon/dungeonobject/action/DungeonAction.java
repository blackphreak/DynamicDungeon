package me.blackphreak.dynamicdungeon.dungeonobject.action;

import lombok.Data;
import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.dungeonobject.ActionNeeded;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import me.blackphreak.dynamicdungeon.dungeonobject.DungeonObject;

@Data
public abstract class DungeonAction extends DungeonObject {
    @DDField(name = "Trigger By")
    private String triggerBy;

    public abstract void action(DungeonSession dg, ActionNeeded needed);
}
