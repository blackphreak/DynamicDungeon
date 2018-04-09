package me.blackphreak.dynamicdungeon.dungeonobject.trigger;

import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import me.blackphreak.dynamicdungeon.dungeonobject.DungeonLocation;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractTrigger extends DungeonTrigger{
    @DDField(name = "Location")
    private DungeonLocation location;
    @DDField(name = "Range")
    private int range;
    
    @Override
    public boolean condition(DungeonSession dg, Event e) {
        PlayerInteractEvent interactEvent = (PlayerInteractEvent) e;
        return (interactEvent.getClickedBlock().getLocation().distance(location.add(dg.getDgMinPt()).toBukkitLoc()) <= range);
    }
}
