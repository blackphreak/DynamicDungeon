package me.blackphreak.dynamicdungeon.MapBuilding.Objects.Trigger;

import me.blackphreak.dynamicdungeon.MapBuilding.Objects.DungeonObject;
import me.blackphreak.dynamicdungeon.MapBuilding.Objects.cLocation;
import me.blackphreak.dynamicdungeon.gb;
import org.bukkit.Location;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class LocationTrigger extends DungeonTrigger
{
	private cLocation loc;
	private int range;
	
	public LocationTrigger(String name,
	                       long delay,
	                       int repeat,
	                       List<TriggerAction> actionList) {
		super(name, "loctri", delay, repeat, actionList);
	}
	
	public void action()
	{
	
	}
	
	public void setRange(int range)
	{
		this.range = range;
	}
	
	public void setLoc(cLocation loc)
	{
		this.loc = loc;
	}
	
	public int getRange()
	{
		return range;
	}
	
	public cLocation getLoc()
	{
		return loc;
	}
	
	private static List<AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>>> operationList = new ArrayList<>();
	
	static {
		operationList.add(new AbstractMap.SimpleEntry<>("Range", (dobj, input) -> ((LocationTrigger) dobj).range = Integer.parseInt((String) input)));
		operationList.add(new AbstractMap.SimpleEntry<>("Location", (dobj, input) ->
		{
			if (input instanceof Location) {
				LocationTrigger obj = (LocationTrigger) dobj;
				obj.setLoc(
						new cLocation(
								gb.dgWorldName,
								((Location) input).getBlockX(),
								((Location) input).getBlockY(),
								((Location) input).getBlockZ()
						)
				);
			}
		}));
	}
	
	private transient int operationIndex = 0;
	
	public AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>> getOperation() {
		if (operationIndex < operationList.size()) {
			return operationList.get(operationIndex++);
		}
		return null;
	}
}
