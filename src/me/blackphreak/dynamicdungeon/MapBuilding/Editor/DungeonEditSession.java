package me.blackphreak.dynamicdungeon.MapBuilding.Editor;

import com.google.gson.Gson;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.Region;
import me.blackphreak.dynamicdungeon.MapBuilding.Objects.*;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.gb;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class DungeonEditSession {
    private List<DungeonObject> dungeonObjectList = new ArrayList<>();
    private DungeonObject lastEdit;
    private AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>> valueOperation;
    private String dungeonName;
    private Region region;
    private final Vector minPoint;

    public DungeonEditSession(String dungeonName, Region region) {
        this.dungeonName = dungeonName;
        this.region = region;
        this.minPoint = region.getMaximumPoint();
    }

    public void save() {
        try {
            FileUtils.writeStringToFile(new File(gb.dataPath + dungeonName + ".json"), new Gson().toJson(dungeonObjectList), Charset.defaultCharset());
        } catch (IOException e) {
            db.log("error on saving dungeon " + dungeonName + "'s objects");
            e.printStackTrace();
        }
    }

    public void createDungeonExit(int x, int y, int z) {
        x -= minPoint.getBlockX();
        y -= minPoint.getBlockY();
        z -= minPoint.getBlockZ();
        lastEdit = new DungeonExit(x, y, z);
        updateOperation();
    }

    public void createDungeonSpawn(int x, int y, int z) {
        x -= minPoint.getBlockX();
        y -= minPoint.getBlockY();
        z -= minPoint.getBlockZ();
        lastEdit = new DungeonSpawn(x, y, z);
        updateOperation();
    }

    public void createDungeonMob(int x, int y, int z) {
        x -= minPoint.getBlockX();
        y -= minPoint.getBlockY();
        z -= minPoint.getBlockZ();
        lastEdit = new DungeonMobSpawner(x, y, z);
        updateOperation();
    }
    
    public void createDungeonDecoration(int x, int y, int z) {
        x -= minPoint.getBlockX();
        y -= minPoint.getBlockY();
        z -= minPoint.getBlockZ();
        lastEdit = new DungeonDecorate(x, y, z);
        updateOperation();
    }


    public void updateOperation() {
        valueOperation = lastEdit.getOperation();
        if (valueOperation == null) {
            dungeonObjectList.add(lastEdit);
            lastEdit = null;
        }
    }


    public String getInputValue() {
        if (valueOperation == null) {
            return null;
        }
        return valueOperation.getKey();
    }

    public void inputValue(Object value) {
        try {
            valueOperation.getValue().accept(lastEdit, value);
            updateOperation();
        } catch (Exception e) {
            db.log("ERROR IN INPUT");
        }
    }
    
    public String getDungeonName() {
        return dungeonName;
    }
}
