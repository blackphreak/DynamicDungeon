package me.blackphreak.dynamicdungeon.dungeonobject.base;

import com.sk89q.worldedit.math.transform.AffineTransform;
import lombok.Data;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import me.blackphreak.dynamicdungeon.dungeonobject.LocationDungeonObject;

@Data
public class DungeonSchematicDecorate extends LocationDungeonObject {
    @DDField(name = "Schematic File")
    private String schematicFile;
    @DDField(name = "Transformation (scale,rotatex,rotatey,rotatez)")
    private AffineTransform transform;
}
