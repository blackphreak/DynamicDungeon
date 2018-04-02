package me.blackphreak.dynamicdungeon.MapBuilding;

import com.boydti.fawe.FaweAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.regions.Region;
import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.MapBuilding.Objects.*;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.Messages.msg;
import me.blackphreak.dynamicdungeon.Supports.HolographicDisplays.cHologram;
import me.blackphreak.dynamicdungeon.Supports.HolographicDisplays.cHologramManager;
import me.blackphreak.dynamicdungeon.gb;
import org.apache.commons.io.FileUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

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


//            File signListFile = new File(gb.dataPath + fileNameWithoutExtension + ".json");
            //cLocation[] signs = new Gson().fromJson(FileUtils.readFileToString(signListFile, Charset.defaultCharset()), cLocation[].class);

            //pasting task done listener
            session.addNotifyTask(() ->
            {
                //flushing the queue
                session.flushQueue();


                Gson gson = new GsonBuilder().registerTypeAdapter(DungeonObject.class, new DungeonObjectDeserializer()).create();
                File dungeonFile = new File(gb.dataPath + fileNameWithoutExtension + ".json");

                DungeonObject[] objs;
                try {
                    String content = FileUtils.readFileToString(dungeonFile, Charset.defaultCharset());
                    objs = gson.fromJson(content, DungeonObject[].class);
//                    db.log(Arrays.toString(objs));
                } catch (IOException e) {
                    db.log("Error on reading Dungeon Object File");
                    e.printStackTrace();
                    return;
                }

                db.log("Loading chunks for session...");
                World world = loc.getWorld();

                for (DungeonObject dungeonObject : objs) {
//                    db.log(dungeonObject.toString());
                    switch (dungeonObject.getType()) {
                        case "exit":
                            //Unimplemented
                            break;
                        case "spawn":
                            DungeonSpawn dgsp = ((DungeonSpawn) dungeonObject);
                            dg.setSpawnLocation(new Location(world, dgsp.getX(), dgsp.getY(), dgsp.getZ()).add(loc));
                            break;
                        case "mobspawner":
                            DungeonMobSpawner dgmobspawner = ((DungeonMobSpawner) dungeonObject);
                            dg.addSpawnerOnChunk(dgmobspawner.getSpawner(), new Location(world, dgmobspawner.getX(), dgmobspawner.getY(), dgmobspawner.getZ()).add(loc));
                            break;
                        case "hddec":
                            if (gb.hd != null)
                            {
                                DungeonHDDecorate dghd = (DungeonHDDecorate) dungeonObject;
                                cHologram chg = cHologramManager.getOrRegister(dghd.getName()).clone();
                                dg.addHologram(chg);
                                chg.teleport(new Location(world, dghd.getX(), dghd.getY(), dghd.getZ()).add(loc).add(0, dghd.getOffset(), 0));
                            }
                            break;
                        case "schemdec":
                            db.log("schematic decoration here.");
                            break;
                    }
                }

                db.log("Dungeon Session created.");
                msg.send(sessionOwner, "&eTeleporting to your DungeonSession...");
                dg.join(sessionOwner);

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
