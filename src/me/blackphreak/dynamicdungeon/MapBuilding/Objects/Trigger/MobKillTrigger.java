package me.blackphreak.dynamicdungeon.MapBuilding.Objects.Trigger;

import me.blackphreak.dynamicdungeon.MapBuilding.Objects.DungeonObject;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class MobKillTrigger extends DungeonTrigger
{
	private String triName; // the name of this trigger
	private String mobName; // the name of this trigger
	private int amount;
	
	public MobKillTrigger(String name,
	                      long delay,
	                      int repeat,
	                      List<TriggerAction> actionList) {
		super(name, "mktri", delay, repeat, actionList);
	}
	
	private static List<AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>>> operationList = new ArrayList<>();
	
	static {
		operationList.add(new AbstractMap.SimpleEntry<>("Mob Name", (dobj, input) -> ((MobKillTrigger) dobj).mobName = (String) input));
		operationList.add(new AbstractMap.SimpleEntry<>("Amount", (dobj, input) -> ((MobKillTrigger) dobj).amount = Integer.parseInt((String) input)));
	}
	
	private transient int operationIndex = 0;
	
	public AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>> getOperation() {
		if (operationIndex < operationList.size())
		{
			return operationList.get(operationIndex++);
		}
		return null;
	}
	
	public String getTriName() {
		return triName;
	}
	
	public void setTriName(String triName) {
		this.triName = triName;
	}
	
	public String getMobName() {
		return mobName;
	}
	
	public void setMobName(String mobName) {
		this.mobName = mobName;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public void action()
	{
	
	}
}
