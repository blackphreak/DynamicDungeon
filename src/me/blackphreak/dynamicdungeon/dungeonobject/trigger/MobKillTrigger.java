package me.blackphreak.dynamicdungeon.dungeonobject.trigger;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import lombok.Getter;
import lombok.Setter;
import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import org.bukkit.event.Event;

public class MobKillTrigger extends DungeonTrigger {
	@DDField(name = "Mob Name")
	private String mobName; // the name of mob
	
	@DDField(name = "Amount")
	private int amount;
	
	@Getter
	@Setter
	private int killedAmount = 0;
	
	public void addKilledAmount() {
		killedAmount++;
	}
	
	@Override
	public boolean condition(DungeonSession dg, Event e) {
		MythicMobDeathEvent killEvent = (MythicMobDeathEvent) e;
		if (killEvent.getMobType().getInternalName().equals(mobName)) {
			addKilledAmount();
			if (getKilledAmount() >= amount) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return String.format("[Tri-MobKill] MobName: %s | Amount: %d", mobName, amount);
	}
}
