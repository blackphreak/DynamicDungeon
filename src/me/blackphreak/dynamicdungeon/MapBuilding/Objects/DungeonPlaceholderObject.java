package me.blackphreak.dynamicdungeon.MapBuilding.Objects;

import me.blackphreak.dynamicdungeon.MapBuilding.Objects.Triggers.DungeonTrigger;

import java.util.AbstractMap;
import java.util.function.BiConsumer;

public class DungeonPlaceholderObject extends DungeonObject {
    public DungeonPlaceholderObject() {
        super("Placeholder", -1, -1, -1);
    }

    public DungeonPlaceholderObject(DungeonTrigger t) {
        this();
    }

    @Override
    public AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>> getOperation() {
        return null;
    }

    public DungeonTrigger getParent() {
        return null;
    }
}
