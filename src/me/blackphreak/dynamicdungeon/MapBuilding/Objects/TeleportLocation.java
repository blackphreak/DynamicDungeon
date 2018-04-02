package me.blackphreak.dynamicdungeon.MapBuilding.Objects;

public class TeleportLocation {
    private String world;
    private int x;
    private int y;
    private int z;

    public TeleportLocation(String world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
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
