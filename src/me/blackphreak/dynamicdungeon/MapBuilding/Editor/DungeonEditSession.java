package me.blackphreak.dynamicdungeon.MapBuilding.Editor;

import com.google.gson.Gson;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.Region;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.Messages.msg;
import me.blackphreak.dynamicdungeon.Objects.Actions.TriggerAction;
import me.blackphreak.dynamicdungeon.Objects.*;
import me.blackphreak.dynamicdungeon.Objects.Triggers.InteractTrigger;
import me.blackphreak.dynamicdungeon.Objects.Triggers.LocationTrigger;
import me.blackphreak.dynamicdungeon.Objects.Triggers.MobKillTrigger;
import me.blackphreak.dynamicdungeon.gb;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class DungeonEditSession {
	private List<DungeonObject> dungeonObjectList = new ArrayList<>();
	private DungeonObject lastEdit;
	private AbstractMap.SimpleEntry<String, TriConsumer<DungeonEditSession, DungeonObject, Object>> valueOperation;
	private Player player;
	private String dungeonName;
	private Region region;
	public final Vector minPoint;
	
	public DungeonEditSession(Player player, String dungeonName, Region region) {
		this.player = player;
		this.dungeonName = dungeonName;
		this.region = region;
		this.minPoint = region.getMinimumPoint();
	}
	
	public void save() {
		try {
			FileUtils.writeStringToFile(new File(gb.dataPath + dungeonName + ".json"), new Gson().toJson(dungeonObjectList), Charset.defaultCharset());
		} catch (IOException e) {
			db.log("Error on saving Dungeon[" + dungeonName + "]'s objects");
			e.printStackTrace();
		}
	}
	
	public void createDungeonExit(int x, int y, int z) {
		x -= minPoint.getBlockX();
		y -= minPoint.getBlockY();
		z -= minPoint.getBlockZ();
		lastEdit = new DungeonLocation(x, y, z);
		updateOperation();
	}
	
	public void createDungeonSpawn(int x, int y, int z) {
		x -= minPoint.getBlockX();
		y -= minPoint.getBlockY();
		z -= minPoint.getBlockZ();
		lastEdit = new DungeonSpawn(x, y, z);
		updateOperation();
	}
	
	public void createDungeonMob(int x, int y, int z) {
		x -= minPoint.getBlockX();
		y -= minPoint.getBlockY();
		z -= minPoint.getBlockZ();
		lastEdit = new DungeonMobSpawner(x, y, z);
		updateOperation();
	}
	
	
	public void createDungeonDecoration(int x, int y, int z) {
		lastEdit = new DungeonPlaceholderObject();
		valueOperation = new AbstractMap.SimpleEntry<>("Decoration Type", (es, dobj, type) -> {
			String strType = (String) type;
			switch (strType.toLowerCase()) {
				case "hd":
					createDungeonHDDecoration(x, y, z);
					break;
				case "schematic":
				case "schem":
					createDungeonSchematicDecoration(x, y, z);
					break;
			}
		});
		msg.send(player, "&6Decoration Setup");
		msg.send(player, "&7+-> &aDecoration Type &7[&ehd &7| &eschematic&7]");
	}
	
	public void createDungeonHDDecoration(int x, int y, int z) {
		x -= minPoint.getBlockX();
		y -= minPoint.getBlockY();
		z -= minPoint.getBlockZ();
		lastEdit = new DungeonHDDecorate(x, y, z);
	}
	
	public void createDungeonSchematicDecoration(int x, int y, int z) {
		x -= minPoint.getBlockX();
		y -= minPoint.getBlockY();
		z -= minPoint.getBlockZ();
		lastEdit = new DungeonSchematicDecorate(x, y, z);
	}
	
	public void createTrigger(int nx, int ny, int nz) {
		final int x = nx - minPoint.getBlockX();
		final int y = ny - minPoint.getBlockY();
		final int z = nz - minPoint.getBlockZ();
		lastEdit = new DungeonPlaceholderObject();
		valueOperation = new AbstractMap.SimpleEntry<>("Trigger Type", (es, dobj, input) -> {
			String type = (String) input;
			switch (type.toLowerCase()) {
				case "location":
					lastEdit = new LocationTrigger(x, y, z);
					break;
				case "mobkill":
					lastEdit = new MobKillTrigger();
					break;
				case "interact":
					lastEdit = new InteractTrigger();
					break;
			}
		});
		msg.send(player, "&6=== Trigger Setup ===");
		msg.send(player, "&7+-> &aTrigger Type &7[&eInteract &7| &eMobKill &7| &eLocation&7]");
	}
	
	public void updateLastEdit(DungeonObject dobj) {
		lastEdit = dobj;
	}
	
	public void updateOperation() {
		valueOperation = lastEdit.getOperation();
		if (valueOperation == null) {
			msg.send(player, "&7-- &cFinished setup for this.");
			if (lastEdit instanceof TriggerAction) {
				lastEdit = ((TriggerAction) lastEdit).getParent();
			} else if (!(lastEdit instanceof DungeonPlaceholderObject)) {
				dungeonObjectList.add(lastEdit);
			} else {
				lastEdit = null;
			}
		} else {
			msg.send(player, "&7+-> &a" + valueOperation.getKey());
		}
	}
	
	public String getInputValue() {
		if (valueOperation == null) {
			return null;
		}
		return valueOperation.getKey();
	}
	
	public void inputValue(Object value) {
		try {
			valueOperation.getValue().accept(this, lastEdit, value);
			updateOperation();
		} catch (Exception e) {
			msg.send(player, "ERROR while setting [" + valueOperation.getKey() + "]!");
			db.log("ERROR IN INPUT -- Who[" + player.getName() + "]");
			e.printStackTrace();
		}
	}
	
	public String getDungeonName() {
		return dungeonName;
	}
}
