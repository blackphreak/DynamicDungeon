package me.blackphreak.dynamicdungeon.dungeonobject;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Data
public class GlobalLocation extends DungeonLocation{
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
    
    public Location toBukkitLoc()
    {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }
    
    public static GlobalLocation fromBukkitLoc(Location loc)
    {
        return new GlobalLocation(loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ());
    }

    public static GlobalLocation createFromString(String location) {
        String[] locationString = location.split(",");
        return new GlobalLocation(locationString[0], Double.parseDouble(locationString[1]), Double.parseDouble(locationString[2]), Double.parseDouble(locationString[3]));
    }
}
