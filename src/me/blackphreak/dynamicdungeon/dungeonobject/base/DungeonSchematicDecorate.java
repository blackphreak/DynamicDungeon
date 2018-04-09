package me.blackphreak.dynamicdungeon.dungeonobject.base;

import lombok.Data;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import me.blackphreak.dynamicdungeon.dungeonobject.LocationDungeonObject;

@Data
public class DungeonSchematicDecorate extends LocationDungeonObject {
    @DDField(name = "Schematic File Name")
    private String schematicFileName;
}
