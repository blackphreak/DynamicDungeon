package com.caxerx.mc.dynamicdungeon.dungeonobject.base;

import com.caxerx.mc.dynamicdungeon.dungeonobject.DDField;
import com.caxerx.mc.dynamicdungeon.dungeonobject.LocationDungeonObject;
import lombok.Data;

@Data
public class DungeonSchematicDecorate extends LocationDungeonObject {
    @DDField(name = "Schematic File Name")
    private String schematicFileName;
}
