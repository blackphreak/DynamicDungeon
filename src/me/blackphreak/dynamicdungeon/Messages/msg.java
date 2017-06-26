package me.blackphreak.dynamicdungeon.Messages;

import org.bukkit.entity.Player;

public class msg
{
	public static void send(Player p, String message)
	{
		p.sendMessage("§c>> " + message);
	}
}
