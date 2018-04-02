package me.blackphreak.dynamicdungeon.MapBuilding.Editor;

import com.sk89q.worldedit.regions.Region;
import me.blackphreak.dynamicdungeon.ItemBuilder;
import me.blackphreak.dynamicdungeon.Messages.msg;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DungeonEditSessionManager implements Listener {
    private static DungeonEditSessionManager instance;
    private HashMap<Player, DungeonEditSession> sessionMap = new HashMap<>();
    private HashMap<Player, HashMap<Integer, ItemStack>> backpack = new HashMap<>();
    private List<String> editingMode = new ArrayList<>();
    private HashMap<Integer, ItemStack> editItemSet = new HashMap<Integer, ItemStack>()
    {{
        put(1, ItemBuilder.setItemNameAndLore(
                new ItemStack(Material.BED, 1),
                "&aSet Spawn Point",
                "&6Right-Click a block to set the spawn point",
                "&7[&cMultiple Spawn Point can be set!&7]" // TODO: random spawn maybe?
            )
        );
        put(2, ItemBuilder.setItemNameAndLore(
                new ItemStack(Material.EGG, 1),
                "&bSet MythicMob Spawner",
                "&6Right-Click a block to set the Spawner Location"
            )
        );
        put(3, ItemBuilder.setItemNameAndLore(
                new ItemStack(Material.LEATHER_BOOTS, 1),
                "&bSet Exit Point",
                "&6Right-Click a block to set the Exit Location"
            )
        );
        put(4, ItemBuilder.setItemNameAndLore(
                new ItemStack(Material.PAINTING, 1),
                "&bSet Decoration",
                "&6Right-Click a block to set the Decoration"
            )
        );
        put(8, ItemBuilder.setItemNameAndLore(
                new ItemStack(Material.COMPASS, 1),
                "&cExit Dungeon Editing Mode",
                "&6Right-Click to exit."
            )
        );
    }};

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
        editingMode.add(player.getName());
        msg.send(player, "Edit Session Created.");
        msg.send(player, "Dungeon Editing Mode Enabled. [All chat will not be sent to others.]");
        msg.send(player, "-- Hint: Hold & Right-Click \"COMPASS\" to finish exit Editing Mode.");
        
        // change the inventory to our item
        HashMap<Integer, ItemStack> map = new HashMap<>();
        editItemSet.forEach((k, v) -> {
            map.put(k, player.getInventory().getItem(k));
            player.getInventory().setItem(k, v);
        });
        backpack.put(player, map);
        return session;
    }

    public DungeonEditSession getPlayerSession(Player player) {
        return sessionMap.get(player);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (editingMode.contains(e.getPlayer().getName())) {
            e.setCancelled(true);
            
            DungeonEditSession session = sessionMap.get(e.getPlayer());
            if (session.getInputValue() != null) {
                // for DungeonExit only.
                if (e.getMessage().toLowerCase().equals("ok"))
                {
                    session.inputValue(e.getPlayer().getLocation());
                    e.getPlayer().sendMessage("Finished setup for this.");
            
                    return;
                }
                session.inputValue(e.getMessage());
        
                String next = session.getInputValue();
                if (next != null) {
                    e.getPlayer().sendMessage("Required: " + next);
                } else {
                    e.getPlayer().sendMessage("Finished setup for this.");
                }
            }
        }
        
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getHand().equals(EquipmentSlot.OFF_HAND))
            return;

        if (editingMode.contains(e.getPlayer().getName())) {
            //EXIT
            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.LEATHER_BOOTS) {
                e.setCancelled(true);
                DungeonEditSession session = sessionMap.get(e.getPlayer());
                Block loc = e.getClickedBlock();
                session.createDungeonExit(loc.getX(), loc.getY() + 1, loc.getZ());
                e.getPlayer().sendMessage("Please go to target location & type ok");
            }

            //MOB
            else if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.EGG) {
                e.setCancelled(true);
                DungeonEditSession session = sessionMap.get(e.getPlayer());
                Block loc = e.getClickedBlock();
                session.createDungeonMob(loc.getX(), loc.getY() + 1, loc.getZ());
                String next = session.getInputValue();
                if (next != null) {
                    e.getPlayer().sendMessage("Required:" + next);
                }
            }

            //SPAWN
            else if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.BED) {
                e.setCancelled(true);
                DungeonEditSession session = sessionMap.get(e.getPlayer());
                Block loc = e.getClickedBlock();
                session.createDungeonSpawn(loc.getX(), loc.getY() + 1, loc.getZ());
                e.getPlayer().sendMessage("Finished setup for Dungeon SpawnPoint.");
            }

            //DECORATION
            else if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.PAINTING) {
                e.setCancelled(true);
                DungeonEditSession session = sessionMap.get(e.getPlayer());
                Block loc = e.getClickedBlock();
                session.createDungeonDecoration(loc.getX(), loc.getY() + 1, loc.getZ());
                String next = session.getInputValue();
                if (next != null) {
                    e.getPlayer().sendMessage("Required:" + next);
                }
            }
            
            //Exit Editing Mode
            else if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.COMPASS) {
                e.setCancelled(true);
                editingMode.remove(e.getPlayer().getName());
                msg.send(e.getPlayer(), "Exited Dungeon Editing Mode.");
                
                //give back the item to player
                editItemSet.forEach((k, v) -> e.getPlayer().getInventory().setItem(k, backpack.get(e.getPlayer()).get(k)));
                backpack.remove(e.getPlayer());
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
