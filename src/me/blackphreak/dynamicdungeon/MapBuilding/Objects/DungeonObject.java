package me.blackphreak.dynamicdungeon.MapBuilding.Objects;

import java.util.AbstractMap;
import java.util.function.BiConsumer;

public abstract class DungeonObject {
    private String type;
    private int x;
    private int y;
    private int z;

    public DungeonObject(String type, int x, int y, int z) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public abstract AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, String>> getOperation();

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
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

    @Override
    public String toString() {
        String r = "";
        r += "X:" + x + "\n";
        r += "Y:" + y + "\n";
        r += "Z:" + z + "\n";
        return r;
    }
}
