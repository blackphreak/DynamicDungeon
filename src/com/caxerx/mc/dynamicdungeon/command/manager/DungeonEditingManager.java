package com.caxerx.mc.dynamicdungeon.command.manager;

import com.caxerx.mc.lib.userinput.ChatInput;
import com.sk89q.worldedit.regions.Region;
import kotlin.Pair;
import me.blackphreak.dynamicdungeon.ItemBuilder;
import me.blackphreak.dynamicdungeon.Messages.msg;
import me.blackphreak.dynamicdungeon.dungeonobject.OffsetLocation;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DungeonEditingManager implements Listener {
    private static DungeonEditingManager instance;
    private HashMap<Player, HashMap<Integer, ItemStack>> savedItem = new HashMap<>();
    private static HashMap<Integer, ItemStack> editItemSet = new HashMap<Integer, ItemStack>() {{
        put(1, ItemBuilder.setItemNameAndLore(
                new ItemStack(Material.BED, 1),
                "&aSet Spawn Point",
                "&6Right-Click a block to set the spawn point",
                "&7[&cMultiple Spawn Point can be set!&7]"
                )
        );
        put(2, ItemBuilder.setItemNameAndLore(
                new ItemStack(Material.EGG, 1),
                "&bSet MythicMob Spawner",
                "&6Right-Click a block to set the Spawner Location"
                )
        );
        put(3, ItemBuilder.setItemNameAndLore(
                new ItemStack(Material.PAINTING, 1),
                "&bSet Decoration",
                "&6Right-Click a block to set the Decoration"
                )
        );
        put(4, ItemBuilder.setItemNameAndLore(
                new ItemStack(Material.REDSTONE_TORCH_ON, 1),
                "&bSet Triggers",
                "&6Right-Click to set the Trigger."
                )
        );
        put(5, ItemBuilder.setItemNameAndLore(
                new ItemStack(Material.STICK, 1),
                "&bInput a Location",
                "&6Right-Click to input the Location."
                )
        );
        put(6, ItemBuilder.setItemNameAndLore(
                new ItemStack(Material.BONE, 1),
                "&bAdd Action to Trigger",
                "&6Right-Click to add the Action."
                )
        );
        put(7, ItemBuilder.setItemNameAndLore(
                new ItemStack(Material.FEATHER, 1),
                "&cExit Dungeon Editing Mode",
                "&6Right-Click to exit."
                )
        );

    }};

    public static DungeonEditingManager getInstnace() {
        if (instance == null) {
            instance = new DungeonEditingManager();
        }
        return instance;
    }

    public void enterEditMode(Player player) {
        HashMap<Integer, ItemStack> save = new HashMap<>();
        savedItem.put(player, save);
        editItemSet.keySet().forEach(idx -> {
            save.put(idx, player.getInventory().getItem(idx));
            player.getInventory().setItem(idx, editItemSet.get(idx));
        });

        msg.send(player, "Edit Session Created.");
        msg.send(player, "Dungeon Editing Mode Enabled. [Pls use items in your hand to add object.]");
        msg.send(player, "&7-- &6Hint&7: &fHold &7& Right-Click &b\"FEATHER\" &fto finish exit Editing Mode.");
    }


    public void exitEditMode(Player player) {
        if (savedItem.containsKey(player)) {
            HashMap<Integer, ItemStack> saved = savedItem.get(player);
            saved.forEach((idx, itm) -> player.getInventory().setItem(idx, itm));
            savedItem.remove(player);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getHand().equals(EquipmentSlot.OFF_HAND))
            return;
        if (savedItem.containsKey(e.getPlayer())) {
            e.setCancelled(true);
            Block loc = e.getClickedBlock();

            //MOB SPAWNER
            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.EGG) {
                Pair<String, Region> dun = DungeonSelectManager.INSTANCE.getSelectedDungeon(e.getPlayer());
                e.getPlayer().performCommand("dde add mobspawner 0:" + OffsetLocation.createFromBukkitLocation(e.getClickedBlock().getLocation()).subtract(OffsetLocation.createFromWorldEditVector(dun.getSecond().getMinimumPoint())).add(0.5, 1, 0.5).toString());
            }

            //SPAWN
            else if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.BED) {
                Pair<String, Region> dun = DungeonSelectManager.INSTANCE.getSelectedDungeon(e.getPlayer());
                e.getPlayer().performCommand("dde add spawn 0:" + OffsetLocation.createFromBukkitLocation(e.getClickedBlock().getLocation()).subtract(OffsetLocation.createFromWorldEditVector(dun.getSecond().getMinimumPoint())).add(0.5, 1, 0.5).toString());
            }

            //DECORATION
            else if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.PAINTING) {
                List<Pair<String, Class<?>>> ipconstraint = new ArrayList<>();
                ipconstraint.add(new Pair<>("Decoration Type", String.class));
                new ChatInput(e.getPlayer(), ipconstraint, input -> {
                    Pair<String, Region> dun = DungeonSelectManager.INSTANCE.getSelectedDungeon(e.getPlayer());
                    String in = (String) input.get(0);
                    switch (in.toLowerCase()) {
                        case "holodisplay":
                        case "holographic":
                        case "hologram":
                        case "hg":
                        case "hd":
                            e.getPlayer().performCommand("dde add hologram 0:" + OffsetLocation.createFromBukkitLocation(e.getClickedBlock().getLocation()).subtract(OffsetLocation.createFromWorldEditVector(dun.getSecond().getMinimumPoint())).add(0.5, 1, 0.5).toString());
                            break;
                        case "schematic":
                        case "schem":
                            e.getPlayer().performCommand("dde add schematic 0:" + OffsetLocation.createFromBukkitLocation(e.getClickedBlock().getLocation()).subtract(OffsetLocation.createFromWorldEditVector(dun.getSecond().getMinimumPoint())).add(0, 1, 0).toString());
                            break;
                        default:
                            e.getPlayer().sendMessage("Unknown type");
                    }
                });
            }

            //LOCATION INPUT
            else if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.STICK) {
                Pair<String, Region> dun = DungeonSelectManager.INSTANCE.getSelectedDungeon(e.getPlayer());
                e.getPlayer().chat(OffsetLocation.createFromBukkitLocation(e.getClickedBlock().getLocation()).subtract(OffsetLocation.createFromWorldEditVector(dun.getSecond().getMinimumPoint())).add(0, 1, 0).toString());
            }

            //ACTION
            else if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.BONE) {
                if (!DungeonSelectManager.INSTANCE.isTriggerSelected(e.getPlayer())) {
                    e.getPlayer().sendMessage("Please select a trigger first");
                    return;
                }
                List<Pair<String, Class<?>>> ipconstraint = new ArrayList<>();
                ipconstraint.add(new Pair<>("Action Type", String.class));
                new ChatInput(e.getPlayer(), ipconstraint, input -> {
                    String tri = DungeonSelectManager.INSTANCE.getSelectedTrigger(e.getPlayer());
                    String in = (String) input.get(0);
                    try {
                        tri = URLEncoder.encode(tri, "UTF-8");
                    } catch (UnsupportedEncodingException ignore) {
                    }

                    switch (in.toLowerCase()) {
                        case "checkpoint":
                        case "ckpt":
                        case "cp":
                            e.getPlayer().performCommand("dde trigger action checkpoint 0:" + tri);
                            break;
                        case "damage":
                            e.getPlayer().performCommand("dde trigger action damage 0:" + tri);
                            break;
                        case "message":
                        case "msg":
                            e.getPlayer().performCommand("dde trigger action message 0:" + tri);
                            break;
                        default:
                            e.getPlayer().sendMessage("Unknown type of action.");
                    }
                });
            }

            //TRIGGER
            else if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.REDSTONE_TORCH_ON) {
                List<Pair<String, Class<?>>> ipconstraint = new ArrayList<>();
                ipconstraint.add(new Pair<>("Trigger Type", String.class));
                new ChatInput(e.getPlayer(), ipconstraint, input -> {
                    //String tri = DungeonSelectManager.INSTANCE.getSelectedTrigger(e.getPlayer());
                    //db.log("tri: " + tri + " | inp: " + input.get(0)); //out: tri: null | inp: location
                    String in = (String) input.get(0);
                    switch (in.toLowerCase()) {
                        case "location":
                        case "loc":
                            e.getPlayer().performCommand("dde trigger add location");
                            break;
                        case "mobkill":
                        case "mk":
                            e.getPlayer().performCommand("dde trigger add mobkill");
                            break;
                        case "interact":
                        case "int":
                            e.getPlayer().performCommand("dde trigger add interact");
                            break;
                        default:
                            e.getPlayer().sendMessage("Unknown trigger type, pls add again.");
                    }
                });
            }

            //Exit Editing Mode
            else if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.FEATHER) {
                exitEditMode(e.getPlayer());
            }
        }
    }


}
