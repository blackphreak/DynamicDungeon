package me.blackphreak.dynamicdungeon.MapBuilding.Editor;

import com.sk89q.worldedit.regions.Region;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;

public class DungeonEditSessionManager implements Listener {
    private static DungeonEditSessionManager instance;
    private HashMap<Player, DungeonEditSession> sessionMap = new HashMap<>();

    public static DungeonEditSessionManager getInstance() {
        if (instance == null) {
            instance = new DungeonEditSessionManager();
        }
        return instance;
    }

    private DungeonEditSessionManager() {
    }

    public DungeonEditSession newSession(Player player, String dungeonName, Region region) {
        DungeonEditSession session = new DungeonEditSession(dungeonName, region);
        sessionMap.put(player, session);
        return session;
    }


    public DungeonEditSession getPlayerSession(Player player) {
        return sessionMap.get(player);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (sessionMap.containsKey(e.getPlayer())) {
            e.setCancelled(true);
        }
        DungeonEditSession session = sessionMap.get(e.getPlayer());
        if (session.getInputValue() != null) {
            session.inputValue(e.getMessage());

            String next = session.getInputValue();
            if (next != null) {
                e.getPlayer().sendMessage(next);
            } else {
                e.getPlayer().sendMessage("CREATED");
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getHand().equals(EquipmentSlot.OFF_HAND))
            return;

        if (sessionMap.containsKey(e.getPlayer())) {
            //EXIT
            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.LEATHER_BOOTS) {
                e.setCancelled(true);
                DungeonEditSession session = sessionMap.get(e.getPlayer());
                Block loc = e.getClickedBlock();
                session.createDungeonExit(loc.getX(), loc.getY(), loc.getZ());
                String next = session.getInputValue();
                if (next != null) {
                    e.getPlayer().sendMessage("INPUT:" + next);
                }
            }

            //MOB
            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.EGG) {
                e.setCancelled(true);
                DungeonEditSession session = sessionMap.get(e.getPlayer());
                Block loc = e.getClickedBlock();
                session.createDungeonMob(loc.getX(), loc.getY(), loc.getZ());
                String next = session.getInputValue();
                if (next != null) {
                    e.getPlayer().sendMessage("INPUT:" + next);
                }
            }

            //SPAWN
            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.BED) {
                e.setCancelled(true);
                DungeonEditSession session = sessionMap.get(e.getPlayer());
                Block loc = e.getClickedBlock();
                session.createDungeonSpawn(loc.getX(), loc.getY(), loc.getZ());
            }

            /*
                String next = session.getInputValue();
                if (next != null) {
                    e.getPlayer().sendMessage(next);
                }
             */
        }
    }

}
