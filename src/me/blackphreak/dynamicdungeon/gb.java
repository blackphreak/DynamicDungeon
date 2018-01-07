package me.blackphreak.dynamicdungeon;

import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class gb {
	public static boolean isDebugging = false;
	public static ConcurrentHashMap<Integer, DungeonSession> dungeons = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<Player, Integer> dungeonPlaying = new ConcurrentHashMap<>();
	public static List</*SessionID*/ Integer> dungeonCreating = new ArrayList<>();
	public static Location nextDungeonLocation = new Location(Bukkit.getWorld("dungeonWorld"), 50, 90, 50);
	
	public gb() {
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
		return (sessionID = dungeonPlaying.getOrDefault(p, -1)) > -1 ? dungeons.getOrDefault(sessionID, null) :null;
	}
	
	public static int getPlayingIDbyPlayer(Player p)
	{
		return gb.dungeonPlaying.getOrDefault(p, -1);
	}
}
