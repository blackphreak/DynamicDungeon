package com.caxerx.mc.dynamicdungeon.dungeonobject.base;

import com.caxerx.mc.dynamicdungeon.dungeonobject.DDField;
import com.caxerx.mc.dynamicdungeon.dungeonobject.LocationDungeonObject;
import lombok.Data;

@Data
public class DungeonMobSpawner extends LocationDungeonObject {
    @DDField(name = "Spawner Name")
    private String spawnerName;
}
