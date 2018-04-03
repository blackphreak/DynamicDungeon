package me.blackphreak.dynamicdungeon.MapBuilding.Objects;

import java.util.AbstractMap;
import java.util.function.BiConsumer;

public class DungeonCheckPoint extends DungeonObject
{
	
	public DungeonCheckPoint(String type, int x, int y, int z) {
		super(type, x, y, z);
	}
	
	@Override
	public AbstractMap.SimpleEntry<String, BiConsumer<DungeonObject, Object>> getOperation() {
		return null;
	}
}
