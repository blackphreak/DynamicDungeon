package me.blackphreak.dynamicdungeon.dungeonobject.action;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitWorld;
import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.dungeonobject.ActionNeeded;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import org.bukkit.Location;

public class SpawnMobAction extends DungeonAction {
	@DDField(name = "MythicMob Name")
	private String mobName;
	
	@DDField(name = "Amount to spawn per action")
	private int amount;
	
	@Override
	public void action(DungeonSession dg, ActionNeeded needed) {
		Location loc = needed.getLocation().add(dg.getDgMinPt()).toBukkitLoc();
		for (int i = 0; i < amount; i++) {
			MythicMobs.inst().getMobManager()
				.getMythicMob(mobName)
				.spawn(
						new AbstractLocation(
								new BukkitWorld(loc.getWorld()),
								loc.getX(),
								loc.getY(),
								loc.getZ()
						),
						1
				);
		}
	}
}
