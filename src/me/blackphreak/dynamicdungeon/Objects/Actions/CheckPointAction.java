package me.blackphreak.dynamicdungeon.Objects.Actions;

import me.blackphreak.dynamicdungeon.MapBuilding.Editor.DungeonEditSession;
import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.Objects.DungeonObject;
import me.blackphreak.dynamicdungeon.Objects.Triggers.DungeonTrigger;
import me.blackphreak.dynamicdungeon.gb;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.Location;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class CheckPointAction extends TriggerAction {
	private int x;
	private int y;
	private int z;
	
	public CheckPointAction(DungeonTrigger dt) {
		super("cp_action", dt);
	}
	
	@Override
	public boolean action(DungeonSession dg) {
		dg.updateCheckPoint(new Location(gb.dgWorld, dg.getSession().getMinimumPoint().getBlockX() + x, dg.getSession().getMinimumPoint().getBlockY() + y, dg.getSession().getMinimumPoint().getBlockZ() + z));
		return true;
	}
	
	private transient List<AbstractMap.SimpleEntry<String, TriConsumer<DungeonEditSession, DungeonObject, Object>>> operationList = new ArrayList<>();
	
	{
		operationList.add(new AbstractMap.SimpleEntry<>("Location [Type \"ok\" when you are there]", (es, dobj, input) ->
		{
			CheckPointAction obj = (CheckPointAction) dobj;
			if (input instanceof Location) {
				obj.setX(((Location) input).getBlockX() - es.minPoint.getBlockX());
				obj.setY(((Location) input).getBlockY() - es.minPoint.getBlockY());
				obj.setZ(((Location) input).getBlockZ() - es.minPoint.getBlockZ());
			}
			
			getParent().addAction(obj);
			es.updateLastEdit(getParent());
		}));
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getZ() {
		return z;
	}
	
	public void setZ(int z) {
		this.z = z;
	}
	
	private transient int operationIndex = 0;
	
	@Override
	public AbstractMap.SimpleEntry<String, TriConsumer<DungeonEditSession, DungeonObject, Object>> getOperation() {
		if (operationIndex < operationList.size()) {
			return operationList.get(operationIndex++);
		}
		return null;
	}
	
	@Override
	public String toString()
	{
		return "{\"type\": \"cp_action\", \"x\": "+x+", \"y\": "+y+", \"z\": "+z+"}";
	}
}
