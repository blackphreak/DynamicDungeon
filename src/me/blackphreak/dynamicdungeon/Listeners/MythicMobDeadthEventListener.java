package me.blackphreak.dynamicdungeon.Listeners;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.dungeonobject.trigger.DungeonTrigger;
import me.blackphreak.dynamicdungeon.dungeonobject.trigger.MobKillTrigger;
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
                    dg.getTriggers().stream()
                            .filter(t -> t instanceof MobKillTrigger)
                            .filter(DungeonTrigger::isActivated)
                            .filter(DungeonTrigger::isActionMade)
                            .filter(t -> t.condition(dg, e))
                            .forEach(t -> t.action(dg, t.getLocation())
                    );
                    dg.removeTriggersInQueue();
                }
            }
        }
    }
}
