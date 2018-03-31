package me.blackphreak.dynamicdungeon.MapBuilding;

import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.regions.Region;
import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.Messages.msg;
import me.blackphreak.dynamicdungeon.gb;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class BuilderV2 {
    public BuilderV2() {
    }

    public static DungeonSession build(Player sessionOwner, String fileNameWithoutExtension) {
        File schematic = new File("plugins/DynamicDungeon/savedDungeons/" + fileNameWithoutExtension + ".schematic");

        try {
            Clipboard cp = FaweAPI.load(schematic).getClipboard();
            
            Location loc = gb.nextDungeonLocation.clone();
            db.log("Building Session at Loc: [" + loc.toString() + "]");
            
            final Vector maxVt = cp.getDimensions();
    
            Location max = loc.clone().subtract(maxVt.getBlockX(), 0, maxVt.getBlockZ());
            db.log("maxLoc: ["+max.toString() + "]");
            Region region = new CuboidSelection(loc.getWorld(), max, loc).getRegionSelector().getRegion();
    
            EditSession session = ClipboardFormat.SCHEMATIC.load(schematic).paste(region.getWorld(), new Vector(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), true, true, null);
            session.enableQueue();
            
            //update the next loc.
            gb.nextDungeonLocation = loc.clone().add(100, 0 ,0);
            db.log("nextLoc: [" + gb.nextDungeonLocation.toString() + "]");
            
            DungeonSession dg = new DungeonSession(sessionOwner, region, session, loc, max);
            
            //pasting task done listener
            session.addNotifyTask(() ->
            {
                //flushing the queue
                session.flushQueue();
                
                //getting location of spawn point & mob spawner
                boolean spawnPointSign = false;
    
                //get all the chunk inside the area.
                World world = loc.getWorld();
                int minX = loc.getChunk().getX();
                int minZ = loc.getChunk().getZ();
                int maxX = max.getChunk().getX();
                int maxZ = max.getChunk().getZ();
    
                Collection<Chunk> chunks = new HashSet<>();
                db.tlog("min: [" + loc.toString() + "]");
                db.tlog("max: [" + max.toString() + "]");
                db.tlog("minX[" + minX + "], minZ[" + minZ + "] | maxX[" + maxX + "], maxZ[" + maxZ + "]");
    
                // switching pos for min & max
                if (minX > maxX)
                {
                    int old_minX = minX;
                    minX = maxX;
                    maxX = old_minX;
                }
                if (minZ > maxZ)
                {
                    int old_minZ = minZ;
                    minZ = maxZ;
                    maxZ = old_minZ;
                }
                
                // adding chunk to chunks list.
                for (int x = minX; x < maxX; x++)
                {
                    for (int z = minZ; z < maxZ; z++)
                    {
                        chunks.add(world.getChunkAt(x, z));
                    }
                }
                
                db.tlog("Loading chunks for session...");
                for (Chunk chunk : chunks)
                {
                    db.tlog("Checking Chunk @ [x:" + chunk.getX() + ", z:" + chunk.getZ() + "]");

                    List<BlockState> signs = Arrays.stream(chunk.getTileEntities())
                            .filter(s -> s instanceof Sign)
                            .collect(Collectors.toList());
                    
                    for (BlockState blockState : signs)
                    {
                        db.log(blockState.getType().name());
                        db.tlog("BlockState: " + blockState.getType().name() + " | " + blockState.getLocation().toString());

                        Sign sign = (Sign) blockState;

                        if (sign.getLine(0).toLowerCase().equals("[dgmob]")) //DunGeon MOB spawner
                        {
                            dg.addSpawnerOnChunk(sign.getChunk(),
                                    sign.getLocation(),
                                    sign.getLine(1)
                            );

                            blockState.getBlock().setType(Material.AIR);
                        }
                        else if (!spawnPointSign
                                && sign.getLine(0).toLowerCase().equals("[dgsp]")) //DunGeon Spawn Point
                        {
                            dg.setSpawnLocation(sign.getLocation());
                            spawnPointSign = true;
                            blockState.getBlock().setType(Material.AIR);
                        }
                    }
                }
    
                if (!spawnPointSign)
                {
                    msg.send(sessionOwner, "Please contact administrator to fix this [Error: Builder->SLNF]");
                    db.log("Dungeon creation error, Spawn Location not found!");
        
                    //undo the dungeon session and mark as un-buildable dungeon
                    db.log("Killing Dungeon Session...");
                    dg.killSession();
                }
                else
                {
                    msg.send(sessionOwner, "Teleporting to your DungeonSession...");
                    dg.join(sessionOwner);
                }
    
                chunks.clear();
            });
    
            return dg;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }
}
