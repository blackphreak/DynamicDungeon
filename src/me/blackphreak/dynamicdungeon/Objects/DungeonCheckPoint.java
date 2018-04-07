package me.blackphreak.dynamicdungeon.Objects;

import me.blackphreak.dynamicdungeon.MapBuilding.Editor.DungeonEditSession;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.AbstractMap;

public class DungeonCheckPoint extends LocationObject
{
	
	public DungeonCheckPoint(String type, int x, int y, int z) {
		super(type, x, y, z);
	}
	
	@Override
	public AbstractMap.SimpleEntry<String, TriConsumer<DungeonEditSession, DungeonObject, Object>> getOperation() {
		return null;
	}
}
