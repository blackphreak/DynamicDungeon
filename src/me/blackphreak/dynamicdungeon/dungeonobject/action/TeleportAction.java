package me.blackphreak.dynamicdungeon.dungeonobject.action;

import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import me.blackphreak.dynamicdungeon.dungeonobject.GlobalLocation;
//TODO
public class TeleportAction extends DungeonAction
{
	@DDField(name = "Is Outside DungeonWorld")
	private boolean outsideDGworld;
	
	@DDField(name = "Location")
	private GlobalLocation location;
	
	@Override
	public void action(DungeonSession dg) {
	
	}
}
