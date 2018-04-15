package me.blackphreak.dynamicdungeon.MapBuilding;

import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.regions.Region;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.Messages.msg;
import me.blackphreak.dynamicdungeon.Supports.HolographicDisplays.cHologram;
import me.blackphreak.dynamicdungeon.Supports.HolographicDisplays.cHologramManager;
import me.blackphreak.dynamicdungeon.dungeonobject.DungeonObject;
import me.blackphreak.dynamicdungeon.dungeonobject.action.DungeonAction;
import me.blackphreak.dynamicdungeon.dungeonobject.base.DungeonHologramDecorate;
import me.blackphreak.dynamicdungeon.dungeonobject.base.DungeonMobSpawner;
import me.blackphreak.dynamicdungeon.dungeonobject.base.DungeonSpawn;
import me.blackphreak.dynamicdungeon.dungeonobject.trigger.DungeonTrigger;
import me.blackphreak.dynamicdungeon.gb;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class BuilderV3 {
	public BuilderV3() {
	}
	
	public static DungeonSession build(Player sessionOwner, String fileNameWithoutExtension) {
		msg.send(sessionOwner, "&aPreparing your DungeonSession...");
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
			
			//pasting task done listener
			session.addNotifyTask(() ->
			{
				//flushing the queue
				session.flushQueue();
				
				db.log("Loading DungeonObject for session...");
				
				List<DungeonAction> actionList = new ArrayList<>();
				
				for (DungeonObject obj : gb.cloneDungeon(fileNameWithoutExtension)) {
					db.tlog(obj.toString());
					
					try
					{
						if (obj instanceof DungeonSpawn) {
							DungeonSpawn dgsp = ((DungeonSpawn) obj);
							dg.setSpawnLocation(dgsp.getLocation().add(dg.getDgMinPt()).toBukkitLoc());
						} else if (obj instanceof DungeonMobSpawner) {
							DungeonMobSpawner dgmobspawner = ((DungeonMobSpawner) obj);
							dg.addSpawnerOnChunk(dgmobspawner.getSpawnerName(), dgmobspawner.getLocation().add(dg.getDgMinPt()).toBukkitLoc());
						} else if (obj instanceof DungeonHologramDecorate) {
							if (gb.hd != null) {
								DungeonHologramDecorate dghd = (DungeonHologramDecorate) obj;
								cHologram chg = cHologramManager.getOrRegister(dghd.getHologramName()).clone();
								dg.addHologram(chg);
								chg.teleport(dghd.getLocation().add(loc).add(0, dghd.getYOffset(), 0).toBukkitLoc());
							}
						} else if (obj instanceof DungeonTrigger) {
							dg.addTrigger((DungeonTrigger) obj);
						} else if (obj instanceof DungeonAction) {
							actionList.add((DungeonAction) obj); //process this later
						}
					}
					catch (Exception e)
					{
						db.log("Unexpected Error Occurred [Obj], please post the message below to https://github.com/blackphreak/DynamicDungeon/issues");
						e.printStackTrace();
						msg.send(sessionOwner, "Please contact admin to fix this. [UEEO_OBJ]");
						
						msg.send(sessionOwner, "Killing Dungeon Session...");
						dg.killSession();
						
						return; // no need to do the rest of dungeon session creation.
					}
				}
				
				for (DungeonAction actionObj : actionList) {
					
					try {
						dg.getTriggerByName(URLDecoder.decode(actionObj.getTriggerBy(), "UTF-8")).addAction(actionObj);
					} catch (Exception e) {
						db.log("Unexpected Error Occurred [Obj], please post the message below to https://github.com/blackphreak/DynamicDungeon/issues");
						e.printStackTrace();
						msg.send(sessionOwner, "Please contact admin to fix this. [UEEO_AOBJ]");
						
						msg.send(sessionOwner, "Killing Dungeon Session...");
						dg.killSession();
						
						return;
					}
				}
				
				db.log("Dungeon Session created.");
				msg.send(sessionOwner, "&aYour DungeonSession is ready!");
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
