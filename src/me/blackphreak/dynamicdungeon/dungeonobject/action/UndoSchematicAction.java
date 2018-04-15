package me.blackphreak.dynamicdungeon.dungeonobject.action;

import com.sk89q.worldedit.EditSession;
import me.blackphreak.dynamicdungeon.MapBuilding.DungeonSession;
import me.blackphreak.dynamicdungeon.dungeonobject.ActionNeeded;
import me.blackphreak.dynamicdungeon.dungeonobject.DDField;

public class UndoSchematicAction extends DungeonAction {
	@DDField(name = "Undo Name")
	private String undoName;
	
	@Override
	public void action(DungeonSession dg, ActionNeeded needed) {
		EditSession session = dg.getSchematicSession(undoName);
		if (session == null)
			return;
		
		session.enableQueue();
		session.undo(session);
		session.flushQueue();
		
		dg.removeSchematicSession(undoName);
	}
}

