package me.blackphreak.dynamicdungeon.MapBuilding.Editor;

import com.google.gson.Gson;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.Region;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.dungeonobject.DungeonObject;
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
	private String prefix = "";
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
	
	/*public void createDungeonExit(double x, double y, double z) {
		x -= minPoint.getBlockX();
		y -= minPoint.getBlockY();
		z -= minPoint.getBlockZ();
		lastEdit = new DungeonLocation(x, y, z);
		updateOperation();
	}
	
	public void createDungeonSpawn(double x, double y, double z) {
		x -= minPoint.getBlockX();
		y -= minPoint.getBlockY();
		z -= minPoint.getBlockZ();
		lastEdit = new DungeonSpawn(x, y, z);
		updateOperation();
	}
	
	public void createDungeonMob(double x, double y, double z) {
		x -= minPoint.getBlockX();
		y -= minPoint.getBlockY();
		z -= minPoint.getBlockZ();
		lastEdit = new DungeonMobSpawner(x, y, z);
		updateOperation();
	}*/
	
	/*
	public void createDungeonDecoration(double x, double y, double z) {
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
	}*/
	
//	public void createDungeonHDDecoration(double x, double y, double z) {
//		x -= minPoint.getBlockX();
//		y -= minPoint.getBlockY();
//		z -= minPoint.getBlockZ();
//		DungeonHologramDecorate obj = new DungeonHologramDecorate();
//		obj.setLocation(new DungeonLocation(x, y, z));
//		lastEdit = obj;
//	}
//
//	public void createDungeonSchematicDecoration(double x, double y, double z) {
//		x -= minPoint.getBlockX();
//		y -= minPoint.getBlockY();
//		z -= minPoint.getBlockZ();
//		DungeonSchematicDecorate obj = new DungeonSchematicDecorate();
//		obj.setLocation(new DungeonLocation(x, y, z));
//		lastEdit = obj;
//	}
	
	/*public void createTrigger(double nx, double ny, double nz) {
		final double x = nx - minPoint.getBlockX();
		final double y = ny - minPoint.getBlockY();
		final double z = nz - minPoint.getBlockZ();
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
		msg.send(player, "&7[ &6"+lastEdit.getType()+" Setup &7]");
		msg.send(player, "&7+-> &aTrigger Type &7[&eInteract &7| &eMobKill &7| &eLocation&7]");
	}*/
	
	/*public void updateLastEdit(DungeonObject dobj) {
		lastEdit = dobj;
	}
	
	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}
	
	public void updateOperation() {
		valueOperation = lastEdit.getOperation();
		if (valueOperation == null) {
			msg.send(player, "&7-- &cFinished setup for &6"+lastEdit.getName()+"&7[&6"+lastEdit.getType()+"&7].");
			if (lastEdit instanceof TriggerAction) {
				lastEdit = ((TriggerAction) lastEdit).getParent();
			} else if (!(lastEdit instanceof DungeonPlaceholderObject)) {
				dungeonObjectList.add(lastEdit);
			} else {
				lastEdit = null;
			}
		} else {
			msg.send(player, prefix + "&7+-> &a" + valueOperation.getKey());
		}
	}*/
	
	public Player getPlayer()
	{
		return player;
	}
	
	/*public DungeonObject getLastEdit()
	{
		return lastEdit;
	}
	
	public String getInputValue() {
		if (valueOperation == null) {
			return null;
		}
		return valueOperation.getKey();
	}*/
	
	/*public void inputValue(Object value) {
		try {
			valueOperation.getValue().accept(this, lastEdit, value);
			updateOperation();
		} catch (Exception e) {
			msg.send(player, "ERROR while setting &7[&6" + valueOperation.getKey() + "&7]!");
			db.log("ERROR IN INPUT -- Who[" + player.getName() + "]");
			e.printStackTrace();
		}
	}*/
	
//	public String getDungeonName() {
//		return dungeonName;
//	}
}
