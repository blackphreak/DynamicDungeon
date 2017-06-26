package me.blackphreak.dynamicdungeon.MapBuilding.Hub;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.blackphreak.dynamicdungeon.math;
import me.blackphreak.dynamicdungeon.Messages.db;
import org.bukkit.Chunk;

public class MythicMobAPI {
	public MythicMobAPI() {
	}
	
	public static Collection<ActiveMob> spawnOnChunk(DungeonSession session, Chunk targetChunk) {
		if(session.getSpawnersByChunk(targetChunk).size() <= 0) {
			db.log("No spawner on that chunk, no mobs have been spawned.");
			db.log("Exiting static function: Collection<ActiveMob> spawn(...)");
			return new ArrayList<>();
		} else {
			List<ActiveMob> mobs = new ArrayList<>();
			session.getSpawnersByChunk(targetChunk).forEach((spawner) -> {
				AbstractLocation location = spawner.getLocation().add(0.5D, 1.0D, 0.5D);
				Collection<ActiveMob> spawned = spawner.Spawn();
				mobs.addAll(spawned);
				db.log("ActiveMob spawned in Location(" + math.round(location.getX()) + ", " + math.round(location.getY()) + ", " + math.round(location.getZ()) + "). CurrentSpawned: " + spawned.size() + " | TotalSpawned: " + mobs.size());
			});
			return mobs;
		}
	}
}
