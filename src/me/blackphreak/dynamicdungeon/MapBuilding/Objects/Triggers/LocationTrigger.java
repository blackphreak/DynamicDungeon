package me.blackphreak.dynamicdungeon.MapBuilding.Objects.Triggers;

import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.MapBuilding.Objects.DungeonObject;
import me.blackphreak.dynamicdungeon.MapBuilding.Objects.cLocation;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.gb;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class LocationTrigger extends DungeonTrigger {
    private cLocation loc;
    private int range;

    public LocationTrigger(int x, int y, int z) {
        super("loc_trigger", x, y, z);
    }

    @Override
    public boolean condition(DungeonSession d, Event e) {
        PlayerMoveEvent moveEvent = (PlayerMoveEvent) e;
        return moveEvent.getTo().distance(new Location(Bukkit.getWorld(gb.dgWorldName), super.getX(), super.getY(), super.getZ()).add(d.getDungeonMin())) <= range;
    }

    public void action(DungeonSession d) {
        db.log("action.");
    }

    public void setRange(int range) {
        this.range = range;
    }

    /*
    public void setLoc(cLocation loc) {
        this.loc = loc;
        bukkitLoc = new Location(Bukkit.getWorld(loc.getWorld()), loc.getX(), loc.getY(), loc.getZ());
    }

    public cLocation getLoc() {
        return loc;
    }
    */

    public int getRange() {
        return range;
    }


    private static List<AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>>> operationList = new ArrayList<>();

    static {
        operationList.add(new AbstractMap.SimpleEntry<>("Trigger Name [String]", (dobj, input) -> ((LocationTrigger) dobj).setTriggerName((String) input)));
        operationList.add(new AbstractMap.SimpleEntry<>("Trigger Delay [1000 = 1sec]", (dobj, input) -> ((LocationTrigger) dobj).setDelay(Long.parseLong((String) input))));
        operationList.add(new AbstractMap.SimpleEntry<>("Trigger Repeat [Integer]", (dobj, input) -> ((LocationTrigger) dobj).setRepeat(Integer.parseInt((String) input))));
        operationList.add(new AbstractMap.SimpleEntry<>("Range [Integer]", (dobj, input) -> ((LocationTrigger) dobj).setRange(Integer.parseInt((String) input))));
        /*
        operationList.add(new AbstractMap.SimpleEntry<>("Location [Type \"ok\" when you are there]", (dobj, input) ->
        {
            if (input instanceof Location) {
                LocationTrigger obj = (LocationTrigger) dobj;
                obj.setLoc(
                        new cLocation(
                                gb.dgWorldName,
                                ((Location) input).getBlockX(),
                                ((Location) input).getBlockY(),
                                ((Location) input).getBlockZ()
                        )
                );
            }
        }));
        */
    }

    private transient int operationIndex = 0;

    public AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>> getOperation() {
        if (operationIndex < operationList.size()) {
            return operationList.get(operationIndex++);
        }
        return null;
    }
}
