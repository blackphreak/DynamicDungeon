package me.blackphreak.dynamicdungeon.dungeonobject.action;

import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.dungeonobject.ActionNeeded;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandAction extends DungeonAction {
	@DDField(name = "Send command as player?")
	private boolean sendAsPlayer;
	
	/**
	 * without "/"
	 * use @p to replace the player name, if needed.
	 */
	@DDField(name = "Command")
	private String command;
	
	@Override
	public void action(DungeonSession dg, ActionNeeded needed) {
		Player p = needed.getWhoTrigger();
		command = command.replaceAll("@p", p.getName());
		if (sendAsPlayer) {
			p.performCommand(command);
		} else {
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
		}
	}
	
	@Override
	public String toString() {
		return String.format("[Ac-Command] Send as Player: %s, Cmd: %s", sendAsPlayer, command);
	}
}
