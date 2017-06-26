package me.blackphreak.dynamicdungeon.MapBuilding;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.schematic.SchematicFormat;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.spawning.spawners.SpawnerManager;
import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.MapBuilding.Hub.SessionCreationDoneEvent;
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
import org.primesoft.asyncworldedit.AsyncWorldEditBukkit;
import org.primesoft.asyncworldedit.api.blockPlacer.IBlockPlacer;
import org.primesoft.asyncworldedit.api.blockPlacer.IJobEntryListener;
import org.primesoft.asyncworldedit.api.playerManager.IPlayerEntry;
import org.primesoft.asyncworldedit.blockPlacer.entries.JobEntry;
import org.primesoft.asyncworldedit.worldedit.ClipboardAsyncTask;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

public class Builder {
	public Builder() {
	}
	
	public static DungeonSession build(Player sessionOwner, String fileNameWithoutExtension)
	{
		File schematic = new File("plugins/DynamicDungeon/savedDungeons/" + fileNameWithoutExtension + ".schematic");
		
		try
		{
			CuboidClipboard ccb = SchematicFormat.MCEDIT.load(schematic);
			
			Location loc = gb.lastDungeonMaxLocation.add(ccb.getWidth() + 20, 0, ccb.getLength() + 20);
//			db.log("ccb: " + ccb.getWidth() + " | " + ccb.getLength() + " | " + ccb.getHeight());
//			db.log("loc: " + loc.getX() + " | " + loc.getY() + " | " + loc.getZ());
			Location max = loc.clone().add((double)ccb.getWidth(), 0, (double)ccb.getLength());
//			db.log("loc: " + loc.getX() + " | " + loc.getY() + " | " + loc.getZ());
//			db.log("max: " + max.getX() + " | " + max.getY() + " | " + max.getZ());
			CuboidSelection region = new CuboidSelection(loc.getWorld(), loc, max);
			
			EditSession session = new EditSession(new BukkitWorld(loc.getWorld()), -1);
			session.enableQueue();
			
			IBlockPlacer placer = AsyncWorldEditBukkit.getInstance().getBlockPlacer();
			IPlayerEntry iPlayer = AsyncWorldEditBukkit.getInstance().getPlayerManager().getConsolePlayer();
			JobEntry job = new JobEntry(iPlayer, placer.getJobId(iPlayer), "DungeonCreation");
			DungeonSession dg = new DungeonSession(sessionOwner, region, session, loc, max);
			
			//creating a BlockPlacing task.
			new ClipboardAsyncTask(ccb, session, iPlayer, "DungeonCreation", placer, job)
			{
				@Override
				public void task(CuboidClipboard cuboidClipboard) throws MaxChangedBlocksException
				{
					ccb.place(session, BukkitUtil.toVector(loc), true);
				}
			}.run();
			
			//add listener for the session owner
			msg.send(sessionOwner, "Preparing a DungeonSession for you...");
			IJobEntryListener jobListener = iJobEntry -> {
				switch (iJobEntry.getStatus().getSeqNumber())
				{
					case 3: //placing
						msg.send(sessionOwner, "Creating a DungeonSession with sessionID: " + dg.getSessionID());
						break;
					case 4: //done
						msg.send(sessionOwner, "DungeonSession Preparation Finished.");
						msg.send(sessionOwner, "Teleporting to your DungeonSession...");
						SessionCreationDoneEvent de = () ->
						{
							dg.join(sessionOwner);
						};
						de.done(); //make it works
						break;
					default:
						msg.send(sessionOwner, "Your DungeonSession will be created soon. Please wait...");
						break;
				}
			};
			job.addStateChangedListener(jobListener);
			
			session.flushQueue();
			
			boolean spawnPointSign = false;
			
			//get all the chunk inside the area.
			World world = loc.getWorld();
			int minX = loc.getChunk().getX();
			int mixZ = loc.getChunk().getZ();
			int maxX = max.getWorld().getChunkAt(max).getX();
			int maxZ = max.getWorld().getChunkAt(max).getZ();
			
			Collection<Chunk> chunks = new HashSet<>();
//			db.log(">x: " + (minX > maxX ? maxX : minX) + " | " + (minX > maxX ? minX : maxX));
			for(int x = (minX > maxX ? maxX : minX); x <= (minX > maxX ? minX : maxX); x++)
			{
//				db.log(">z: " + (mixZ > maxZ ? maxZ : mixZ) + " | " + (mixZ > maxZ ? mixZ : maxZ));
				for(int z = (mixZ > maxZ ? maxZ : mixZ); z <= (mixZ > maxZ ? mixZ : maxZ); z++)
				{
					Chunk chunk = world.getChunkAt(x, z);
					chunks.add(chunk);
				}
			}
			
			for (Chunk chunk : chunks)
			{
				for (BlockState blockState : chunk.getTileEntities())
				{
					db.log("BlockState: " + blockState.getType().name() + " | " + blockState.getLocation().toString());
					if (blockState instanceof Sign)
					{
//						db.log("sign found.: " + blockState.getLocation().toString());
						Sign sign = (Sign) blockState;
						if (sign.getLine(0).toLowerCase().equals("[dg_spawner]")) {
							dg.addSpawnerOnChunk(sign.getChunk(), sign.getLocation(), (new SpawnerManager(MythicMobs.inst())).getSpawnerByName(sign.getLine(1)));
							blockState.getBlock().setType(Material.AIR);
						}
						else if (!spawnPointSign && sign.getLine(0).toLowerCase().equals("[dg_spawnpoint]"))
						{
							dg.setSpawnLocation(sign.getLocation());
							spawnPointSign = true;
							blockState.getBlock().setType(Material.AIR);
						}
					}
				}
				
			}
			
			if(!spawnPointSign)
			{
				msg.send(sessionOwner, "Please contact administrator to fix this [Error: Builder->SLNF]");
				throw new Exception("Dungeon creation error, Spawn Location not found!");
			}
			
			return dg;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
}
