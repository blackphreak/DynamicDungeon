package com.caxerx.mc.dynamicdungeon.dungeonobject.trigger;

import com.caxerx.mc.dynamicdungeon.dungeonobject.DDField;
import com.caxerx.mc.dynamicdungeon.dungeonobject.DungeonObject;
import lombok.Data;

@Data
public class DungeonTrigger extends DungeonObject {
    @DDField(name = "Trigger Name")
    private String triggerName;
}
