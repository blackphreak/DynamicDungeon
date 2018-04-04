package me.blackphreak.dynamicdungeon.Listeners;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.gb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MythicMobDeadthEventListener implements Listener {
	public MythicMobDeadthEventListener() {
		db.log("Listening on MythicMobDeathEvent");
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onMythicMobDeath(MythicMobDeathEvent e) {
		if (e.getKiller() instanceof Player) {
			Player p = (Player) e.getKiller();
			
			if (p.getLocation().getWorld().getName()
					.equalsIgnoreCase(gb.dgWorldName)) {
				// player is in DungeonWorld
				// get the playing session
				DungeonSession dg = gb.getDungeonSessionByPlayer(p);
				if (dg != null) {
					// is playing
					dg.getMobKillTriggers().forEach(v -> v.condition(e));
				}
			}
		}
	}
}
