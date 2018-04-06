package me.blackphreak.dynamicdungeon.Objects.Triggers;

import me.blackphreak.dynamicdungeon.MapBuilding.Editor.DungeonEditSession;
import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.Objects.Actions.CheckPointAction;
import me.blackphreak.dynamicdungeon.Objects.DungeonObject;
import me.blackphreak.dynamicdungeon.gb;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class LocationTrigger extends DungeonTrigger {
	private int x;
	private int y;
	private int z;
	private int range;
	
	public LocationTrigger(int x, int y, int z) {
		super("loc_trigger");
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public boolean condition(DungeonSession d, Event e) {
		PlayerMoveEvent moveEvent = (PlayerMoveEvent) e;
		return moveEvent.getTo().distance(new Location(Bukkit.getWorld(gb.dgWorldName), x, y, z).add(d.getDungeonMin())) <= range;
	}
	
	public void setRange(int range) {
		this.range = range;
	}
	
	public int getRange() {
		return range;
	}
	
	
	private transient List<AbstractMap.SimpleEntry<String, TriConsumer<DungeonEditSession, DungeonObject, Object>>> operationList = new ArrayList<>();
	
	{
		operationList.add(new AbstractMap.SimpleEntry<>("Trigger Name [String]", (es, dobj, input) -> ((LocationTrigger) dobj).setTriggerName((String) input)));
		operationList.add(new AbstractMap.SimpleEntry<>("Trigger Delay [1000 = 1sec]", (es, dobj, input) -> ((LocationTrigger) dobj).setDelay(Long.parseLong((String) input))));
		operationList.add(new AbstractMap.SimpleEntry<>("Trigger Repeat [Integer]", (es, dobj, input) -> ((LocationTrigger) dobj).setRepeat(Integer.parseInt((String) input))));
		operationList.add(new AbstractMap.SimpleEntry<>("Range [Integer]", (es, dobj, input) -> ((LocationTrigger) dobj).setRange(Integer.parseInt((String) input))));
		operationList.add(new AbstractMap.SimpleEntry<>("Action [CheckPoint | CancelTrigger]", (es, dobj, input) -> {
			switch (((String) input).toLowerCase()) {
				case "checkpoint":
				case "ck":
				case "cp":
					es.updateLastEdit(new CheckPointAction((DungeonTrigger) dobj));
					break;
				case "canceltrigger":
				case "ct":
					
					break;
			}
		}));
		operationList.add(new AbstractMap.SimpleEntry<>("+ ?", (es, dobj, input) -> {
			if (!input.equals("no"))
				((LocationTrigger) dobj).setOperationIndex(((LocationTrigger) dobj).getOperationIndex() - 2);
		}));
	    /*
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
        */
	}
	
	private transient int operationIndex = 0;
	
	public int getOperationIndex() {
		return operationIndex;
	}
	
	public void setOperationIndex(int operationIndex) {
		this.operationIndex = operationIndex;
	}
	
	public AbstractMap.SimpleEntry<String, TriConsumer<DungeonEditSession, DungeonObject, Object>> getOperation() {
		if (operationIndex < operationList.size()) {
			return operationList.get(operationIndex++);
		}
		return null;
	}
}
