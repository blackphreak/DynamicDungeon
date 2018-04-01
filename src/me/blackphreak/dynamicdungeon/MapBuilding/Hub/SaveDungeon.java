package me.blackphreak.dynamicdungeon.MapBuilding.Hub;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.object.FawePlayer;
import com.boydti.fawe.object.schematic.Schematic;
import com.google.gson.Gson;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockID;
import com.sk89q.worldedit.blocks.SignBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import jdk.nashorn.internal.ir.Block;
import me.blackphreak.dynamicdungeon.DynamicDungeon;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.Messages.msg;
import me.blackphreak.dynamicdungeon.gb;
import org.apache.commons.io.FileUtils;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

public class SaveDungeon {
    public static void saveDungeon(Player p, String dungeonName) {
        try {
            // getting vector from we
            Region r = FawePlayer.wrap(p).getSelection();

            if (r == null) {
                msg.send(p, "You must select an area using AXE!!");
                return;
            }

            BukkitWorld world = (BukkitWorld) r.getWorld();
            Vector min = r.getMinimumPoint();
            Vector max = r.getMaximumPoint();

            File file = new File("plugins/DynamicDungeon/savedDungeons/" + dungeonName + ".schematic");
            CuboidRegion region = new CuboidRegion(world, min, max);

            Iterator<BlockVector> iter = region.iterator();
            List<SignLocation> signPlace = new ArrayList<>();

            BlockVector blockLoc;

            while (iter.hasNext()) {
                blockLoc = iter.next();
                BaseBlock block = world.getBlock(blockLoc);
                if (block.getType() == BlockID.WALL_SIGN || block.getType() == BlockID.SIGN_POST) {
                    signPlace.add(new SignLocation(blockLoc.getBlockX() - min.getBlockX(), blockLoc.getBlockY() - min.getBlockY(), blockLoc.getBlockZ() - min.getBlockZ()));
                }
            }

            FileUtils.writeStringToFile(new File("plugins/DynamicDungeon/savedDungeons/" + dungeonName + ".json"), new Gson().toJson(signPlace), Charset.defaultCharset());

            Schematic schem = new Schematic(region);
            schem.save(file, ClipboardFormat.SCHEMATIC);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            db.log("Exception caught. Please report to github.");
            e.printStackTrace();
        }
    }
}
