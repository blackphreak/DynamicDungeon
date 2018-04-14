package me.blackphreak.dynamicdungeon.dungeonobject.trigger;

import lombok.Data;
import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

@Data
public class InteractTrigger extends DungeonTrigger {
	@DDField(name = "Range")
	private double range;
	
	@Override
	public boolean condition(DungeonSession dg, Event e) {
		PlayerInteractEvent interactEvent = (PlayerInteractEvent) e;
		return (interactEvent.getClickedBlock().getLocation().distance(getLocation().add(dg.getDgMinPt()).toBukkitLoc()) <= range);
	}
	
	@Override
	public String toString() {
		return String.format("[Tri-Interact] Range: %.2f", range);
	}
}
