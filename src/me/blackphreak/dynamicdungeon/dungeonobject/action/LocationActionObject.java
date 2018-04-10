package me.blackphreak.dynamicdungeon.dungeonobject.action;

import lombok.Data;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import me.blackphreak.dynamicdungeon.dungeonobject.DungeonLocation;

@Data
public abstract class LocationActionObject extends DungeonAction {
    @DDField(name = "Location")
    private DungeonLocation location;
}
