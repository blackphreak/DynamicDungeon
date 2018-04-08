package com.caxerx.mc.dynamicdungeon.dungeonobject.trigger;

import com.caxerx.mc.dynamicdungeon.dungeonobject.DDField;
import com.caxerx.mc.dynamicdungeon.dungeonobject.DungeonLocation;

public class LocationTrigger extends DungeonTrigger {
    @DDField(name = "Location")
    private DungeonLocation location;
    @DDField(name = "Range")
    private int range;
}
