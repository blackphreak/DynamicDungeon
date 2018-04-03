package me.blackphreak.dynamicdungeon.MapBuilding.Objects.Trigger;

import me.blackphreak.dynamicdungeon.MapBuilding.Objects.DungeonObject;

import java.util.ArrayList;
import java.util.List;

public abstract class DungeonTrigger extends DungeonObject
{
	private String name;
	private String type;
	private long delay;
	private int repeat;
	private List<TriggerAction> actions = new ArrayList<>();
	
	public DungeonTrigger(String name, String type, long delay, int repeat, List<TriggerAction> actionList)
	{
		super(type, name, 0, -1, 0);
		this.name = name;
		this.delay = delay;
		this.repeat = repeat;
		this.type = type;
		this.actions = actionList;
	}
	
	public void fire()
	{
		for (TriggerAction action : actions)
			action.action();
	}
	
	public abstract void action();
	
	// getters & setters
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getDelay() {
		return delay;
	}
	
	public void setDelay(long delay) {
		this.delay = delay;
	}
	
	public int getRepeat() {
		return repeat;
	}
	
	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public List<TriggerAction> getActions() {
		return actions;
	}
	
	public void setActions(List<TriggerAction> actions) {
		this.actions = actions;
	}
}
