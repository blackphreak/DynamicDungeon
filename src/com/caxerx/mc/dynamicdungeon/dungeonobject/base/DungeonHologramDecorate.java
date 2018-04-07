package com.caxerx.mc.dynamicdungeon.object.base;

import com.caxerx.mc.dynamicdungeon.object.DDField;
import com.caxerx.mc.dynamicdungeon.object.LocationDungeonObject;
import lombok.Data;

@Data
public class DungeonHologramDecorate extends LocationDungeonObject {
    @DDField(name = "Hologram Name")
    private String hologramName;
    @DDField(name = "Y axis Offset")
    private double yOffset;
}
