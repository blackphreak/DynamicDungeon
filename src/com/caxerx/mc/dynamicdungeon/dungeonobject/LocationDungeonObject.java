package com.caxerx.mc.dynamicdungeon.dungeonobject;

import lombok.Data;

@Data
public abstract class LocationDungeonObject extends DungeonObject {
    @DDField(name = "Location")
    private DungeonLocation location;
}
