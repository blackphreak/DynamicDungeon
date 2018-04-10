package me.blackphreak.dynamicdungeon.dungeonobject.action;

import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.regions.Region;
import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import me.blackphreak.dynamicdungeon.gb;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;

public class SchematicAction extends LocationActionObject {
	@DDField(name = "Schematic Name")
	private String schematicName;
	
	@DDField(name = "Transform")
	private String transform;
	
	/**
	 * String triggerName:
	 * acceptable values:
	 *      String, the name of an existing trigger.
	 *      OR
	 *      "" <-- this is the input of String empty
	 * when the schematic paste is done, the trigger will be fired.
	 * TODO: add to wiki
	 */
	@DDField(name = "Trigger Name")
	private String triggerName;
	
	@Override
	public void action(DungeonSession dg) {
		try {
			Location loc = getLocation().add(dg.getDgMinPt()).toBukkitLoc();
			File schematic = new File(gb.decorationPath + schematicName);
			Clipboard cp = FaweAPI.load(schematic).getClipboard();
			final Vector maxVt = cp.getDimensions();
			Region region = new CuboidSelection(gb.dgWorld, loc, getLocation().add(dg.getDgMinPt()).add(maxVt.getBlockX(), 0, maxVt.getBlockZ()).toBukkitLoc()).getRegionSelector().getRegion();
			EditSession session = ClipboardFormat.SCHEMATIC.load(schematic).paste(region.getWorld(), new Vector(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), true, true, null);
			session.enableQueue();
			
			// when the action is done
			session.addNotifyTask(() ->
			{
				//flushing the queue
				session.flushQueue();
				
				dg.fireTheTrigger(triggerName);
			});
		} catch (IncompleteRegionException e) {
			db.log("Error occurred when doing schematicAction, pls report to https://github.com/blackphreak/DynamicDungeon/pulls :");
			e.printStackTrace();
		} catch (IOException e) {
			db.log("This may due to schematic file not found in path: plugins/DynamicDungeon/decorations/");
			db.log("Error occurred when doing schematicAction, pls report to https://github.com/blackphreak/DynamicDungeon/pulls :");
			e.printStackTrace();
		}
	}
}

