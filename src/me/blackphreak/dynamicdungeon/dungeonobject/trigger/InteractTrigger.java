package me.blackphreak.dynamicdungeon.dungeonobject.trigger;

import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import me.blackphreak.dynamicdungeon.dungeonobject.OffsetLocation;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractTrigger extends DungeonTrigger{
    @DDField(name = "Location")
    private OffsetLocation location;
    
    @DDField(name = "Range")
    private double range;
    
    @Override
    public boolean condition(DungeonSession dg, Event e) {
        PlayerInteractEvent interactEvent = (PlayerInteractEvent) e;
        return (interactEvent.getClickedBlock().getLocation().distance(location.add(dg.getDgMinPt()).toBukkitLoc()) <= range);
    }
    
    @Override
    public String toString()
    {
        return String.format("[Tri-Interact] Range: %.2f", range);
    }
}
