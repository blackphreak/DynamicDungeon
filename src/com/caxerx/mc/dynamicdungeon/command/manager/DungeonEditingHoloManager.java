package com.caxerx.mc.dynamicdungeon.command.manager;

import com.boydti.fawe.FaweAPI;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.sk89q.worldedit.Vector;
import me.blackphreak.dynamicdungeon.DynamicDungeon;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.dungeonobject.OffsetLocation;
import me.blackphreak.dynamicdungeon.dungeonobject.trigger.DungeonTrigger;
import me.blackphreak.dynamicdungeon.dungeonobject.trigger.InteractTrigger;
import me.blackphreak.dynamicdungeon.dungeonobject.trigger.LocationTrigger;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * class DungeonEditingHoloManager:
 * make hologram to mark all the triggers on edit.
 */
public class DungeonEditingHoloManager
{
	private static HashMap<Player, HashMap<String, Hologram>> _playerHoloList = new HashMap<>();
	
	public static void createHoloForTrigger(Player p, DungeonTrigger dt)
	{
		Location loc;
		
		final Vector minPt = FaweAPI.wrapPlayer(p).getSelection().getMinimumPoint();
		
		if (dt instanceof InteractTrigger)
		{
			final OffsetLocation location = ((InteractTrigger) dt).getLocation();
			loc = location.add(minPt.getX(), minPt.getY(), minPt.getZ()).midPt().toBukkitLoc();
		}
		else if (dt instanceof LocationTrigger)
		{
//			new OffsetLocation(location.getX(), location.getY(), location.getZ())
			final OffsetLocation location = ((LocationTrigger) dt).getLocation().clone();
			db.log("loc: " + location.toString());
			loc = location.add(minPt.getX(), minPt.getY(), minPt.getZ()).midPt().toBukkitLoc();
			db.log("loc: " + location.toString());
		}
		else
			loc = p.getLocation();
		
		Hologram hg = HologramsAPI.createHologram(DynamicDungeon.plugin, loc.add(0, 2.0, 0));
		hg.appendTextLine("§6Tri§7-§6Name§7: §e" + dt.getTriggerName());
		hg.appendTextLine(dt.toString());
		
		HashMap<String, Hologram> t_holoList = _playerHoloList.getOrDefault(p, new HashMap<>());
		t_holoList.put(dt.getTriggerName(), hg);
		
		_playerHoloList.put(p, t_holoList);
	}
	
	// TODO: add this after removeTriggerCommand is added.
	public static void removeHoloForTrigger(Player p, String triggerName)
	{
		if (_playerHoloList.get(p) != null)
			_playerHoloList.get(p).get(triggerName).delete();
		
		_playerHoloList.get(p).remove(triggerName);
	}
	
	public static void clearHolos(Player p)
	{
		if (_playerHoloList.get(p) != null)
			_playerHoloList.get(p).forEach((k, v) -> v.delete());
		
		_playerHoloList.remove(p);
	}
}
