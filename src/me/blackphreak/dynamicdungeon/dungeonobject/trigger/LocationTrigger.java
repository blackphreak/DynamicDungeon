package me.blackphreak.dynamicdungeon.dungeonobject.trigger;

import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import me.blackphreak.dynamicdungeon.dungeonobject.DungeonLocation;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

public class LocationTrigger extends DungeonTrigger {
    @DDField(name = "§a+- §eLocation")
    private DungeonLocation location;
    
    @DDField(name = "§a+- §eRange")
    private double range;
    
    @Override
    public boolean condition(DungeonSession dg, Event e) {
        PlayerMoveEvent moveEvent = (PlayerMoveEvent) e;
        return moveEvent.getTo().distance(location.add(dg.getDgMinPt()).toBukkitLoc()) <= range;
    }
    
    @Override
    public String toString()
    {
        return String.format("[Tri-Location] Range: %.2f", range);
    }
}
