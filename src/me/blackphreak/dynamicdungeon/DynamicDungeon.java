package me.blackphreak.dynamicdungeon;

import com.caxerx.mc.dynamicdungeon.command.CommandHandler;
import com.caxerx.mc.dynamicdungeon.command.editor.EditorDefaultCommand;
import com.caxerx.mc.dynamicdungeon.command.manager.DungeonEditingManager;
import com.caxerx.mc.dynamicdungeon.command.manager.DungeonManager;
import com.caxerx.mc.lib.userinput.InputManager;
import me.blackphreak.dynamicdungeon.Command.Core.DefaultCoreCommand;
import me.blackphreak.dynamicdungeon.Listeners.MythicMobDeadthEventListener;
import me.blackphreak.dynamicdungeon.Listeners.PlayerInteractEventListener;
import me.blackphreak.dynamicdungeon.Listeners.PlayerMoveEventListener;
import me.blackphreak.dynamicdungeon.MapBuilding.Editor.DungeonEditSessionManager;
import me.blackphreak.dynamicdungeon.Messages.db;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class DynamicDungeon extends JavaPlugin {
    public static DynamicDungeon plugin = null;

    public DynamicDungeon() {
    }

    public void onEnable() {
        plugin = this;
        //this.getCommand("dynamicdungeon").setExecutor(new CommandManager());
        CommandHandler commandHandler = new CommandHandler(this);
        commandHandler.registerCommand("dd", new DefaultCoreCommand());
        commandHandler.registerCommand("dde", new EditorDefaultCommand());

        File dir = new File("plugins/DynamicDungeon");
        if (!dir.exists())
            dir.mkdir();

        dir = new File("plugins/DynamicDungeon/savedDungeons");
        if (!dir.exists())
            dir.mkdir();

        DungeonManager.INSTANCE.readFromFile();

        Bukkit.getPluginManager().registerEvents(new PlayerInteractEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new MythicMobDeadthEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveEventListener(), this);
        Bukkit.getPluginManager().registerEvents(DungeonEditSessionManager.getInstance(), this);
        Bukkit.getPluginManager().registerEvents(InputManager.INSTANCE, this);
        Bukkit.getPluginManager().registerEvents(DungeonEditingManager.getInstnace(), this);

//		db.log("Registering Chunk Load Event Listener...");
//		Bukkit.getPluginManager().registerEvents(new ChunkLoadEventListener(), this);

        db.log("Dynamic Dungeon has been enabled");

        // teleport all player to spawn
        Bukkit.getWorld("dungeonworld").getPlayers().forEach(
                p -> p.teleport(p.getBedSpawnLocation())
        );

        new gb();
    }

    public void onDisable() {
        //unload all dungeons
        gb.dungeons.forEach((k, v) -> v.killSession());
        plugin = null;
        db.log("Dynamic Dungeon has been disabled");
    }
}
