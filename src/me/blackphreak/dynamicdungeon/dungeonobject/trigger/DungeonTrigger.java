package me.blackphreak.dynamicdungeon.dungeonobject.trigger;

import lombok.Data;
import lombok.Getter;
import me.blackphreak.dynamicdungeon.DynamicDungeon;
import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import me.blackphreak.dynamicdungeon.dungeonobject.DungeonObject;
import me.blackphreak.dynamicdungeon.dungeonobject.action.DungeonAction;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class DungeonTrigger extends DungeonObject {
	@DDField(name = "Trigger Name")
	private String triggerName;
	
	@DDField(name = "§a+- §eDelay")
	private long delay;
	
	/**
	 * int repeat:
	 * acceptable values: {int x: -1, 0, 1, ..., intMaxValue}
	 * when value is -1, means this action has no repeat limit.
	 */
	@DDField(name = "§a+- §eRepeat")
	private int repeat;
	
	@DDField(name = "§a+- §ePeriod")
	private long period;
	
	/**
	 * boolean isActivated:
	 * acceptable values: [true | false]
	 * indicates whether must trigger by others.
	 * true : must be triggered by others.
	 * false: always active.
	 * TODO: add to wiki & check passive
	 */
	@DDField(name = "§a+- §eIs Activated")
	private boolean isActivated;
	
	/**
	 * boolean isActionMade
	 * used to prevent job repeats on delayed task.
	 */
	@Getter
	private transient boolean isActionMade = true;
	private transient List<DungeonAction> actionList = new ArrayList<>();
	
	public void addAction(DungeonAction actionObj) {
		actionList.add(actionObj);
	}
	
	public void removeAction(DungeonAction actionObj) {
		actionList.remove(actionObj);
	}
	
	// condition for Interact Triggers
	public abstract boolean condition(DungeonSession dg, Event e);
	
	public void action(DungeonSession dg) {
		if (getRepeat() == 0 && getDelay() <= 0) {
			getActionList().forEach(v -> v.action(dg));
			dg.addToTriggerRemoveQueue(this);
			db.tlog("DungeonTrigger[" + triggerName + "] has been removed due to it reached the repeat limit.");
		} else {
			if (getDelay() < 0) // negative delay?? u know the future?? wtf??
				setDelay(Math.abs(getDelay()));
			
			if (getPeriod() < 0) // negative period?? really???
				setDelay(Math.abs(getPeriod()));
			
			isActionMade = false;
			
			DungeonTrigger trigger = this;
			new BukkitRunnable() {
				@Override
				public void run() {
					getActionList().forEach(v -> v.action(dg));
					
					if (getRepeat() > 0)
						setRepeat(getRepeat() - 1);
					else if (getRepeat() == 0) {
						dg.addToTriggerRemoveQueue(trigger);
						db.tlog("DungeonTrigger[" + triggerName + "] has been removed due to it reached the repeat limit.");
						cancel();
					}
					isActionMade = true;
				}
			}.runTaskTimer(DynamicDungeon.plugin, delay, period);
		}
	}
}
