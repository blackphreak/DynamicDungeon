package com.caxerx.mc.dynamicdungeon.object.base;

import com.caxerx.mc.dynamicdungeon.object.DDField;
import com.caxerx.mc.dynamicdungeon.object.LocationDungeonObject;
import lombok.Data;

@Data
public class DungeonSchematicDecorate extends LocationDungeonObject {
    @DDField(name = "Schematic File Name")
    private String schematicFileName;
}
