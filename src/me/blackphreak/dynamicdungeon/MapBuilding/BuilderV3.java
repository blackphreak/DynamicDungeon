package me.blackphreak.dynamicdungeon.MapBuilding;

import com.boydti.fawe.FaweAPI;
import com.google.gson.Gson;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.regions.Region;
import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.MapBuilding.Objects.cLocation;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.Messages.msg;
import me.blackphreak.dynamicdungeon.Supports.HolographicDisplays.cHologram;
import me.blackphreak.dynamicdungeon.Supports.HolographicDisplays.cHologramManager;
import me.blackphreak.dynamicdungeon.gb;
import me.blackphreak.dynamicdungeon.math;
import org.apache.commons.io.FileUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BuilderV3 {
    public BuilderV3() {
    }

    public static DungeonSession build(Player sessionOwner, String fileNameWithoutExtension) {
        try {
            File schematic = new File(gb.dataPath + fileNameWithoutExtension + ".schematic");
            Clipboard cp = FaweAPI.load(schematic).getClipboard();

            Location loc = gb.nextDungeonLocation.clone();
            db.log("Building Session at Loc[" + loc.toString() + "] Owner[" + sessionOwner.getName() + "]");

            final Vector maxVt = cp.getDimensions();

            Location max = loc.clone().add(maxVt.getBlockX(), 0, maxVt.getBlockZ());
            db.tlog("maxLoc: [" + max.toString() + "]");
            Region region = new CuboidSelection(gb.dgWorld, loc, max).getRegionSelector().getRegion();
            EditSession session = ClipboardFormat.SCHEMATIC.load(schematic).paste(region.getWorld(), new Vector(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), true, true, null);
            session.enableQueue();

            //update the next loc.
            gb.nextDungeonLocation = loc.clone().add(maxVt.getBlockX() + gb.gap, 0, 0);
            db.tlog("nextLoc: [" + gb.nextDungeonLocation.toString() + "]");

            DungeonSession dg = new DungeonSession(sessionOwner, region, session, loc, max);


            File signListFile = new File(gb.dataPath + fileNameWithoutExtension + ".json");
            cLocation[] signs = new Gson().fromJson(FileUtils.readFileToString(signListFile, Charset.defaultCharset()), cLocation[].class);
            //pasting task done listener
            session.addNotifyTask(() ->
            {
                //flushing the queue
                session.flushQueue();

                //getting location of spawn point & mob spawner
                boolean spawnPointSign = false;

                //get all the chunk inside the area.


                db.log("Loading chunks for session...");
                World world = loc.getWorld();
                List<Sign> signList = Arrays.stream(signs).map(s -> {
                    Location sloc = loc.clone();
                    sloc.add(s.getX(), s.getY(), s.getZ());
                    return (Sign) world.getBlockAt(sloc).getState();
                }).collect(Collectors.toList());

                for (Sign sign : signList) {
                    db.tlog("BlockState: " + sign.getType().name() + " | " + sign.getLocation().toString() + " | Line0[" + sign.getLine(0) + "]");

                    switch (sign.getLine(0).toLowerCase()) {
                        case "[dgmob]": //DunGeon MOB spawner
                            if (sign.getLine(1).isEmpty()) {
                                db.log("-+-- Error: Invalid Dungeon Mob Spawner Sign! Please Check!");
                                db.log(" + Debug:");
                                db.log(" +-- Sign Location: [" + sign.getX() + ", " + sign.getY() + ", " + sign.getZ() + "]");
                                db.log(" +-- Spawner Type : [dgmob]");
                                db.log(" +-- Debug Message: " + "line 2 (Spawner Name) is missing.");
                                db.logArr(" +-- Lines: ", sign.getLines());
                            } else
                                dg.addSpawnerOnChunk(sign);

                            sign.getBlock().setType(Material.AIR);
                            break;
                        case "[dgexit]": //DunGeon EXIT
                            if (sign.getLine(1).isEmpty()) {
                                db.log("-+-- Error: Invalid Dungeon Exit Sign! Please Check!");
                                db.log(" + Debug:");
                                db.log(" +-- Sign Location: [" + sign.getX() + ", " + sign.getY() + ", " + sign.getZ() + "]");
                                db.log(" +-- Spawner Type : [dgexit]");
                                db.log(" +-- Debug Message: " + "line 2 (Exit Location) is missing.");
                                db.logArr(" +-- Lines: ", sign.getLines());
                            } else {
                                dg.addExitPoint(
                                        sign.getLocation(), //sign location
                                        sign.getLine(1) //target location format:[world,x,y,z]
                                );
                            }

                            sign.getBlock().setType(Material.AIR);
                            break;
                        case "[dgdec]": //DunGeon DECoration
                            if (sign.getLine(1).isEmpty()) {
                                db.log("-+-- Error: Invalid Dungeon Decoration Sign! Please Check!");
                                db.log(" + Debug:");
                                db.log(" +-- Sign Location: [" + sign.getX() + ", " + sign.getY() + ", " + sign.getZ() + "]");
                                db.log(" +-- Spawner Type : [dgexit]");
                                db.log(" +-- Debug Message: " + "line 2 (Decoration Type) is missing.");
                                db.logArr(" +-- Lines: ", sign.getLines());
                            } else {
                                //use what decoration? format:[decType]
                                switch (sign.getLine(1).toLowerCase()) {
                                    case "hd":
                                        // Holographic Display
                                        if (gb.hd == null)
                                            continue;

                                        String hdName = sign.getLine(2); //holographic name

                                        if (hdName.isEmpty()) {
                                            db.log("-+-- Error: Invalid Dungeon Decoration Sign! Please Check!");
                                            db.log(" + Debug:");
                                            db.log(" +-- Sign Location: [" + sign.getX() + ", " + sign.getY() + ", " + sign.getZ() + "]");
                                            db.log(" +-- Spawner Type : [dgdec]");
                                            db.log(" +-- Debug Message: " + "line 3 (Holographic Name) is missing.");
                                            db.logArr(" +-- Lines: ", sign.getLines());

                                            continue;
                                        }
                                        if (!math.isDouble(sign.getLine(3))) {
                                            db.log("-+-- Error: Invalid Dungeon Decoration Sign! Please Check!");
                                            db.log(" + Debug:");
                                            db.log(" +-- Sign Location: [" + sign.getX() + ", " + sign.getY() + ", " + sign.getZ() + "]");
                                            db.log(" +-- Spawner Type : [dgdec]");
                                            db.log(" +-- Debug Message: " + "line 4 (offset) is invalid, it should be a double.");
                                            db.logArr(" +-- Lines: ", sign.getLines());

                                            continue;
                                        }
                                        double offset = Double.parseDouble(sign.getLine(3)); // offset value -- (double) offset of decoration y axis
                                        cHologram chg = cHologramManager.getOrRegister(hdName).clone();
                                        if (chg == null) {
                                            db.log("-+-- Error: Invalid Dungeon Decoration Sign! Please Check!");
                                            db.log(" + Debug:");
                                            db.log(" +-- Sign Location: [" + sign.getX() + ", " + sign.getY() + ", " + sign.getZ() + "]");
                                            db.log(" +-- Spawner Type : [dgdec]");
                                            db.log(" +-- Debug Message: " + "Hologram[" + hdName + "] not found!");
                                            db.logArr(" +-- Lines: ", sign.getLines());

                                            continue;
                                        }
                                        chg.teleport(sign.getLocation().add(0, offset, 0));
                                        break;
                                }
                                sign.getBlock().setType(Material.AIR);
                            }

                            break;
                    }
                    if (!spawnPointSign
                            && sign.getLine(0).toLowerCase().equals("[dgsp]")) //DunGeon Spawn Point
                    {
                        dg.setSpawnLocation(sign.getLocation());
                        spawnPointSign = true;
                        sign.getBlock().setType(Material.AIR);
                    }
                }


                if (!spawnPointSign) {
                    msg.send(sessionOwner, "Please contact administrator to fix this [Error: Builder->SLNF]");
                    db.log("Dungeon creation error, Spawn Location not found!");

                    //undo the dungeon session and mark as un-buildable dungeon
                    db.log("Killing Dungeon Session...");
                    dg.killSession();
                } else {
                    db.log("Dungeon Session created.");
                    msg.send(sessionOwner, "Teleporting to your DungeonSession...");
                    dg.join(sessionOwner);
                }
            });

            return dg;
        } catch (FileNotFoundException ex) {
            db.log("Dungeon File Not Found! [Name: " + fileNameWithoutExtension + ".schematic]");
            msg.send(sessionOwner, "Please contact admin to fix this. [DFNFE]"); //dungeon file not found exception
        } catch (Exception ex) {
            db.log("Unexpected Error Occurred, please post the message below to https://github.com/blackphreak/DynamicDungeon/issues");
            ex.printStackTrace();
            msg.send(sessionOwner, "Please contact admin to fix this. [UEEO]");
            DungeonSession dg = gb.getDungeonSessionByPlayer(sessionOwner);
            if (dg != null) {
                msg.send(sessionOwner, "Killing Dungeon Session...");
                dg.killSession();
            }
        }
        return null;
    }
}