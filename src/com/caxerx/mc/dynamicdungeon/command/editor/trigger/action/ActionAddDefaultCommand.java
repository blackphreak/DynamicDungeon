package com.caxerx.mc.dynamicdungeon.command.editor.trigger.action;

import com.caxerx.mc.dynamicdungeon.command.CommandNode;
import com.caxerx.mc.dynamicdungeon.command.DefaultCommand;
import me.blackphreak.dynamicdungeon.dungeonobject.action.*;

public class ActionAddDefaultCommand extends DefaultCommand {
	
	public ActionAddDefaultCommand(CommandNode parent) {
		super(parent, "action", "dynamicdungeon.admin", "add a action to selected trigger", null);
		addSub(new ActionAddObjectCommand(this, "checkpoint", CheckPointAction.class));
		addSub(new ActionAddObjectCommand(this, "damage", DamageAction.class));
		addSub(new ActionAddObjectCommand(this, "message", MessageAction.class));
		addSub(new ActionAddObjectCommand(this, "trigger", TriggerAction.class));
		addSub(new ActionAddObjectCommand(this, "schematic", SchematicAction.class));
		addSub(new ActionAddObjectCommand(this, "hologram", HologramAction.class));
		addSub(new ActionAddObjectCommand(this, "teleport", TeleportAction.class));
	}
}
