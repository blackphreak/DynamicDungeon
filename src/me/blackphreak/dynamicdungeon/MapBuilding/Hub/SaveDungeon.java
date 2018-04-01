package me.blackphreak.dynamicdungeon.MapBuilding.Hub;

import com.boydti.fawe.object.FawePlayer;
import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.Messages.msg;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class SaveDungeon
{
	public static void saveDungeon(Player p, String dungeonName)
	{
		try
		{
			// getting vector from we
			Region r = FawePlayer.wrap(p).getSelection();
			
			if (r == null)
			{
				msg.send(p, "You must select an area using AXE!!");
				return;
			}
			
			com.sk89q.worldedit.world.World world = r.getWorld();
			Vector min = r.getMinimumPoint();
			Vector max = r.getMaximumPoint();
			
			File file = new File("plugins/DynamicDungeon/savedDungeons/" + dungeonName + ".schematic");
			CuboidRegion region = new CuboidRegion(world, min, max);
			Schematic schem = new Schematic(region);
			schem.save(file, ClipboardFormat.SCHEMATIC);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			db.log("Exception caught. Please report to github.");
			e.printStackTrace();
		}
	}
}
