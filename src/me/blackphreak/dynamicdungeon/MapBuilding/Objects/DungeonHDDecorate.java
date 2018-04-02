package me.blackphreak.dynamicdungeon.MapBuilding.Objects;

import me.blackphreak.dynamicdungeon.gb;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class DungeonHDDecorate extends DungeonObject {

    public DungeonHDDecorate(int x, int y, int z) {
        super("hddecorate", x, y, z);
    }

    private String name;
    private double offset;

    private static List<AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>>> operationList = new ArrayList<>();

    static {
        operationList.add(new AbstractMap.SimpleEntry<>("HD Name", (dobj, input) -> ((DungeonHDDecorate) dobj).name = (String) input));
        operationList.add(new AbstractMap.SimpleEntry<>("Offset Y axis", (dobj, input) -> ((DungeonHDDecorate) dobj).offset = Double.parseDouble((String) input)));
    }

    private transient int operationIndex = 0;

    public AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>> getOperation() {
        if (operationIndex < operationList.size()) {
            return operationList.get(operationIndex++);
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public double getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        String r = "DungeonHDDeacorate:\n" + super.toString();
        r += "HDName: " + name + "\n";
        r += "Y Offset" + offset + "\n";
        return r;
    }

}
