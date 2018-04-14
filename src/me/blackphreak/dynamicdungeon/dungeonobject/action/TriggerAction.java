package me.blackphreak.dynamicdungeon.dungeonobject.action;

import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import me.blackphreak.dynamicdungeon.dungeonobject.OffsetLocation;

public class TriggerAction extends DungeonAction {
	@DDField(name = "Target Trigger Name")
	private String targetTriggerName;
	
	@Override
	public void action(DungeonSession dg, OffsetLocation location) {
		dg.fireTheTrigger(targetTriggerName);
	}
	
	@Override
	public String toString()
	{
		return String.format("[Ac-Trigger] TriggerName: %s", targetTriggerName);
	}
}
