package me.blackphreak.dynamicdungeon.MapBuilding.Objects;

import java.util.AbstractMap;
import java.util.function.BiConsumer;

public class DungeonMobSpawner extends DungeonObject {
    private String spawner;

    public DungeonMobSpawner(int x, int y, int z) {
        super("mobspawner", x, y, z);
    }

    
    private static AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>> spawnerInput = new AbstractMap.SimpleEntry<>("SpawnerName", (dobj, input) -> ((DungeonMobSpawner) dobj).spawner = (String) input);    private transient boolean received = false;

    @Override
    public AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>> getOperation() {
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

/*

    private static List<AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, String>>> operationList = new ArrayList<>();

    static {
        operationList.add(new AbstractMap.SimpleEntry<>("spawnerName", (dobj, input) -> ((DungeonMobSpawner) dobj).spawner = input));
    }

    private transient int operationIndex = 0;

    public AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, String>> getOperation() {
        if (operationIndex >= operationList.size()) {
            return null;
        }
        return operationList.get(operationIndex++);
    }


 */