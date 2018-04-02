package me.blackphreak.dynamicdungeon.MapBuilding.Objects;


import org.bukkit.Location;

import java.util.AbstractMap;
import java.util.function.BiConsumer;

public class DungeonExit extends DungeonObject {
    private cLocation _loc; //target location

    public DungeonExit(int x, int y, int z) {
        super("exit", x, y, z);
    }

//    private transient String world;
//    private transient int x;
//    private transient int y;
//    private transient int z;
//
//    private static List<AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, String>>> operationList = new ArrayList<>();
//
//    static {
//        operationList.add(new AbstractMap.SimpleEntry<>("teleportWorld", (dobj, input) -> ((DungeonExit) dobj).world = input));
//        operationList.add(new AbstractMap.SimpleEntry<>("teleportX", (dobj, input) -> ((DungeonExit) dobj).x = Integer.parseInt(input)));
//        operationList.add(new AbstractMap.SimpleEntry<>("teleportY", (dobj, input) -> ((DungeonExit) dobj).y = Integer.parseInt(input)));
//        operationList.add(new AbstractMap.SimpleEntry<>("teleportZ", (dobj, input) -> {
//            DungeonExit dexit = ((DungeonExit) dobj);
//            dexit.z = Integer.parseInt(input);
//            dexit._loc = new cLocation(dexit.world, dexit.x, dexit.y, dexit.z);
//        }));
//
//    }

//    private transient int operationIndex = 0;

    public AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>> getOperation() {
        // empty input key name, since there is no input required.
        return new AbstractMap.SimpleEntry<>("", (dobj, input) -> {
            if (input instanceof Location)
            {
                DungeonExit obj = (DungeonExit) dobj;
                obj.setLoc(
                        new cLocation(
                            ((Location) input).getWorld().getName(),
                            ((Location) input).getBlockX(),
                            ((Location) input).getBlockY(),
                            ((Location) input).getBlockZ()
                        )
                );
            }
        });
//        if (operationIndex >= operationList.size()) {
//            return null;
//        }
//        return operationList.get(operationIndex++);
//        return null;
    }

    public void setLoc(cLocation location) {
        this._loc = location;
    }

    @Override
    public String toString() {
        String r = "DungeonExit:\n" + super.toString();
        r += "Teleport To: \n";
        r += "World:" + _loc.getWorld() + " \n";
        r += "X:" + _loc.getX() + " \n";
        r += "Y:" + _loc.getY() + " \n";
        r += "Z:" + _loc.getZ() + " \n";
        return r;
    }
}
