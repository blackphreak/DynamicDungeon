package me.blackphreak.dynamicdungeon.Objects;

import me.blackphreak.dynamicdungeon.MapBuilding.Editor.DungeonEditSession;
import me.blackphreak.dynamicdungeon.gb;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.AbstractMap;

public class DungeonSpawn extends LocationObject {
    private cLocation loc;

    public DungeonSpawn(int x, int y, int z) {
        super("spawn", x, y, z);
        loc = new cLocation(gb.dgWorldName, x, y, z);
    }

    public void setLoc(cLocation loc) {
        this.loc = loc;
    }

    @Override
    public AbstractMap.SimpleEntry<String, TriConsumer<DungeonEditSession, DungeonObject, Object>> getOperation() {
        return null;
    }

    @Override
    public String toString() {
        return "DungeonSpawn:\n" + super.toString();
    }
}
