package me.blackphreak.dynamicdungeon.Objects.Actions;

import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.Objects.DungeonObject;
import me.blackphreak.dynamicdungeon.Objects.Triggers.DungeonTrigger;

public abstract class TriggerAction extends DungeonObject {
	private String actionType;
	private transient DungeonTrigger dt;
	
	public TriggerAction(String type, DungeonTrigger parent) {
		super(type, "");
		this.actionType = type;
		this.dt = parent;
	}
	
	public abstract boolean action(DungeonSession dg);
	
	public DungeonTrigger getParent() {
		return dt;
	}
}
