package com.caxerx.mc.dynamicdungeon.object.base;

import com.caxerx.mc.dynamicdungeon.object.DDField;
import com.caxerx.mc.dynamicdungeon.object.LocationDungeonObject;
import lombok.Data;

@Data
public class DungeonMobSpawner extends LocationDungeonObject {
    @DDField(name = "Spawner Name")
    private String spawnerName;
}
