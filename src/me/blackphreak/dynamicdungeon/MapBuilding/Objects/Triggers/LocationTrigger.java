package me.blackphreak.dynamicdungeon.MapBuilding.Objects.Triggers;

import me.blackphreak.dynamicdungeon.MapBuilding.Objects.DungeonObject;
import me.blackphreak.dynamicdungeon.MapBuilding.Objects.cLocation;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.gb;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class LocationTrigger extends DungeonTrigger
{
	private cLocation loc;
	private int range;
	
	public LocationTrigger() {
		super(null, "loctri", 0, 0);
	}
	
	public LocationTrigger(String name, long delay, int repeat) {
		super(name, "loctri", delay, repeat);
	}
	
	@Override
	public void condition(PlayerMoveEvent e) {
	
	}
	
	public void action()
	{
		db.log("action.");
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
		operationList.add(new AbstractMap.SimpleEntry<>("Trigger Name [String]", (dobj, input) -> ((LocationTrigger) dobj).setTriggerName((String) input)));
		operationList.add(new AbstractMap.SimpleEntry<>("Trigger Delay [1000 = 1sec]", (dobj, input) -> ((LocationTrigger) dobj).setDelay(Long.parseLong((String) input))));
		operationList.add(new AbstractMap.SimpleEntry<>("Trigger Repeat [Integer]", (dobj, input) -> ((LocationTrigger) dobj).setRepeat(Integer.parseInt((String) input))));
		operationList.add(new AbstractMap.SimpleEntry<>("Range [Integer]", (dobj, input) -> ((LocationTrigger) dobj).setRange(Integer.parseInt((String) input))));
		operationList.add(new AbstractMap.SimpleEntry<>("Location [Type \"ok\" when you are there]", (dobj, input) ->
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
