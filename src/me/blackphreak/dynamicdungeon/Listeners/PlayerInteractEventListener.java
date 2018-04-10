package me.blackphreak.dynamicdungeon.Listeners;

import me.blackphreak.dynamicdungeon.MapBuilding.BuilderV3;
import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.Messages.msg;
import me.blackphreak.dynamicdungeon.dungeonobject.trigger.InteractTrigger;
import me.blackphreak.dynamicdungeon.gb;
import me.blackphreak.dynamicdungeon.math;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerInteractEventListener implements Listener {
    public PlayerInteractEventListener() {
        db.log("Listening on PlayerInteractEvent");
    }

    @EventHandler
    public void callsWhenPlayerInteract(PlayerInteractEvent e) {
        if (e.getPlayer().getLocation().getWorld().getName()
                .equalsIgnoreCase(gb.dgWorldName)) {
            // player is in DungeonWorld
            // get the playing session
            DungeonSession dg = gb.getDungeonSessionByPlayer(e.getPlayer());
            if (dg != null) {
                // is playing
                dg.getTriggers().stream().filter(t -> t instanceof InteractTrigger).map(t -> (InteractTrigger) t).forEach(v -> {
                            if (v.condition(dg, e)) {
                                v.action(dg);
                            }
                        }
                );
                dg.removeTriggersInQueue();
            }

            if (e.getHand() == null) {
                Location tLoc = e.getPlayer().getLocation();
                tLoc.add(0.0D, -2.0D, 0.0D);
                if (tLoc.getBlock().getType().equals(Material.SIGN)
                        || tLoc.getBlock().getType().equals(Material.WALL_SIGN)) {
                    Sign sign = (Sign) tLoc.getBlock();
                    int tSessionID;
                    if (sign.getLine(0).equalsIgnoreCase("[dg_checkpoint]")
                            && (tSessionID = gb.dungeonPlaying.getOrDefault(e.getPlayer(), -1)) > -1) {
                        DungeonSession tSession = gb.dungeons.get(tSessionID);
                        tSession.updateCheckPoint(e.getPlayer().getLocation());
                        e.getClickedBlock().setType(Material.AIR);
                        tSession.getWhoPlaying().forEach((v) -> v.sendMessage("Check Point is set on: " + math.round(sign.getLocation().getX()) + ", " + math.round(sign.getLocation().getY()) + ", " + math.round(sign.getLocation().getZ())));
                    }
                }
            } else if (e.getHand().equals(EquipmentSlot.HAND)
                    && e.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                    &&
                    (e.getClickedBlock().getType().equals(Material.WALL_SIGN)
                            || e.getClickedBlock().getType().equals(Material.SIGN_POST)
                    )) {
                Sign sign = (Sign) e.getClickedBlock().getState();

                switch (sign.getLine(0).toLowerCase()) {
                    case "[dg_checkpoint]":
                        int tSessionID;
                        if ((tSessionID = gb.dungeonPlaying.getOrDefault(e.getPlayer(), -1)) > -1) {
                            DungeonSession tSession = gb.dungeons.get(tSessionID);
                            tSession.updateCheckPoint(e.getPlayer().getLocation());
                            e.getClickedBlock().setType(Material.AIR);
                            tSession.getWhoPlaying().forEach((v) -> v.sendMessage("Check Point is set on: " + math.round(sign.getLocation().getX()) + ", " + math.round(sign.getLocation().getY()) + ", " + math.round(sign.getLocation().getZ())));
                        }
                        break;
                }
            }
        } else //not inside the world of dungeon
        {
            if (
                    (
                            e.getHand() != null
                                    && e.getHand().equals(EquipmentSlot.HAND)
                    )
                            && e.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                            &&
                            (e.getClickedBlock().getType().equals(Material.WALL_SIGN)
                                    || e.getClickedBlock().getType().equals(Material.SIGN)
                                    || e.getClickedBlock().getType().equals(Material.SIGN_POST)
                            )) {
//				db.log("block: " + e.getClickedBlock().getType().name());
                Sign sign = (Sign) e.getClickedBlock().getState();

                switch (sign.getLine(0).toLowerCase()) {
                    case "[--dungeon--]":
                        if (sign.getLine(1).startsWith("DGID: ")) {
                            //substring start counting from "0"
                            DungeonSession dg = null;
                            int sessionID = gb.getPlayingIDbyPlayer(e.getPlayer());
                            DungeonSession session = null;

                            if (sessionID == -1) {
                                //that player has no playing dungeon session. create a new session
                                msg.send(e.getPlayer(), "&aPreparing a Dungeon Session for you. It might take a while, please be patient.");
                                session = BuilderV3.build(e.getPlayer(), sign.getLine(1).substring(6));
                                if (session == null) {
                                    msg.send(e.getPlayer(), "&cDungeon Creation Failure.");
                                    return;
                                }
                                gb.dungeonCreating.add(session.getSessionID());
                                gb.dungeonPlaying.put(e.getPlayer(), session.getSessionID());
                            } else {
                                if (gb.dungeonCreating.contains(sessionID)) {
                                    msg.send(e.getPlayer(), "Your Dungeon Session is still preparing, please wait.");
                                    return;
                                }

                                session = gb.dungeons.get(sessionID);
                                session.join(e.getPlayer());
                            }
                        }
                        break;
                }
            }
        }
    }
}
