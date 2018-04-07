package com.caxerx.mc.dynamicdungeon.dungeonobject;

import lombok.Data;
import org.bukkit.Location;

@Data
public class DungeonLocation {
    private double x;
    private double y;
    private double z;

    public DungeonLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static DungeonLocation createFromMinPoint(Location minPoint, double x, double y, double z) {
        return new DungeonLocation(minPoint.getX() - x, minPoint.getY() - y, minPoint.getZ() - z);
    }

    public static DungeonLocation createToMinPoint(Location minPoint, double x, double y, double z) {
        return new DungeonLocation(minPoint.getX() + x, minPoint.getY() + y, minPoint.getZ() + z);
    }

    public static DungeonLocation createFromString(String location) {
        String[] locationString = location.split(",");
        return new DungeonLocation(Double.parseDouble(locationString[0]), Double.parseDouble(locationString[1]), Double.parseDouble(locationString[2]));
    }
}
