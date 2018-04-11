package me.blackphreak.dynamicdungeon.dungeonobject.trigger;

import lombok.Data;
import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import me.blackphreak.dynamicdungeon.dungeonobject.OffsetLocation;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

@Data
public class LocationTrigger extends DungeonTrigger {
    @DDField(name = "§a+- §eLocation")
    private OffsetLocation location;
    
    @DDField(name = "§a+- §eRange")
    private double range;
    
    @Override
    public boolean condition(DungeonSession dg, Event e) {
        PlayerMoveEvent moveEvent = (PlayerMoveEvent) e;
        Location loc = location.add(dg.getDgMinPt()).toBukkitLoc();
        db.log("loc: " + loc.toString());
        db.log("d: " + moveEvent.getTo().distance(loc));
        return moveEvent.getTo().distance(loc) <= range;
    }
    
    @Override
    public String toString()
    {
        return String.format("[Tri-Location] Range: %.2f", range);
    }
}
