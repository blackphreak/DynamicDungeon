package me.blackphreak.dynamicdungeon.dungeonobject.trigger;

import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import org.bukkit.event.Event;

/**
 * only used for do a set of actions.
 * no condition / trigger point.
 * must be triggered by another trigger.
 */
public class PassiveTrigger extends DungeonTrigger {
	
	@Override
	public boolean condition(DungeonSession dg, Event e) {
		return false;
	}
	
	@Override
	public String toString()
	{
		return "[Tri-Passive] none";
	}
}
