package me.blackphreak.dynamicdungeon.Listeners;

import org.bukkit.event.Listener;

public class ChunkLoadEventListener implements Listener {
	public ChunkLoadEventListener() {
	}
	
//	@EventHandler
//	public void onChunkLoad(ChunkLoadEvent e)
//	{
//		if(e.getChunk() != null) {
//			if(e.getChunk().getWorld().getName().equalsIgnoreCase("dungeonworld"))
//			{
////				DungeonSession session = gb.getDungeonSessionByPlayer(
////						(Player) (Arrays.stream(e.getChunk().getEntities())
////								.filter(v -> v instanceof Player)
////								.toArray()[0])
////				);
//
//				// identify the session by chunk.
//				DungeonSession session = DungeonSession.getSessionByChunk(e.getChunk());
//
//				if(session == null)
//				{
//					return;
//				}
//
//				db.tlog("Session found: " + session.getDungeonID());
//
////				session.getSpawnersByChunk(e.getChunk()).forEach(MythicSpawner::Spawn);
//				MythicMobAPI.spawnOnChunk(session, e.getChunk());
//			}
//
//		}
//	}
}
