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
import me.blackphreak.dynamicdungeon.dungeonobject.ActionNeeded;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import me.blackphreak.dynamicdungeon.dungeonobject.OffsetLocation;
import me.blackphreak.dynamicdungeon.gb;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;

public class SchematicAction extends DungeonAction {
	@DDField(name = "Schematic Name")
	private String schematicName;
	
	@DDField(name = "[Location] Paste to")
	private OffsetLocation loc;
	
	/*@DDField(name = "Transform")
	private String transform;*/
	
	/**
	 * String triggerName:
	 * acceptable values:
	 * String, the name of an existing trigger.
	 * OR
	 * "" <-- this is the input of String empty
	 * when the schematic paste is done, the trigger will be fired.
	 * TODO: add to wiki
	 * TODO: transform
	 */
	@DDField(name = "Trigger Name")
	private String triggerName;
	
	@DDField(name = "Name for Undo")
	private String undoName;
	
	@Override
	public void action(DungeonSession dg, ActionNeeded needed) {
		try {
			Location minLoc = this.loc.clone().add(dg.getDgMinPt()).toBukkitLoc();
			File schematic = new File(gb.decorationPath + schematicName + ".schematic");
			Clipboard cp = FaweAPI.load(schematic).getClipboard();
			final Vector maxVt = cp.getDimensions();
			Region region = new CuboidSelection(gb.dgWorld, minLoc, this.loc.clone().add(maxVt.getBlockX(), 0, maxVt.getBlockZ()).toBukkitLoc()).getRegionSelector().getRegion();
			EditSession session = ClipboardFormat.SCHEMATIC.load(schematic).paste(region.getWorld(), new Vector(minLoc.getBlockX(), minLoc.getBlockY(), minLoc.getBlockZ()), true, true, null);
			session.enableQueue();
			
			// when the action is done
			session.addNotifyTask(() ->
			{
				//flushing the queue
				session.flushQueue();
				
				dg.putSchematicSession(undoName, session);
				
				if (!triggerName.isEmpty())
					dg.fireTheTrigger(triggerName, needed.setPreviousTrigger(this.getTriggerBy()));
			});
		} catch (IncompleteRegionException e) {
			db.log("Error occurred when doing schematicAction, pls report to https://github.com/blackphreak/DynamicDungeon/issues :");
			e.printStackTrace();
		} catch (IOException e) {
			db.log("This may due to schematic file not found in path: plugins/DynamicDungeon/decorations/");
			db.log("Error occurred when doing schematicAction, pls report to https://github.com/blackphreak/DynamicDungeon/issues :");
			e.printStackTrace();
		} catch (Exception e) {
			db.log("Error occurred when doing schematicAction, pls report to https://github.com/blackphreak/DynamicDungeon/issues :");
			e.printStackTrace();
		}
	}
}

