package me.blackphreak.dynamicdungeon.Listeners;

import io.lumine.xikage.mythicmobs.spawning.spawners.MythicSpawner;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;
import me.blackphreak.dynamicdungeon.gb;
import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class ChunkLoadEventListener implements Listener {
	public ChunkLoadEventListener() {
	}
	
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent e) {
		if(e.getChunk() != null) {
			if(e.getChunk().getWorld().getName().equalsIgnoreCase("dungeonworld")) {
				DungeonSession session = gb.getDungeonSessionByPlayer((Player)Arrays.stream(e.getChunk().getEntities()).filter((v) -> v instanceof Player).toArray()[0]);
				if(session == null) {
					return;
				}
				
				session.getSpawnersByChunk(e.getChunk()).forEach(MythicSpawner::Spawn);
			}
			
		}
	}
}
