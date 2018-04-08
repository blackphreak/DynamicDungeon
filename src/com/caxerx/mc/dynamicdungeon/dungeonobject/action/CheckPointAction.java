package com.caxerx.mc.dynamicdungeon.dungeonobject.action;

import com.caxerx.mc.dynamicdungeon.dungeonobject.DDField;
import com.caxerx.mc.dynamicdungeon.dungeonobject.DungeonLocation;

public class CheckPointAction extends DungeonAction {
    @DDField(name = "Location")
    private DungeonLocation location;

    @Override
    void action() {

    }
}
