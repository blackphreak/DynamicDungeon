package me.blackphreak.dynamicdungeon.Objects.Triggers;

import me.blackphreak.dynamicdungeon.MapBuilding.Editor.DungeonEditSession;
import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.Messages.msg;
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
	public boolean condition(DungeonSession dg, Event e) {
		PlayerMoveEvent moveEvent = (PlayerMoveEvent) e;
		return moveEvent.getTo().distance(new Location(Bukkit.getWorld(gb.dgWorldName), x, y, z).add(dg.getDungeonMin())) <= range;
	}
	
	public void setRange(int range) {
		this.range = range;
	}
	
	public int getRange() {
		return range;
	}
	
	
	private transient List<AbstractMap.SimpleEntry<String, TriConsumer<DungeonEditSession, DungeonObject, Object>>> operationList = new ArrayList<>();
	
	{
		operationList.add(new AbstractMap.SimpleEntry<>("Trigger Name &7[&eString&7]", (es, dobj, input) -> ((LocationTrigger) dobj).setTriggerName((String) input)));
		operationList.add(new AbstractMap.SimpleEntry<>("Trigger Delay &7[&e20 &7= &e1sec&7]", (es, dobj, input) -> ((LocationTrigger) dobj).setDelay(Long.parseLong((String) input))));
		operationList.add(new AbstractMap.SimpleEntry<>("Trigger Repeat &7[&eInteger&7]", (es, dobj, input) -> {
			((LocationTrigger) dobj).setRepeat(Integer.parseInt((String) input));
			
			if (((LocationTrigger) dobj).getRepeat() < 0)
			{
				((LocationTrigger) dobj).setOperationIndex(
						((LocationTrigger) dobj).getOperationIndex() + 1 // skipping "Trigger Period'
				);
				((LocationTrigger) dobj).setPeriod(0);
			}
		}));
		operationList.add(new AbstractMap.SimpleEntry<>("Trigger Period &7[&e20 &7= &e1sec&7]", (es, dobj, input) -> ((LocationTrigger) dobj).setPeriod(Long.parseLong((String) input))));
		operationList.add(new AbstractMap.SimpleEntry<>("Acceptable Trigger Range &7[&eInteger&7]", (es, dobj, input) -> ((LocationTrigger) dobj).setRange(Integer.parseInt((String) input))));
		operationList.add(new AbstractMap.SimpleEntry<>("Action &7[&eCheckPoint &7| &eCancelTrigger&7]", (es, dobj, input) -> {
			switch (((String) input).toLowerCase()) {
				case "checkpoint":
				case "ck":
				case "cp":
					es.updateLastEdit(new CheckPointAction((DungeonTrigger) dobj));
					msg.send(es.getPlayer(), "   &7[ &b"+es.getLastEdit().getType()+" Setup &7]");
					es.setPrefix("   ");
					break;
				case "canceltrigger":
				case "ct":
					
					break;
			}
		}));
		operationList.add(new AbstractMap.SimpleEntry<>("Want to add more action? &7[&eyes&7(&ey&7) &7| &eno&7(&en&7)]", (es, dobj, input) -> {
			es.setPrefix("");
			if (!input.equals("no")
					&& !input.equals("n"))
				((LocationTrigger) dobj).setOperationIndex(((LocationTrigger) dobj).getOperationIndex() - 2);
		}));
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
