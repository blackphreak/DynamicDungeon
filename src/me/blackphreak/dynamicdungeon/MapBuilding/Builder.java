package me.blackphreak.dynamicdungeon.MapBuilding;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.RegionSelection;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.registry.WorldData;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.spawning.spawners.SpawnerManager;
import me.blackphreak.dynamicdungeon.DynamicDungeon;
import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.MapBuilding.Hub.SessionCreationDoneEvent;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.Messages.msg;
import me.blackphreak.dynamicdungeon.gb;
import org.apache.commons.io.FileUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.primesoft.asyncworldedit.AsyncWorldEditBukkit;
import org.primesoft.asyncworldedit.api.blockPlacer.IBlockPlacer;
import org.primesoft.asyncworldedit.api.blockPlacer.IBlockPlacerListener;
import org.primesoft.asyncworldedit.api.blockPlacer.IJobEntryListener;
import org.primesoft.asyncworldedit.api.blockPlacer.entries.IJobEntry;
import org.primesoft.asyncworldedit.api.playerManager.IPlayerEntry;
import org.primesoft.asyncworldedit.api.utils.IAsyncCommand;
import org.primesoft.asyncworldedit.api.worldedit.IAsyncEditSessionFactory;
import org.primesoft.asyncworldedit.api.worldedit.IThreadSafeEditSession;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.HashSet;

public class Builder {
    public Builder() {
    }

    public static DungeonSession build(Player sessionOwner, String fileNameWithoutExtension) {
        File schematic = new File("plugins/DynamicDungeon/savedDungeons/" + fileNameWithoutExtension + ".schematic");

        try {
            IAsyncEditSessionFactory asyncEditSessionFactory = (IAsyncEditSessionFactory) WorldEdit.getInstance().getEditSessionFactory();
            IBlockPlacer placer = AsyncWorldEditBukkit.getInstance().getBlockPlacer();
            WorldData weWorldData = new BukkitWorld(gb.lastDungeonMaxLocation.getWorld()).getWorldData();

            FileInputStream inputStream = FileUtils.openInputStream(schematic);
            Clipboard clipboard = ClipboardFormat.SCHEMATIC.getReader(inputStream).read(weWorldData);
            inputStream.close();

            ClipboardHolder clipboardHolder = new ClipboardHolder(clipboard, weWorldData);



            /*

            FileInputStream inputStream = FileUtils.openInputStream(schematic);
            Clipboard clipboard = ClipboardFormat.SCHEMATIC.getReader(inputStream).read(weWorldData);
            inputStream.close();

            ClipboardHolder clipboardHolder = new ClipboardHolder(clipboard, weWorldData);

             */

            Location loc = gb.lastDungeonMaxLocation.add(clipboard.getRegion().getWidth() + 20, 0, clipboard.getRegion().getLength() + 20);


//			db.log("ccb: " + ccb.getWidth() + " | " + ccb.getLength() + " | " + ccb.getHeight());
//			db.log("loc: " + loc.getX() + " | " + loc.getY() + " | " + loc.getZ());
            Location max = loc.clone().add((double) clipboard.getRegion().getWidth(), 0, (double) clipboard.getRegion().getLength());
//			db.log("loc: " + loc.getX() + " | " + loc.getY() + " | " + loc.getZ());
//			db.log("max: " + max.getX() + " | " + max.getY() + " | " + max.getZ());
            Region region = new CuboidSelection(loc.getWorld(), loc, max).getRegionSelector().getRegion();

            IThreadSafeEditSession session = asyncEditSessionFactory.getThreadSafeEditSession(new BukkitWorld(loc.getWorld()), -1);

            //session.enableQueue();
            //session.setFastMode(true);


            DungeonSession dg = new DungeonSession(sessionOwner, region, session, loc, max);
            IPlayerEntry iPlayer = AsyncWorldEditBukkit.getInstance().getPlayerManager().createFakePlayer(dg.getUuid().toString(), dg.getUuid());
            //IPlayerEntry iPlayer = AsyncWorldEditBukkit.getInstance().getPlayerManager().getConsolePlayer();

            //creating a BlockPlacing task.
            //IAsyncCommand asyncCommand = AsyncWorldEditBukkit.getInstance().getOperations().getChunkOperations().createPaste(iPlayer, region, null, clipboard);
            IAsyncCommand asyncCommand = AsyncWorldEditBukkit.getInstance().getOperations().getChunkOperations().createPaste(iPlayer, new com.sk89q.worldedit.util.Location(region.getWorld(), region.getMinimumPoint()), region.getWorld(), null, clipboardHolder, false, true);

            /*
            new ClipboardAsyncTask(ccb, session, iPlayer, "DungeonCreation", placer, job) {
                @Override
                public void task(CuboidClipboard cuboidClipboard) throws MaxChangedBlocksException {
                    ccb.place(session, BukkitUtil.toVector(loc), true);
                }
            }.run();
            */

            SessionCreationDoneEvent doneEvent = () ->
            {
                boolean spawnPointSign = false;

                //get all the chunk inside the area.
                World world = loc.getWorld();
                int minX = loc.getChunk().getX();
                int mixZ = loc.getChunk().getZ();
                int maxX = max.getWorld().getChunkAt(max).getX();
                int maxZ = max.getWorld().getChunkAt(max).getZ();

                int unloaded = 0;
                //unload unused chunk
                for (Chunk loadedChunk : world.getLoadedChunks()) {
                    if (loadedChunk.unload(false)) {
                        unloaded++;
                    }
                }
                db.log("unloaded " + unloaded + " chunks");

                Collection<Chunk> chunks = new HashSet<>();
//			db.log(">x: " + (minX > maxX ? maxX : minX) + " | " + (minX > maxX ? minX : maxX));
                for (int x = (minX > maxX ? maxX : minX); x <= (minX > maxX ? minX : maxX); x = x + 8) {
//				db.log(">z: " + (mixZ > maxZ ? maxZ : mixZ) + " | " + (mixZ > maxZ ? mixZ : maxZ));
                    for (int z = (mixZ > maxZ ? maxZ : mixZ); z <= (mixZ > maxZ ? mixZ : maxZ); z = z + 8) {
                        Chunk chunk = world.getChunkAt(x, z);
                        chunks.add(chunk);
                    }
                }

                for (Chunk chunk : chunks) {
                    chunk.load(false);
                    for (BlockState blockState : chunk.getTileEntities()) {
                        db.log("BlockState: " + blockState.getType().name() + " | " + blockState.getLocation().toString());
                        if (blockState instanceof Sign) {
                            db.log("sign found.: " + blockState.getLocation().toString());
                            Sign sign = (Sign) blockState;
                            if (sign.getLine(0).toLowerCase().equals("[dg_spawner]")) {
                                dg.addSpawnerOnChunk(sign.getChunk(), sign.getLocation(), (new SpawnerManager(MythicMobs.inst())).getSpawnerByName(sign.getLine(1)));
                                blockState.getBlock().setType(Material.AIR);
                            } else if (!spawnPointSign && sign.getLine(0).toLowerCase().equals("[dg_spawnpoint]")) {
                                dg.setSpawnLocation(sign.getLocation());
                                spawnPointSign = true;
                                blockState.getBlock().setType(Material.AIR);
                            }
                        }
                    }

                }

                if (!spawnPointSign) {
                    msg.send(sessionOwner, "Please contact administrator to fix this [Error: Builder->SLNF]");
                    db.log("Dungeon creation error, Spawn Location not found!");
                    return;
                }

                dg.join(sessionOwner);
            };

            //add listener for the session owner
            msg.send(sessionOwner, "Preparing a DungeonSession for you...");
            IJobEntryListener jobListener = iJobEntry -> {
                switch (iJobEntry.getStatus().getSeqNumber()) {
                    case 3: //placing
                        msg.send(sessionOwner, "Creating a DungeonSession with sessionID: " + dg.getSessionID() + "(" + dg.getUuid().toString() + ")");
                        break;
                    case 4: //done
                        msg.send(sessionOwner, "DungeonSession Preparation Finished.");
                        msg.send(sessionOwner, "Teleporting to your DungeonSession...");
                        //session.flushQueue();
                        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                            @Override
                            public void run() {
                                doneEvent.done();
                            }
                        };
                        bukkitRunnable.runTask(DynamicDungeon.plugin);
                        break;
                    default:
                        msg.send(sessionOwner, "Your DungeonSession will be created soon. Please wait...");
                        break;
                }
            };
            IBlockPlacerListener listener = new IBlockPlacerListener() {
                public void jobAdded(IJobEntry job) {
                    if (job.getPlayer().getUUID() == iPlayer.getUUID()) {
                        //if (job.getPlayer().isConsole()) {
                        job.addStateChangedListener(jobListener);
                    }
                }

                public void jobRemoved(IJobEntry job) {
                    if (job.getPlayer().getUUID() == iPlayer.getUUID()) {
                        //if (job.getPlayer().isConsole()) {
                        job.removeStateChangedListener(jobListener);
                    }
                }
            };
            placer.addListener(listener);
            placer.performAsAsyncJob(session, asyncCommand);

            return dg;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }
}
