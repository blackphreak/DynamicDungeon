package me.blackphreak.dynamicdungeon.Objects;

import me.blackphreak.dynamicdungeon.MapBuilding.Editor.DungeonEditSession;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.AbstractMap;

public class DungeonPlaceholderObject extends DungeonObject {
    public DungeonPlaceholderObject() {
        super("Placeholder");
    }

    @Override
    public AbstractMap.SimpleEntry<String, TriConsumer<DungeonEditSession, DungeonObject, Object>> getOperation() {
        return null;
    }
}
