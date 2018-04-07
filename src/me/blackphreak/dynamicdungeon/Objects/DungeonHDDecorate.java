package me.blackphreak.dynamicdungeon.Objects;

import me.blackphreak.dynamicdungeon.MapBuilding.Editor.DungeonEditSession;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class DungeonHDDecorate extends LocationObject {

    public DungeonHDDecorate(int x, int y, int z) {
        super("hddec", x, y, z);
    }

    private String holoName;
    private double offset;

    private static List<AbstractMap.SimpleEntry<String, TriConsumer<DungeonEditSession, DungeonObject, Object>>> operationList = new ArrayList<>();

    static {
        operationList.add(new AbstractMap.SimpleEntry<>("Hologram Name", (es, dobj, input) -> ((DungeonHDDecorate) dobj).holoName = (String) input));
        operationList.add(new AbstractMap.SimpleEntry<>("Height Offset [Double]", (es, dobj, input) -> ((DungeonHDDecorate) dobj).offset = Double.parseDouble((String) input)));
    }
    
    private transient int operationIndex = 0;

    public AbstractMap.SimpleEntry<String, TriConsumer<DungeonEditSession, DungeonObject, Object>> getOperation() {
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
