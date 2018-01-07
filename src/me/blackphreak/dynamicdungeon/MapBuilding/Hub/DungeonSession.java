package me.blackphreak.dynamicdungeon.MapBuilding.Hub;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.regions.Region;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.AbstractWorld;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.spawning.spawners.MythicSpawner;
import me.blackphreak.dynamicdungeon.DynamicDungeon;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.gb;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DungeonSession {
    private int sessionID = -1;
    private int dungeonID = -1;
    private int maxPlayers = 5;

    private Player sessionOwner = null;
    private EditSession session = null;
    private Region region = null;
    private Location latestCheckPoint = null,
            spawnLocation = null;
    private Location[] dungeonLocation = {null, null};
    private ConcurrentHashMap<Player, Location> enterLocation = new ConcurrentHashMap<>();
    private List<Player> whoPlaying = new ArrayList<>();
    private ConcurrentHashMap<Chunk, List<MythicSpawner>> spawnerTable = new ConcurrentHashMap<>();
    private List<ActiveMob> spawnedMobs = new ArrayList<>();
    private UUID uuid = UUID.randomUUID();

    public DungeonSession(Player p_caller, Region p_region, EditSession p_session, Location playerSpawnLocation, Location min, Location max) {
        this.sessionOwner = p_caller;
        this.session = p_session;
        this.region = p_region;
        this.sessionID = gb.dungeons.size();
        gb.dungeons.put(this.sessionID, this);
        this.latestCheckPoint = playerSpawnLocation;
        this.dungeonLocation[0] = min;
        this.dungeonLocation[1] = max;
    }

    public DungeonSession(Player p_caller, Region p_region, EditSession p_session, Location min, Location max) {
        this.sessionOwner = p_caller;
        this.session = p_session;
        this.region = p_region;
        this.sessionID = gb.dungeons.size();
        gb.dungeons.put(this.sessionID, this);
        this.dungeonLocation[0] = min;
        this.dungeonLocation[1] = max;
    }

    //init.
    public void setSpawnLocation(Location loc) {
        this.latestCheckPoint = loc;
        this.spawnLocation = loc;
    }

    public void setDungeonID(int _id) {
        this.dungeonID = _id;
    }

    public Location getDungeonMin() {
        return dungeonLocation[0];
    }

    public Location getDungeonMax() {
        return dungeonLocation[1];
    }

    public Player getSessionOwner() {
        return this.sessionOwner;
    }

    public void setSessionOwner(Player p) {
        this.sessionOwner = p;
    }

    public EditSession getSession() {
        return this.session;
    }

    public Region getRegion() {
        return this.region;
    }

    public int getSessionID() {
        return this.sessionID;
    }

    public int getDungeonID() {
        return this.dungeonID;
    }

    public List<Player> getWhoPlaying() {
        return this.whoPlaying;
    }

    public void enter(Player p) {
        this.whoPlaying.add(p);
    }

    public void left(Player p) {
        this.whoPlaying.remove(p);
    }

    public void killSession(Player p) {
        if (enterLocation.size() > 0)
            whoPlaying.forEach(v -> {
                gb.dungeonPlaying.remove(v);
                if (v.isOnline()) {
                    v.teleport(enterLocation.get(v));
                    v.sendMessage("Your Dungeon Session has been forced to kill by Admin: " + p.getName());
                }
            });
        whoPlaying.clear();
        gb.dungeons.remove(sessionID);
        this.session.undo(this.session);
    }

    public void killSession() {
        if (enterLocation.size() > 0)
            whoPlaying.forEach(v -> {
                gb.dungeonPlaying.remove(v);
                if (v.isOnline())
                    v.teleport(enterLocation.get(v));
            });
        whoPlaying.clear();
        this.session.undo(this.session);
    
        db.log("Dungeon Session["+uuid+"] with owner: "+this.sessionOwner.getName()+" has been killed.");
    }

    public void updateCheckPoint(Location location) {
        this.latestCheckPoint = location;
    }

    public Location getLatestCheckPoint() {
        return this.latestCheckPoint;
    }

    public void addSpawnersOnChunk(Chunk targetChunk, Location spawnLocation, List<MythicSpawner> spawners) {
        List<MythicSpawner> tLst = this.spawnerTable.getOrDefault(targetChunk, new ArrayList<>());
        tLst.addAll(spawners);
        this.spawnerTable.put(targetChunk, tLst);
    }

    public void addSpawnerOnChunk(Chunk targetChunk, Location spawnLocation, MythicSpawner spawner) {
        List<MythicSpawner> tLst = this.spawnerTable.getOrDefault(targetChunk, new ArrayList<>());
        AbstractLocation loc = new AbstractLocation((AbstractWorld) spawnLocation.getWorld(), spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ());
        spawner.setLocation(loc);
        tLst.add(spawner);
        this.spawnerTable.put(targetChunk, tLst);
    }

    public void removeSpawnersByChunk(Chunk targetChunk) {
        this.spawnerTable.remove(targetChunk);
    }

    public List<MythicSpawner> getSpawnersByChunk(Chunk targetChunk) {
        return this.spawnerTable.getOrDefault(targetChunk, new ArrayList<>());
    }

    public void addSpawnedMobs() {
    }

    public int maxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int tInt) {
        maxPlayers = tInt;
    }

    public void join(Player p) {
        if (whoPlaying.size() > maxPlayers) {
            p.sendMessage("You can't join this session due to max player reached.");
            return;
        }

//		p.sendMessage("Joining Dungeon Session [#" + sessionID + "] | Owner: " + sessionOwner.getName());

        //make a delayed teleport
        new BukkitRunnable() {
            @Override
            public void run() {
                enterLocation.put(p, p.getLocation());
                p.teleport(spawnLocation);
                enter(p);
                gb.dungeonPlaying.put(p, sessionID);

                if (gb.dungeonCreating.contains(sessionID))
                    gb.dungeonCreating.remove(session);
            }
        }.runTaskLater(DynamicDungeon.plugin, 40L);
    }

    public UUID getUuid() {
        return uuid;
    }
}
