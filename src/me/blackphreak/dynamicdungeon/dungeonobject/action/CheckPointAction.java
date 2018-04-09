package me.blackphreak.dynamicdungeon.dungeonobject.action;

import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import me.blackphreak.dynamicdungeon.dungeonobject.DungeonLocation;

public class CheckPointAction extends DungeonAction {
    @DDField(name = "Location")
    private DungeonLocation location;
    
    @Override
    public void action(DungeonSession dg) {
        dg.updateCheckPoint(location.add(dg.getDgMinPt()).toBukkitLoc());
    }
}
