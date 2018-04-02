package me.blackphreak.dynamicdungeon.MapBuilding.Objects;

import java.util.AbstractMap;
import java.util.function.BiConsumer;

public class DungeonPlaceholderObject extends DungeonObject {
    public DungeonPlaceholderObject() {
        super("Placeholder", -1, -1, -1);
    }

    @Override
    public AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>> getOperation() {
        return null;
    }
}
