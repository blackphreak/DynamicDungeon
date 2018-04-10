package me.blackphreak.dynamicdungeon.dungeonobject.action;

import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.Supports.HolographicDisplays.cHologram;
import me.blackphreak.dynamicdungeon.Supports.HolographicDisplays.cHologramManager;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;

public class HologramAction extends LocationActionObject {
	@DDField(name = "Holo Name")
	private String holoName;
	
	@DDField(name = "Offset Y axis")
	private double offsetY;
	
	@Override
	public void action(DungeonSession dg) {
		cHologram chg = cHologramManager.getOrRegister(holoName).clone();
		dg.addHologram(chg);
		chg.teleport(getLocation().add(dg.getDgMinPt()).add(0, offsetY, 0).toBukkitLoc());
	}
}
