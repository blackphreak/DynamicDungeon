package com.caxerx.mc.dynamicdungeon.dungeonobject;

import lombok.Data;

@Data
public abstract class LocationDungeonObject {
    @DDField(name = "Location")
    private DungeonLocation location;
}
