package com.caxerx.mc.dynamicdungeon.command.manager

import com.caxerx.mc.dynamicdungeon.dungeonobject.DungeonObject

object DungeonManager {
    private val dungeonMap: HashMap<String, List<DungeonObject>> = HashMap()
    fun getDungeon(dungeonName: String) = dungeonMap[dungeonName]
    fun createDungeon(dungeonName: String) = dungeonMap.put(dungeonName, mutableListOf())
}