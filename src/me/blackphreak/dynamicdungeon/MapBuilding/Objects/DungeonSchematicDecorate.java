package me.blackphreak.dynamicdungeon.MapBuilding.Objects;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class DungeonSchematicDecorate extends DungeonObject {

    public DungeonSchematicDecorate(int x, int y, int z) {
        // this x, y, z is the centre point of schematic!!
        super("schemdec", x, y, z);
    }

    private String schematicName;

    private static List<AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>>> operationList = new ArrayList<>();

    static {
        operationList.add(new AbstractMap.SimpleEntry<>("Schematic Name (Without file extension)", (dobj, input) -> ((DungeonSchematicDecorate) dobj).schematicName = (String) input));
    }

    private transient int operationIndex = 0;

    public AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>> getOperation() {
        if (operationIndex < operationList.size()) {
            return operationList.get(operationIndex++);
        }
        return null;
    }

    public String getSchematicName() {
        return schematicName;
    }

    @Override
    public String toString() {
        String r = "DungeonSchematicDecorate:\n" + super.toString();
        r += "Schematic: " + schematicName + "\n";
        return r;
    }

}
