package me.blackphreak.dynamicdungeon.MapBuilding;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.Mask2D;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.io.Closer;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.registry.WorldData;
import me.blackphreak.dynamicdungeon.DynamicDungeon;
import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.Messages.msg;
import me.blackphreak.dynamicdungeon.gb;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.primesoft.asyncworldedit.AsyncWorldEditMain;
import org.primesoft.asyncworldedit.api.blockPlacer.IBlockPlacer;
import org.primesoft.asyncworldedit.api.blockPlacer.IBlockPlacerListener;
import org.primesoft.asyncworldedit.api.blockPlacer.IJobEntryListener;
import org.primesoft.asyncworldedit.api.blockPlacer.entries.IJobEntry;
import org.primesoft.asyncworldedit.api.playerManager.IPlayerEntry;
import org.primesoft.asyncworldedit.api.utils.IAsyncCommand;
import org.primesoft.asyncworldedit.api.utils.IFuncParamEx;
import org.primesoft.asyncworldedit.api.worldedit.ICancelabeEditSession;
import org.primesoft.asyncworldedit.api.worldedit.IThreadSafeEditSession;
import org.primesoft.asyncworldedit.blockPlacer.entries.JobEntry;
import org.primesoft.asyncworldedit.worldedit.AsyncEditSessionFactory;
import org.primesoft.asyncworldedit.worldedit.ClipboardAsyncTask;

import javax.annotation.Nullable;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import static net.minecraft.server.v1_12_R1.NetworkManager.f;

public class Builder
{
	public Builder()
	{
	}
	
	public static DungeonSession build(Player sessionOwner, String fileNameWithoutExtension)
	{
		/*
		 * reference: https://www.spigotmc.org/threads/asyncworldedit-premium-paid.79081/page-10#post-961711
		 */
		CuboidClipboard ccb = null;
		World world = BukkitUtil.getLocalWorld(Bukkit.getWorld("dungeonWorld"));
		AsyncWorldEditMain awe = AsyncWorldEditMain.getInstance();
		IPlayerEntry ipe = awe.getPlayerManager().getConsolePlayer();
		
		try
		{
			File schematic = new File("plugins/DynamicDungeon/savedDungeons/" + fileNameWithoutExtension + ".schematic");
			ccb = SchematicFormat.MCEDIT.load(schematic);
			
//			ClipboardFormat format = ClipboardFormat.findByAlias("schematic");
//			Closer closer = Closer.create();
//			FileInputStream fis = closer.register(new FileInputStream(schematic));
//			BufferedInputStream bis = closer.register(new BufferedInputStream(fis));
//			ClipboardReader reader = format.getReader(bis);
//			
//			ccb = reader.read(world.getWorldData());
		}
		catch (Exception ex)
		{
			db.log("Error while loading schematic");
			ex.printStackTrace();
		}
		
		if (ccb == null)
		{
			db.log("CuboidClipboard is null!");
			return null;
		}
		
//		Location loc = gb.lastDungeonMaxLocation.add(ccb.getRegion().getWidth() + 20, 0, ccb.getRegion().getLength() + 20);
		Location loc = gb.lastDungeonMaxLocation.add(ccb.getWidth() + 20, 0, ccb.getLength() + 20);
//		Location max = loc.clone().add((double)ccb.getRegion().getWidth(), 0, (double)ccb.getRegion().getLength());
		loc.setWorld(Bukkit.getWorld("dungeonWorld"));
		Location max = loc.clone().add((double)ccb.getWidth(), 0, (double)ccb.getLength());
		CuboidSelection region = new CuboidSelection(loc.getWorld(), loc, max);
		
		AsyncEditSessionFactory factory = (AsyncEditSessionFactory) WorldEdit.getInstance().getEditSessionFactory();
		
		IThreadSafeEditSession tses = factory.getThreadSafeEditSession(
				world, //casting bukkitWorld -> com.sk89q.worldedit.world.World
				-1
		);
		IBlockPlacer placer = awe.getBlockPlacer(); //iawe
		/**
		 * prams: IThreadSafeEditSession, IPlayerEntry, String, IFuncParamEx<Integer, ICancelabeEditSession, MaxChangedBlocksException>);
		 * prams: IThreadSafeEditSession, IAsyncCommand
		 */
//		EditSession session = new EditSession(new BukkitWorld(loc.getWorld()), -1);
//		session.enableQueue();
//		session.setFastMode(true);
		
//		WorldEdit we = ((WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit")).getWorldEdit();
		
		/*IAsyncCommand asyncCommand = awe.getOperations().getChunkOperations().createPaste(ipe,
				new com.sk89q.worldedit.util.Location(session,
						loc.getBlockX(),
						loc.getBlockY(),
						loc.getBlockZ()
				),
				world,
				new Mask() {
					@Override
					public boolean test(Vector vector) {
						return true;
					}
					
					@Nullable
					@Override
					public Mask2D toMask2D() {
						return null;
					}
				},
				new ClipboardHolder(ccb, world.getWorldData()),
				false,
				true);
		placer.performAsAsyncJob(tses, asyncCommand);*/
		JobEntry job = new JobEntry(ipe, placer.getJobId(ipe), "DungeonCreation");
		IJobEntryListener lst2 = (iJobEntry -> {
			switch (iJobEntry.getStatus().getSeqNumber())
			{
				case 3:
					msg.send(sessionOwner, "processing...");
					break;
				case 4:
//					new BukkitRunnable() {
//						@Override
//						public void run() {
////				sessionOwner.teleport(loc);
////				session.flushQueue();
//							tses.flushQueue();
//							tses.doUndo();
//						}
//					}.runTaskLater(DynamicDungeon.plugin, 20L);
					
					msg.send(sessionOwner, "done.");
					break;
				default:
					msg.send(sessionOwner, "default...");
					break;
			}
		});
		
		tses.addAsync(job);
		tses.setFastMode(true);
		
		
		final IBlockPlacerListener listener = new IBlockPlacerListener() {
			@Override
			public void jobAdded(IJobEntry iJobEntry) {
				db.log("job added " + iJobEntry.getStatusString() + " | name: " + iJobEntry.getName());
				iJobEntry.addStateChangedListener(lst2);
			}
			
			@Override
			public void jobRemoved(IJobEntry iJobEntry) {
			
			}
		};
		
		placer.addListener(listener);
		
		final CuboidClipboard f_ccb = ccb;
		placer.performAsAsyncJob(tses, ipe,
				"ABC",
				iCancelabeEditSession -> {
//					f_ccb.place((EditSession) tses, BukkitUtil.toVector(loc), false);
//					iCancelabeEditSession.getJobId();
//					f_ccb.place(tses, BukkitUtil.toVector(loc), true);
					new ClipboardAsyncTask(f_ccb, (EditSession) tses, ipe, "DungeonCreation", placer, job)
					{
						@Override
						public void task(CuboidClipboard cuboidClipboard) throws MaxChangedBlocksException
						{
							f_ccb.place((EditSession) tses, BukkitUtil.toVector(loc), false);
						}
					}.run();
					
					return tses.getBlockChangeCount();
				}
		);
		
		return null;
	}
}
