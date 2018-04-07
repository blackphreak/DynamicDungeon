package com.caxerx.mc.dynamicdungeon.object;

import lombok.Data;
import org.bukkit.Location;

@Data
public class GlobalLocation {
    private String world;
    private double x;
    private double y;
    private double z;

    public GlobalLocation(String world, double x, double y, double z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static GlobalLocation createFromString(String location) {
        String[] locationString = location.split(",");
        return new GlobalLocation(locationString[0], Double.parseDouble(locationString[1]), Double.parseDouble(locationString[2]), Double.parseDouble(locationString[3]));
    }
}
