package com.caxerx.mc.dynamicdungeon.command.manager

import com.sk89q.worldedit.regions.Region
import org.bukkit.entity.Player

object DungeonSelectManager {
    private val selectMap: HashMap<Player, Pair<String, Region>> = HashMap()
    fun isSelected(player: Player) = player in selectMap
    fun getSelect(player: Player) = selectMap[player]
    fun select(player: Player, name: String, region: Region) = selectMap.put(player, Pair(name, region))
}