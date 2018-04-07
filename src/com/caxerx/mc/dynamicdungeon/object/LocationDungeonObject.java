package com.caxerx.mc.dynamicdungeon.object;

import lombok.Data;

@Data
public abstract class LocationDungeonObject {
    @DDField(name = "Location")
    private DungeonLocation location;
}
