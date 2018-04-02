package me.blackphreak.dynamicdungeon;

import com.google.gson.Gson;
import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.Messages.db;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class gb {
    public static final String dgWorldName = "dungeonWorld"; // TODO: change in config.yml
    public static final World dgWorld = Bukkit.getWorld(dgWorldName);
    public static final int gap = 100; // indicates the gap between different dungeons. // TODO: change in config.yml
    public static final String dataPath = "plugins/DynamicDungeon/savedDungeons/";
    
    
    public static boolean isDebugging = true; // TODO: change in config.yml
    public static ConcurrentHashMap<Integer, DungeonSession> dungeons = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Player, Integer> dungeonPlaying = new ConcurrentHashMap<>();
    public static List</*SessionID*/ Integer> dungeonCreating = new ArrayList<>();
    public static Location nextDungeonLocation = new Location(dgWorld, 50, 90, 50);
    

    public static Plugin hd = null;

    public gb() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if ((hd = Bukkit.getPluginManager().getPlugin("HolographicDisplays")) != null) {
                    db.log("HolographicDisplays Supports has been enabled.");
                } else
                    db.log("HolographicDisplays not found, the Supports has been disabled.");
            }
        }.runTaskLater(DynamicDungeon.plugin, 40L);
    }

    public static void listOutSessions(Player p) {
        p.sendMessage("Here's the existing DungeonSessions:");
        p.sendMessage(" ");
        dungeons.forEach((k, v) -> {
            p.sendMessage("sessionID: " + k + " | dgID: " + v.getDungeonID() + " | sessionOwner: " + (v.getSessionOwner() == null ? "==NULL==" : v.getSessionOwner().getName()));
            p.sendMessage("      posFrom: " + math.round(v.getDungeonMin().getX()) + ", " + math.round(v.getDungeonMin().getY()) + ", " + math.round(v.getDungeonMin().getZ()));
            p.sendMessage("      posTo  : " + math.round(v.getDungeonMax().getX()) + ", " + math.round(v.getDungeonMax().getY()) + ", " + math.round(v.getDungeonMax().getZ()));
            p.sendMessage(" ============================= ");
        });
    }

    public static DungeonSession getDungeonSessionByPlayer(Player p) {
        int sessionID;
        return (sessionID = dungeonPlaying.getOrDefault(p, -1)) > -1 ? dungeons.getOrDefault(sessionID, null) : null;
    }

    public static int getPlayingIDbyPlayer(Player p) {
        return gb.dungeonPlaying.getOrDefault(p, -1);
    }

    static Gson gson = new Gson();

    public static String toGsonString(Object o) {
        return gson.toJson(o);
    }

}
