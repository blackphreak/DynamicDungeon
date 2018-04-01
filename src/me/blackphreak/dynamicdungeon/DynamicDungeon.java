package me.blackphreak.dynamicdungeon;

import me.blackphreak.dynamicdungeon.Commands.CommandManager;
import me.blackphreak.dynamicdungeon.Listeners.PlayerInteractEventListener;
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
		this.getCommand("dynamicdungeon").setExecutor(new CommandManager());
		
		File dir = new File("plugins/DynamicDungeon");
		if (!dir.exists())
			dir.mkdir();
		
		dir = new File("plugins/DynamicDungeon/savedDungeons");
		if (!dir.exists())
			dir.mkdir();
		
		db.log("Registering Player Interact Event Listener...");
		Bukkit.getPluginManager().registerEvents(new PlayerInteractEventListener(), this);
		
//		db.log("Registering Chunk Load Event Listener...");
//		Bukkit.getPluginManager().registerEvents(new ChunkLoadEventListener(), this);
		
		db.log("Dynamic Dungeon has been enabled");
		
		// teleport all player to spawn
		Bukkit.getWorld("dungeonworld").getPlayers().forEach(
				p -> p.teleport(p.getBedSpawnLocation())
		);
		
		new gb();
	}
	
	public void onDisable()
	{
		//unload all dungeons
		gb.dungeons.forEach((k, v) -> v.killSession());
		plugin = null;
		db.log("Dynamic Dungeon has been disabled");
	}
}
