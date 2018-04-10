package me.blackphreak.dynamicdungeon.dungeonobject.action;

import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;

public class MessageAction extends LocationActionObject {
	@DDField(name = "  §a+- §eRadius")
	private double radius;
	
	@DDField(name = "  §a+- §eMessage")
	private String message;
	
	@Override
	public void action(DungeonSession dg) {
		if (radius == -1)
			dg.getWhoPlaying().forEach(p -> p.sendMessage(message));
		else
			dg.getWhoPlaying().forEach(p -> {
				if (p.getLocation().distance(getLocation().add(dg.getDgMinPt()).toBukkitLoc()) <= radius)
					p.sendMessage(message);
			});
	}
	
	@Override
	public String toString()
	{
		return String.format("[Ac-Message] Radius: %.2f | Msg: %s", radius, message);
	}
}
