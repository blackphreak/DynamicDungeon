package me.blackphreak.dynamicdungeon.MapBuilding.Objects.Triggers;

import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.MapBuilding.Objects.DungeonObject;
import me.blackphreak.dynamicdungeon.MapBuilding.Objects.cLocation;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.gb;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class InteractTrigger extends DungeonTrigger {
    private cLocation loc; // offset location
    private String material;
    private String state;

    public InteractTrigger(int x, int y, int z) {
        super("int_trigger", x, y, z);
    }

    @Override
    public boolean condition(DungeonSession d, Event e) {
        db.log("Interact Trigger Condition Check");
        PlayerInteractEvent ievent = (PlayerInteractEvent) e;

        return ievent.getClickedBlock().getType().name().equals(material) && ievent.getClickedBlock().getState().toString().equals(state);
        /*&& ievent.getClickedBlock().getLocation().distance(null)<=offset*/
    }

    public void action(DungeonSession d) {
        db.log("Interact Trigger Action");
    }

    public void setLoc(cLocation loc) {
        this.loc = loc;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setState(String state) {
        this.state = state;
    }

    public cLocation getLoc() {
        return loc;
    }

    public String getMaterial() {
        return material;
    }

    public String getState() {
        return state;
    }

    private static List<AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>>> operationList = new ArrayList<>();

    static {
        operationList.add(new AbstractMap.SimpleEntry<>("Trigger Name [String]", (dobj, input) -> ((InteractTrigger) dobj).setTriggerName((String) input)));
        operationList.add(new AbstractMap.SimpleEntry<>("Trigger Delay [1000 = 1sec]", (dobj, input) -> ((InteractTrigger) dobj).setDelay(Long.parseLong((String) input))));
        operationList.add(new AbstractMap.SimpleEntry<>("Trigger Repeat [Integer]", (dobj, input) -> ((InteractTrigger) dobj).setRepeat(Integer.parseInt((String) input))));
        operationList.add(new AbstractMap.SimpleEntry<>("Click The Block!", (dobj, input) ->
        {
            if (input instanceof PlayerInteractEvent) {
                InteractTrigger obj = (InteractTrigger) dobj;
                obj.setMaterial(((PlayerInteractEvent) input).getClickedBlock().getType().name());
                obj.setState(((PlayerInteractEvent) input).getClickedBlock().getState().toString());
                obj.setLoc(new cLocation(
                        gb.dgWorldName,
                        ((PlayerInteractEvent) input).getClickedBlock().getX(),
                        ((PlayerInteractEvent) input).getClickedBlock().getY(),
                        ((PlayerInteractEvent) input).getClickedBlock().getZ()
                ));
            }
        }));
    }

    private transient int operationIndex = 0;

    @Override
    public AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>> getOperation() {
        if (operationIndex < operationList.size()) {
            return operationList.get(operationIndex++);
        }
        return null;
    }
}
