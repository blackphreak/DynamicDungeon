package me.blackphreak.dynamicdungeon.Objects;

import me.blackphreak.dynamicdungeon.MapBuilding.Editor.DungeonEditSession;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.AbstractMap;

public class DungeonMobSpawner extends LocationObject {
    private String spawner;

    public DungeonMobSpawner(int x, int y, int z) {
        super("mobspawner", x, y, z);
    }

    
    private static AbstractMap.SimpleEntry<String, TriConsumer<DungeonEditSession, DungeonObject, Object>> spawnerInput = new AbstractMap.SimpleEntry<>("SpawnerName", (es, dobj, input) -> ((DungeonMobSpawner) dobj).spawner = (String) input);
    private transient boolean received = false;

    @Override
    public AbstractMap.SimpleEntry<String, TriConsumer<DungeonEditSession, DungeonObject, Object>> getOperation() {
        if (!received) {
            received = true;
            return spawnerInput;
        }
        return null;
    }

    public void setSpawner(String spawner) {
        this.spawner = spawner;
    }

    @Override
    public String toString() {
        String r = "DungeonMobSpawner:\n" +super.toString();
        r += "SpawnerName: " + spawner + "\n";
        return r;
    }

    public String getSpawner() {
        return spawner;
    }
}