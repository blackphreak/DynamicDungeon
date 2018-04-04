package me.blackphreak.dynamicdungeon.MapBuilding.Objects.Triggers;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import me.blackphreak.dynamicdungeon.MapBuilding.Objects.DungeonObject;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.AbstractMap;
import java.util.function.BiConsumer;

public abstract class DungeonTrigger extends DungeonObject
{
	private String triggerName;
	private String triggerType;
	private long delay;
	private int repeat;
	
	public DungeonTrigger(String triggerName, String triggerType, long delay, int repeat)
	{
		super("trigger", triggerName, 0, -1, 0);
		this.triggerName = triggerName;
		this.delay = delay;
		this.repeat = repeat;
		this.triggerType = triggerType;
	}
	
	// condition for Interact Triggers
	public void condition(PlayerInteractEvent e) {}
	// condition for Location Triggers
	public void condition(PlayerMoveEvent e) {}
	// condition for Mob Kill Triggers
	public void condition(MythicMobDeathEvent e) {}
	
	public abstract void action();
	
	// getters & setters
	
	public String getTriggerName() {
		return triggerName;
	}
	
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}
	
	public long getDelay() {
		return delay;
	}
	
	//delay * 20L / 1000L ||| to long (20L = 1sec) || input: 1000 = 1sec.
	public void setDelay(long delay) {
		this.delay = delay;
	}
	
	public int getRepeat() {
		return repeat;
	}
	
	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}
	
	public String getTriggerType() {
		return triggerType;
	}
	
	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}
	
	@Override
	public AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>> getOperation() {
		return null;
	}
}
