package me.blackphreak.dynamicdungeon.MapBuilding.Editor;

import com.sk89q.worldedit.regions.Region;
import me.blackphreak.dynamicdungeon.ItemBuilder;
import me.blackphreak.dynamicdungeon.Messages.msg;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class DungeonEditSessionManager implements Listener {
    private static DungeonEditSessionManager instance;
    private HashMap<Player, DungeonEditSession> sessionMap = new HashMap<>();
    private HashMap<Player, HashMap<Integer, ItemStack>> backpack = new HashMap<>();
    private HashMap<Integer, ItemStack> editItemSet = new HashMap<Integer, ItemStack>() {{
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
        put(5, ItemBuilder.setItemNameAndLore(
                new ItemStack(Material.REDSTONE_TORCH_ON, 1),
                "&bSet Triggers",
                "&6Right-Click to set the Trigger."
                )
        );
        put(8, ItemBuilder.setItemNameAndLore(
                new ItemStack(Material.FEATHER, 1),
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
        DungeonEditSession session = new DungeonEditSession(player, dungeonName, region);
        sessionMap.put(player, session);
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
        DungeonEditSession session;
        if ((session = sessionMap.get(e.getPlayer())) != null
                && session.getInputValue() != null) {
            e.setCancelled(true);

            // for DungeonLocation & LocationTrigger only.
            if (session.getInputValue()
                    .equalsIgnoreCase("Location [Type \"ok\" when you are there]")
                    && e.getMessage().equalsIgnoreCase("ok")
                    ) {
                session.inputValue(e.getPlayer().getLocation());
            }
			/*else if (session.getInputValue()
					.equalsIgnoreCase("Trigger Type")
				)
			{
				switch (e.getMessage().toLowerCase())
				{
					case "location":
						session.inputValue(e.getPlayer().getLocation());
						break;
					case "mobkill":
						session.inputValue(e.getPlayer().getLocation());
						break;
					case "interact":
						session.inputValue(e.getPlayer().getLocation());
						break;
				}
			}*/
            else {
                session.inputValue(e.getMessage());
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getHand().equals(EquipmentSlot.OFF_HAND))
            return;

        if (sessionMap.containsKey(e.getPlayer())) {
            DungeonEditSession session = sessionMap.get(e.getPlayer());
            if (session == null) {
                return;
            }
            e.setCancelled(true);

            if (session.getInputValue() != null) {
                // for InteractTrigger only.
                if (session.getInputValue()
                        .equalsIgnoreCase("Click The Block!")
                        && !e.getClickedBlock().getType().equals(Material.AIR)
                        ) {
                    session.inputValue(e);
                    return;
                }
            }

            //EXIT
            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.LEATHER_BOOTS) {
                Block loc = e.getClickedBlock();
                session.createDungeonExit(loc.getX(), loc.getY() + 1, loc.getZ());
                e.getPlayer().sendMessage("To set exit location, go to target location & type \"ok\"");
            }

            //MOB
            else if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.EGG) {
                Block loc = e.getClickedBlock();
                session.createDungeonMob(loc.getX(), loc.getY() + 1, loc.getZ());
            }

            //SPAWN
            else if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.BED) {
                Block loc = e.getClickedBlock();
                session.createDungeonSpawn(loc.getX(), loc.getY() + 1, loc.getZ());
            }

            //DECORATION
            else if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.PAINTING) {
                Block loc = e.getClickedBlock();
                session.createDungeonDecoration(loc.getX(), loc.getY() + 1, loc.getZ());
            }

            //TRIGGER
            else if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.REDSTONE_TORCH_ON) {
                Block loc = e.getClickedBlock();
                session.createTrigger(loc.getX(), loc.getY() + 1, loc.getZ());
            }

            //Exit Editing Mode
            else if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.FEATHER) {
                msg.send(e.getPlayer(), "Exited Dungeon Editing Mode.");

                session.save();
                sessionMap.remove(e.getPlayer());

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
