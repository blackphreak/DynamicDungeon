package com.caxerx.mc.dynamicdungeon.dungeonobject.trigger;

import com.caxerx.mc.dynamicdungeon.dungeonobject.DDField;

public class MobKillTrigger extends DungeonTrigger{
    @DDField(name = "Mob Name")
    private String mobName; // the name of mob
    @DDField(name = "Amount")
    private int amount;
}
