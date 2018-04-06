package me.blackphreak.dynamicdungeon.Objects;

import me.blackphreak.dynamicdungeon.MapBuilding.Editor.DungeonEditSession;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class DungeonSchematicDecorate extends LocationObject {

    public DungeonSchematicDecorate(int x, int y, int z) {
        // this x, y, z is the centre point of schematic!!
        super("schemdec", x, y, z);
    }

    private String schematicName;

    private static List<AbstractMap.SimpleEntry<String, TriConsumer<DungeonEditSession, DungeonObject, Object>>> operationList = new ArrayList<>();

    static {
        operationList.add(new AbstractMap.SimpleEntry<>("Schematic Name (Without file extension)", (es, dobj, input) -> ((DungeonSchematicDecorate) dobj).schematicName = (String) input));
    }

    private transient int operationIndex = 0;

    public AbstractMap.SimpleEntry<String, TriConsumer<DungeonEditSession, DungeonObject, Object>> getOperation() {
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
