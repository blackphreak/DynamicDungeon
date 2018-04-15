package me.blackphreak.dynamicdungeon.dungeonobject.action;

import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.dungeonobject.ActionNeeded;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import me.blackphreak.dynamicdungeon.dungeonobject.GlobalLocation;
import org.bukkit.Location;

public class TeleportAction extends DungeonAction
{
	@DDField(name = "Range")
	private double range;
	
	@DDField(name = "Teleport to Location")
	private GlobalLocation targetLocation;
	
	//TODO: add to wiki
	
	@Override
	public void action(DungeonSession dg, ActionNeeded needed) {
		Location loc = needed.getLocation().add(dg.getDgMinPt()).toBukkitLoc();
		
		dg.getWhoPlaying().forEach(p -> {
			if (loc.distance(p.getLocation()) <= range)
				p.teleport(targetLocation.toBukkitLoc());
		});
	}
}
