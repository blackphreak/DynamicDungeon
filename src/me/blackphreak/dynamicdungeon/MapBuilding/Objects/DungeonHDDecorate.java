package me.blackphreak.dynamicdungeon.MapBuilding.Objects;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class DungeonHDDecorate extends DungeonObject {

    public DungeonHDDecorate(int x, int y, int z) {
        super("hddec", x, y, z);
    }

    private String holoName;
    private double offset;

    private static List<AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>>> operationList = new ArrayList<>();

    static {
        operationList.add(new AbstractMap.SimpleEntry<>("Hologram Name", (dobj, input) -> ((DungeonHDDecorate) dobj).holoName = (String) input));
        operationList.add(new AbstractMap.SimpleEntry<>("Height Offset [Double]", (dobj, input) -> ((DungeonHDDecorate) dobj).offset = Double.parseDouble((String) input)));
    }
    
    private transient int operationIndex = 0;

    public AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>> getOperation() {
        if (operationIndex < operationList.size()) {
            return operationList.get(operationIndex++);
        }
        return null;
    }

    public String getHoloName() {
        return holoName;
    }

    public double getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        String r = "DungeonHDDecorate:\n" + super.toString();
        r += "Hologram: " + holoName + "\n";
        r += "Height Offset: " + offset + "\n";
        return r;
    }

}
