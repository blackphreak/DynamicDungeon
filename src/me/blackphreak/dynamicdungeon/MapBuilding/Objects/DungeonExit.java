package me.blackphreak.dynamicdungeon.MapBuilding.Objects;


import org.bukkit.Location;

import java.util.AbstractMap;
import java.util.function.BiConsumer;

public class DungeonExit extends DungeonObject {
    private cLocation loc; //target location

    public DungeonExit(int x, int y, int z) {
        super("exit", x, y, z);
    }


    private boolean received = false;
    private static AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>> operation = new AbstractMap.SimpleEntry<>("Exit Location", (dobj, input) -> {
        if (input instanceof Location) {
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

    public AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>> getOperation() {
        if (received) {
            return null;
        }
        received = true;
        return operation;
    }

    public void setLoc(cLocation location) {
        this.loc = location;
    }

    @Override
    public String toString() {
        String r = "DungeonExit:\n" + super.toString();
        r += "Teleport To: \n";
        r += "World:" + loc.getWorld() + " \n";
        r += "X:" + loc.getX() + " \n";
        r += "Y:" + loc.getY() + " \n";
        r += "Z:" + loc.getZ() + " \n";
        return r;
    }
}
