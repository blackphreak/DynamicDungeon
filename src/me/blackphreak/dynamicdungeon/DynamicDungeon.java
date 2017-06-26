package me.blackphreak.dynamicdungeon;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import me.blackphreak.dynamicdungeon.Commands.CommandManager;
import me.blackphreak.dynamicdungeon.Listeners.PlayerInteractEventListener;
import me.blackphreak.dynamicdungeon.Messages.db;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class DynamicDungeon extends JavaPlugin {
	public static DynamicDungeon plugin = null;
	
	public DynamicDungeon() {
	}
	
	public void onEnable() {
		plugin = this;
		this.getCommand("dynamicdungeon").setExecutor(new CommandManager());
		this.getServer().getPluginManager().registerEvents(new PlayerInteractEventListener(), this);
		
		File p = new File("plugins/DynamicDungeon");
		if (!p.exists())
			p.mkdir();
		
		p = new File("plugins/DynamicDungeon/savedDungeons");
		if (!p.exists())
			p.mkdir();
		
//		File dgs = new File("plugins/DynamicDungeon/savedDungeons.yml");
//		if(!dgs.exists()) {
//			plugin.saveResource("savedDungeons.yml", false);
//		}
		
//		gb.savedDungeons = YamlConfiguration.loadConfiguration(dgs);
		db.log("Dynamic Dungeon has been enabled");
	}
	
	public void onDisable()
	{
		//unload all dungeons
		gb.dungeons.forEach((k, v) -> {
			v.killSession();
		});
		plugin = null;
		db.log("Dynamic Dungeon has been disabled");
	}
}
