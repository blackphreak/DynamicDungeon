package me.blackphreak.dynamicdungeon.mapcreator;

import com.google.gson.Gson;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by caxerx on 2017/6/30.
 */
public class DungeonSign {

    private final HashMap<String, String> extraData = new HashMap<>();
    private final Vector location;


    public DungeonSign(Location loc) {
        this.location = new Vector(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public DungeonSign(Location loc, String... extradatapair) {
        this.location = new Vector(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        new ArrayList<String>(Arrays.asList(extradatapair)).forEach(item -> {
            String[] pair = item.split(":");
            extraData.put(pair[0], pair[1]);
        });
    }

    public Vector getLocation() {
        return location;
    }

    public String getData(String data) {
        return extraData.get(data);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
