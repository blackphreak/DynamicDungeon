package me.blackphreak.dynamicdungeon.dungeonobject.action;

import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import me.blackphreak.dynamicdungeon.dungeonobject.OffsetLocation;
import me.blackphreak.dynamicdungeon.dungeonobject.trigger.DungeonTrigger;

public class DeactivateTriggerAction extends DungeonAction {
	@DDField(name = "Target Trigger Name")
	private String targetTriggerName;
	
	@Override
	public void action(DungeonSession dg, OffsetLocation location) {
		DungeonTrigger dt = dg.getTriggerByName(targetTriggerName);
		if (dt != null)
			dt.setActivated(false);
	}
	
	@Override
	public String toString()
	{
		return String.format("[Ac-DeactivateTrigger] TriggerName: %s", targetTriggerName);
	}
}
