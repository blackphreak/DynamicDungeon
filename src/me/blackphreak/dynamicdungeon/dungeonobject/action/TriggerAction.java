package me.blackphreak.dynamicdungeon.dungeonobject.action;

import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;

public class TriggerAction extends DungeonAction {
	@DDField(name = "Target Trigger Name")
	private String targetTriggerName;
	
	@Override
	public void action(DungeonSession dg) {
		dg.fireTheTrigger(targetTriggerName);
	}
	
	@Override
	public String toString()
	{
		return String.format("[Ac-Trigger] TriggerName: %s", targetTriggerName);
	}
}
