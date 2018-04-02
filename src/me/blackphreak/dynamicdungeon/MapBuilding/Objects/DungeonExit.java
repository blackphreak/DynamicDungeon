package me.blackphreak.dynamicdungeon.MapBuilding.Objects;


import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class DungeonExit extends DungeonObject {
    private TeleportLocation loc;

    public DungeonExit(int x, int y, int z) {
        super("exit", x, y, z);
    }


    transient String world;
    transient int x;
    transient int y;
    transient int z;

    private static List<AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, String>>> operationList = new ArrayList<>();

    static {
        operationList.add(new AbstractMap.SimpleEntry<>("teleportWorld", (dobj, input) -> ((DungeonExit) dobj).world = input));
        operationList.add(new AbstractMap.SimpleEntry<>("teleportX", (dobj, input) -> ((DungeonExit) dobj).x = Integer.parseInt(input)));
        operationList.add(new AbstractMap.SimpleEntry<>("teleportY", (dobj, input) -> ((DungeonExit) dobj).y = Integer.parseInt(input)));
        operationList.add(new AbstractMap.SimpleEntry<>("teleportZ", (dobj, input) -> {
            DungeonExit dexit = ((DungeonExit) dobj);
            dexit.z = Integer.parseInt(input);
            dexit.loc = new TeleportLocation(dexit.world, dexit.x, dexit.y, dexit.z);
        }));

    }

    private transient int operationIndex = 0;

    public AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, String>> getOperation() {
        if (operationIndex >= operationList.size()) {
            return null;
        }
        return operationList.get(operationIndex++);
    }

    public void setLoc(TeleportLocation loc) {
        this.loc = loc;
    }

    @Override
    public String toString() {
        String r = "DungeonExit:\n" + super.toString();
        r += "Teleport To: \n";
        r += "World:" + world + " \n";
        r += "X:" + x + " \n";
        r += "Y:" + y + " \n";
        r += "Z:" + z + " \n";
        return r;
    }
}
