package me.blackphreak.dynamicdungeon.MapBuilding.Objects;

import me.blackphreak.dynamicdungeon.gb;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class DungeonDecorate extends DungeonObject {
    private cLocation loc;
    
    public DungeonDecorate(int x, int y, int z) {
        super("decorate", x, y, z);
        loc = new cLocation(gb.dgWorldName, x, y, z);
    }
    
    private transient String type;
    private transient String name;
    private transient double offset;
    
    private static List<AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>>> operationList = new ArrayList<>();

    static {
        operationList.add(new AbstractMap.SimpleEntry<>("Type of decoration", (dobj, input) -> ((DungeonDecorate) dobj).type = (String) input));
        operationList.add(new AbstractMap.SimpleEntry<>("Decoration Name", (dobj, input) -> ((DungeonDecorate) dobj).name = (String) input));
        operationList.add(new AbstractMap.SimpleEntry<>("Offset Y axis", (dobj, input) -> ((DungeonDecorate) dobj).offset = Double.parseDouble((String) input)));
    }

    private transient int operationIndex = 0;
    
    public AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>> getOperation() {
        if (operationIndex < operationList.size()) {
            return operationList.get(operationIndex++);
        }
        return null;
    }
}
