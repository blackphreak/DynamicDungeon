package me.blackphreak.dynamicdungeon.Listeners;

import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.gb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveEventListener implements Listener
{
	public PlayerMoveEventListener() {
		db.log("Listening on PlayerMoveEvent");
	}
	
	@EventHandler
	public void callsWhenPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		
		if (p.getLocation().getWorld().getName()
				.equalsIgnoreCase(gb.dgWorldName)) {
			// player is in DungeonWorld
			// get the playing session
			DungeonSession dg = gb.getDungeonSessionByPlayer(p);
			if (dg != null) {
				// is playing
				dg.getLocationTriggers().forEach(v -> v.condition(e));
			}
		}
	}
}
