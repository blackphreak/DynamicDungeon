package com.caxerx.mc.dynamicdungeon.dungeonobject;

import com.sk89q.worldedit.Vector;
import lombok.Data;
import me.blackphreak.dynamicdungeon.Messages.db;
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

    public static DungeonLocation createFromBukkitLocation(Location loc) {
        return new DungeonLocation(loc.getX(), loc.getY(), loc.getZ());
    }

    public static DungeonLocation createFromWorldEditVector(Vector loc) {
        return new DungeonLocation(loc.getX(), loc.getY(), loc.getZ());
    }

    public DungeonLocation subtract(DungeonLocation loc) {
        this.x -= loc.getX();
        this.y -= loc.getY();
        this.z -= loc.getZ();
        return this;
    }

    public DungeonLocation subtract(double x,double y,double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public DungeonLocation add(double x,double y,double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public DungeonLocation add(DungeonLocation loc) {
        this.x += loc.getX();
        this.y += loc.getY();
        this.z += loc.getZ();
        return this;
    }

    @Override
    public String toString() {
        return x + "," + y + "," + z;
    }
}
