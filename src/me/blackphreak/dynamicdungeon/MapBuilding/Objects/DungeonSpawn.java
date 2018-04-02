package me.blackphreak.dynamicdungeon.MapBuilding.Objects;

import java.util.AbstractMap;
import java.util.function.BiConsumer;

public class DungeonSpawn extends DungeonObject {
    private TeleportLocation loc;

    public DungeonSpawn(int x, int y, int z) {
        super("spawn", x, y, z);
        loc = new TeleportLocation("dungeonMap", x, y, z);
    }

    public void setLoc(TeleportLocation loc) {
        this.loc = loc;
    }

    @Override
    public AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, String>> getOperation() {
        return null;
    }

    @Override
    public String toString() {
        return "DungeonSpawn:\n" + super.toString();
    }
}
