package me.blackphreak.dynamicdungeon.mapcreator;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.BukkitServerInterface;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.schematic.SchematicFormat;
import me.blackphreak.dynamicdungeon.DynamicDungeon;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.primesoft.asyncworldedit.AsyncWorldEditMain;
import org.primesoft.asyncworldedit.api.playerManager.IPlayerEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

/**
 * Created by caxerx on 2017/6/30.
 */
public class MapCreateSession {
    private final String dungeonName;
    private Player owner;
    private Plugin plugin = DynamicDungeon.plugin;

    /*
    static HashMap<IPlayerEntry, Runnable> jobs = new HashMap<>();

    static {

        IJobEntryListener jobListener = iJobEntry -> {
            if (iJobEntry.getStatus().getSeqNumber() == 4) {

            }
        };


        IBlockPlacerListener listener = new IBlockPlacerListener() {
            public void jobAdded(IJobEntry job) {
                if (jobs.containsKey(job.getPlayer())) {
                    job.addStateChangedListener(jobListener);
                }
            }

            public void jobRemoved(IJobEntry job) {
                if (jobs.containsKey(job.getPlayer())) {
                    jobs.get(job.getPlayer()).run();
                    job.removeStateChangedListener(jobListener);
                }
            }
        };

        AsyncWorldEditMain.getInstance().getBlockPlacer().addListener(listener);

    }
*/

    public MapCreateSession(Player owner, String dungeonName) {
        this.owner = owner;
        this.dungeonName = dungeonName;
    }

    public void create() {
        WorldEditPlugin worldedit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        BukkitServerInterface serverInterface = new BukkitServerInterface(worldedit, plugin.getServer());
        BukkitPlayer worldeditPlayer = new BukkitPlayer(worldedit, serverInterface, owner);
        BukkitWorld worldeditWorld = new BukkitWorld(owner.getWorld());
        if (worldedit.getSelection(owner) instanceof CuboidSelection) {
            CuboidSelection selection = (CuboidSelection) worldedit.getSelection(owner);
            Vector bukkitMin = selection.getNativeMinimumPoint();
            Vector bukkitMax = selection.getNativeMaximumPoint();
            CuboidRegion region = new CuboidRegion(worldeditWorld, new Vector(bukkitMin.getX(), bukkitMin.getY(), bukkitMin.getZ()), new Vector(bukkitMax.getX(), bukkitMax.getY(), bukkitMax.getZ()));
            World bukkitworld = owner.getWorld();
            Iterator<BlockVector> iter = region.iterator();
            ArrayList<String> signlist = new ArrayList<>();
            while (iter.hasNext()) {
                BlockVector block = iter.next();
                Block bukkitBlock = bukkitworld.getBlockAt(block.getBlockX(), block.getBlockY(), block.getBlockZ());
                if (bukkitBlock.getType() == Material.WALL_SIGN || bukkitBlock.getType() == Material.SIGN_POST) {
                    DungeonSign parsedSignBlock = SignParser.parseFromSign(bukkitBlock);
                    if (parsedSignBlock != null) {
                        signlist.add(parsedSignBlock.toString());
                    }
                }
            }

            try {
                FileUtils.writeStringToFile(new File(plugin.getDataFolder() + "/savedDungeons/" + dungeonName + ".json"), signlist.toString());


/*
            FileInputStream inputStream = FileUtils.openInputStream(schematic);
            Clipboard clipboard = ClipboardFormat.SCHEMATIC.getReader(inputStream).read(weWorldData);
            inputStream.close();

            ClipboardHolder clipboardHolder = new ClipboardHolder(clipboard, weWorldData);
*/
                IPlayerEntry awePlayer = AsyncWorldEditMain.getInstance().getPlayerManager().createFakePlayer(dungeonName, UUID.randomUUID());
                File schemFile = new File(plugin.getDataFolder() + "/savedDungeons/" + dungeonName + ".schematic");
                //ClipboardHolder clipboard = worldedit.getSession(owner).getClipboard();
                CuboidClipboard clipboard = new CuboidClipboard(new Vector(region.getWidth(), region.getHeight(), region.getLength()), region.getMinimumPoint());
                EditSession session = WorldEdit.getInstance().getEditSessionFactory().getEditSession(worldeditWorld, -1);
                clipboard.copy(session, region);
                SchematicFormat.MCEDIT.save(clipboard, schemFile);

            } catch (Exception e) {
                e.printStackTrace();
            }
/*
                ClipboardWriter writer = ClipboardFormat.SCHEMATIC.getWriter(FileUtils.openOutputStream(schemFile));
                IAsyncCommand action = AsyncWorldEditMain.getInstance().getOperations().getChunkOperations().createCopy(awePlayer, region, null, clipboard);
                IAsyncEditSessionFactory asyncEditSessionFactory = (IAsyncEditSessionFactory) WorldEdit.getInstance().getEditSessionFactory();
                IThreadSafeEditSession session = asyncEditSessionFactory.getThreadSafeEditSession(worldeditWorld, -1);
                AsyncWorldEditMain.getInstance().getBlockPlacer().performAsAsyncJob(session, awePlayer, "name", action);
                Runnable runnable = () -> {
                    try {
                        writer.write(clipboard, worldeditWorld.getWorldData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                };
*/

        }

        //
    }
}
