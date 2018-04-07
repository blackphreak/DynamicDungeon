package me.blackphreak.dynamicdungeon.Objects;

import me.blackphreak.dynamicdungeon.MapBuilding.Editor.DungeonEditSession;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.AbstractMap;

public abstract class DungeonObject {
    private String type;
    private String name; // custom name of this object

    public DungeonObject(String type) {
        this.type = type;
        this.name = "";
    }
    
    public DungeonObject(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public abstract AbstractMap.SimpleEntry<String, TriConsumer<DungeonEditSession, DungeonObject, Object>> getOperation();
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }

    public String getType() {
        return type;
    }
    
    public String getName()
    {
        return name;
    }

    @Override
    public String toString() {
        String r = "";
        return r;
    }
}
