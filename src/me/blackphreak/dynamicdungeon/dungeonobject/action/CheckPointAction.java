package me.blackphreak.dynamicdungeon.dungeonobject.action;

import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import me.blackphreak.dynamicdungeon.dungeonobject.OffsetLocation;

public class CheckPointAction extends DungeonAction {
    @DDField(name = "Location")
    private OffsetLocation location;
    
    @Override
    public void action(DungeonSession dg, OffsetLocation location) {
        dg.updateCheckPoint(this.location.add(dg.getDgMinPt()).toBukkitLoc());
    }
    
    @Override
    public String toString()
    {
        return String.format("[Ac-CheckPoint] loc: %s", location.toString());
    }
}
