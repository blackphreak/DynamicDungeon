package me.blackphreak.dynamicdungeon.MapBuilding.Objects;

import me.blackphreak.dynamicdungeon.gb;

import java.util.AbstractMap;
import java.util.function.BiConsumer;

public class DungeonSpawn extends DungeonObject {
    private cLocation loc;

    public DungeonSpawn(int x, int y, int z) {
        super("spawn", x, y, z);
        loc = new cLocation(gb.dgWorldName, x, y, z);
    }

    public void setLoc(cLocation loc) {
        this.loc = loc;
    }

    @Override
    public AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>> getOperation() {
        return null;
    }

    @Override
    public String toString() {
        return "DungeonSpawn:\n" + super.toString();
    }
}
