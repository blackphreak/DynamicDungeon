package me.blackphreak.dynamicdungeon.MapBuilding.Objects;

public class cLocation {
    private String world;
    private int x;
    private int y;
    private int z;

    public cLocation(String world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public String getWorld() {
        return world;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
