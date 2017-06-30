package me.blackphreak.dynamicdungeon.mapcreator;

import com.google.gson.Gson;
import me.blackphreak.dynamicdungeon.Messages.db;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

/**
 * Created by caxerx on 2017/6/30.
 */
public class SignParser {
    public static DungeonSign parseFromSign(Block sign) {
        if (sign.getType() == Material.SIGN_POST || sign.getType() == Material.WALL_SIGN) {
            String firstline = ((Sign) sign.getState()).getLine(0);
            switch (firstline.toLowerCase()) {
                case "[dg_spawnpoint]":
                    return new DungeonSign(sign.getLocation());
                case "[dg_spawner]":
                    return new DungeonSign(sign.getLocation(), "spawner:" + ((Sign) sign.getState()).getLine(1));
                default:
                    return null;
            }
        }
        return null;
    }

    public static DungeonSign parseFromString(String string) {
        return new Gson().fromJson(string, DungeonSign.class);
    }
}
