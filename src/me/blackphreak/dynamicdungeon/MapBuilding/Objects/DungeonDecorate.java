package me.blackphreak.dynamicdungeon.MapBuilding.Objects;

import java.util.AbstractMap;
import java.util.function.BiConsumer;

public class DungeonDecorate extends DungeonObject {

    public DungeonDecorate(int x, int y, int z) {
        super("decorate", x, y, z);
    }

    @Override
    public AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, String>> getOperation() {
        return null;
    }
}
