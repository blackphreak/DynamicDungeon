package me.blackphreak.dynamicdungeon.MapBuilding.Hub;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.regions.Region;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitWorld;
import io.lumine.xikage.mythicmobs.spawning.spawners.MythicSpawner;
import me.blackphreak.dynamicdungeon.DynamicDungeon;
import me.blackphreak.dynamicdungeon.Messages.db;
import me.blackphreak.dynamicdungeon.Messages.msg;
import me.blackphreak.dynamicdungeon.Supports.HolographicDisplays.cHologram;
import me.blackphreak.dynamicdungeon.dungeonobject.trigger.DungeonTrigger;
import me.blackphreak.dynamicdungeon.gb;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
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
    private UUID uuid = UUID.randomUUID();
    private List<cHologram> holograms = new ArrayList<>(); // store all the cHologram for later remove.
    private List<EditSession> schematics = new ArrayList<>(); // decoration -> schematic, store the editsession for later remove.
    private List<DungeonTrigger> triggers = new ArrayList<>();
    private List<DungeonTrigger> rm_queue = new ArrayList<>();

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

    /*
    public void updateMobKillTrigger(MobKillTrigger mkt) {
        db.log(mkt.getName() + " : " + mkt.readCounter());
        mobKillTriggers.put(mkt.getName(), mkt);
    }
    */

    public Location getDgMinPt() {
        return dungeonLocation[0];
    }

    public Location getDgMaxPt() {
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

    public List<DungeonTrigger> getTriggers() {
        return triggers;
    }


    public void enter(Player p) {
        this.whoPlaying.add(p);
    }

    public void left(Player p) {
        this.whoPlaying.remove(p);
    }

    public void killSession(CommandSender p) {
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

        //remove holograms
        if (gb.hd != null)
            holograms.forEach(cHologram::delete);

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
        /*spawnedMobs.forEach(mob ->
        {
            mob.setDead();
            mob.setDespawned();
        });*/
        spawnerTable.values().forEach(list -> list.forEach(MythicSpawner::unloadSpawner));
        this.session.undo(this.session);

        db.log("Dungeon Session[" + uuid + "] with owner: " + this.sessionOwner.getName() + " has been killed.");
    }

    public void updateCheckPoint(Location location) {
        this.latestCheckPoint = location;
        whoPlaying.forEach(p -> msg.send(p, "Dungeon CheckPoint updated."));
    }

    public Location getLatestCheckPoint() {
        return this.latestCheckPoint;
    }

    // adder.
    public void addHologram(cHologram hg) {
        this.holograms.add(hg);
    }

    public void addDecorationSchematic(EditSession es) {
        schematics.add(es);
    }

    public void addSpawnersOnChunk(Chunk targetChunk, Location spawnLocation, List<MythicSpawner> spawners) {
        List<MythicSpawner> tLst = this.spawnerTable.getOrDefault(targetChunk, new ArrayList<>());
        tLst.addAll(spawners);
        this.spawnerTable.put(targetChunk, tLst);
    }

    /*
    public void addSpawnerOnChunk(Sign sign)
    {
        MythicSpawner spawner = MythicMobs.inst().getSpawnerManager()
                .getSpawnerByName(sign.getLine(1));

        if (spawner == null)
        {
            db.log("Failed to add spawner on chunk.");
            db.log("-+-- Error: Spawner not found! Please check!");
            db.log(" + Debug:");
            db.log(" +-- Sign Location: [" + spawnLocation.getX() + ", " + spawnLocation.getY() + ", " + spawnLocation.getZ() + "]");
            db.log(" +-- Spawner Name : [" + sign.getLine(1) + "]");
            db.logArr(" +-- Lines: ", sign.getLines());
            return;
        }

        addSpawnerOnChunk(sign.getChunk(), sign.getLocation(), spawner);
    }
    */

    public void addSpawnerOnChunk(String spawnerName, Location loc) {
        MythicSpawner spawner = MythicMobs.inst().getSpawnerManager().getSpawnerByName(spawnerName);

        if (spawner == null) {
            db.log("Failed to add spawner on chunk.");
            db.log("-+-- Error: Spawner not found! Please check!");
            db.log(" + Debug:");
            //db.log(" +-- Sign Location: [" + spawnLocation.getX() + ", " + spawnLocation.getY() + ", " + spawnLocation.getZ() + "]");
            db.log(" +-- Spawner Name : [" + spawnerName + "]");
            return;
        }
        addSpawnerOnChunk(loc.getChunk(), loc, spawner);
    }


    public void addSpawnerOnChunk(Chunk targetChunk, Location spawnLocation, MythicSpawner spawner) {
        List<MythicSpawner> tLst = this.spawnerTable.getOrDefault(targetChunk, new ArrayList<>());

        AbstractLocation loc = new AbstractLocation(
                new BukkitWorld(spawnLocation.getWorld()),
                spawnLocation.getX(),
                spawnLocation.getY(),
                spawnLocation.getZ()
        );
        db.tlog("Adding spawner[" + spawner.getName() + "] on Chunk @ [" + targetChunk.getX() + "," + targetChunk.getZ() + "]");
        spawner.setLocation(loc);
        tLst.add(spawner);
        this.spawnerTable.put(targetChunk, tLst);
    }

    public void addTrigger(DungeonTrigger dt) {
        triggers.add(dt);
    }
    
    /**
     * Calls when a trigger has done its job(s).
     * The trigger will be removed after all condition are done.
     * See: DungeonSession.removeTriggersInQueue()
     * @param dt: the target DungeonTrigger that wants to be removed.
     */
    public void addToTriggerRemoveQueue(DungeonTrigger dt)
    {
        rm_queue.add(dt);
    }
    
    /**
     * Calls when all condition & action are done.
     */
    public void removeTriggersInQueue()
    {
        triggers.removeAll(rm_queue);
        rm_queue.clear();
    }
    
    public void removeSpawnersByChunk(Chunk targetChunk) {
        this.spawnerTable.remove(targetChunk);
    }

    public List<MythicSpawner> getSpawnersByChunk(Chunk targetChunk) {
        return this.spawnerTable.getOrDefault(targetChunk, new ArrayList<>());
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

//		p.sendMessage("Joining Dungeon Session [#" + sessionID + "] | Owner: " + sessionOwner.getHoloName());

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
