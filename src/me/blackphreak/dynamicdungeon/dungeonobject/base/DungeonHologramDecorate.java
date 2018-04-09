package me.blackphreak.dynamicdungeon.dungeonobject.base;

import lombok.Data;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import me.blackphreak.dynamicdungeon.dungeonobject.LocationDungeonObject;

@Data
public class DungeonHologramDecorate extends LocationDungeonObject {
    @DDField(name = "Hologram Name")
    private String hologramName;
    @DDField(name = "Y axis Offset")
    private double yOffset;
}
