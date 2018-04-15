package me.blackphreak.dynamicdungeon.dungeonobject;

import lombok.Getter;
import org.bukkit.entity.Player;

public class ActionNeeded {
	@Getter
	private OffsetLocation location;
	
	@Getter
	private Player whoTrigger;
	
	@Getter
	private String previousTrigger;
	
	public ActionNeeded(Player whoTrigger, OffsetLocation loc) {
		this.whoTrigger = whoTrigger;
		this.location = loc;
	}
	
	public ActionNeeded setLocation(OffsetLocation loc)
	{
		this.location = loc;
		return this;
	}
	
	public ActionNeeded setWhoTrigger(Player who)
	{
		this.whoTrigger = whoTrigger;
		return this;
	}
	
	public ActionNeeded setPreviousTrigger(String name)
	{
		this.previousTrigger = name;
		return this;
	}
}
