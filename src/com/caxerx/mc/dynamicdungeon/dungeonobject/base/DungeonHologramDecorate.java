package com.caxerx.mc.dynamicdungeon.dungeonobject.base;

import com.caxerx.mc.dynamicdungeon.dungeonobject.DDField;
import com.caxerx.mc.dynamicdungeon.dungeonobject.LocationDungeonObject;
import lombok.Data;

@Data
public class DungeonHologramDecorate extends LocationDungeonObject {
    @DDField(name = "Hologram Name")
    private String hologramName;
    @DDField(name = "Y axis Offset")
    private double yOffset;
}
