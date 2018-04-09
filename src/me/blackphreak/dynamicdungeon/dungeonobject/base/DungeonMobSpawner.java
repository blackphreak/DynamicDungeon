package me.blackphreak.dynamicdungeon.dungeonobject.base;

import lombok.Data;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import me.blackphreak.dynamicdungeon.dungeonobject.LocationDungeonObject;

@Data
public class DungeonMobSpawner extends LocationDungeonObject {
    @DDField(name = "Spawner Name")
    private String spawnerName;
}
