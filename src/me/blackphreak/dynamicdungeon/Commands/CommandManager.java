package me.blackphreak.dynamicdungeon.Commands;

import me.blackphreak.dynamicdungeon.MapBuilding.Builder;
import me.blackphreak.dynamicdungeon.MapBuilding.Hub.DungeonSession;
import me.blackphreak.dynamicdungeon.gb;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {
	public CommandManager() {
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getLabel().equalsIgnoreCase("dynamicdungeon")
				|| cmd.getLabel().equalsIgnoreCase("dd"))
		{
			if(!(sender instanceof Player)) {
				sender.sendMessage("You must using this command in-game");
				return false;
			}
			
			if(!sender.hasPermission("dynamicdungeon.admin") && !sender.isOp()) {
				return false;
			}
			
			switch (args[0].toLowerCase())
			{
				case "admin":
				{
					switch (args[1].toLowerCase())
					{
						case "build":
							Builder.build((Player) sender, args[2]);
							sender.sendMessage("building DungeonSession: " + args[2]);
							break;
						case "listsessions":
						case "lss":
							gb.listOutSessions((Player) sender);
							break;
						case "join":
							DungeonSession s = gb.dungeons.get(Integer.valueOf(args[2]));
							if (s == null)
								sender.sendMessage("Dungeon Session not found.");
							else
								s.join((Player) sender);
							break;
						case "destory":
						case "killsession":
							DungeonSession s1 = gb.dungeons.get(Integer.valueOf(args[2]));
							if (s1 == null)
								sender.sendMessage("Dungeon Session not found.");
							else
								s1.killSession((Player) sender);
						default:
							sender.sendMessage("Unknown command.");
							break;
					}
					break;
				}
				default:
					sender.sendMessage("Unknown command.");
					break;
			}
		}
		
		return true;
	}
}
