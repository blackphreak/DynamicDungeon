package com.caxerx.mc.dynamicdungeon.command.manager

import com.sk89q.worldedit.regions.Region
import org.bukkit.entity.Player

object DungeonSelectManager {
    private val selectedDungeon: HashMap<Player, Pair<String, Region>> = HashMap()
    private val selectedTrigger: HashMap<Player, String> = HashMap()
    fun isDungeonSelected(player: Player) = player in selectedDungeon
    fun getSelectedDungeon(player: Player) = selectedDungeon[player]
    fun selectDungeon(player: Player, name: String, region: Region) = selectedDungeon.put(player, Pair(name, region))
    fun deselectDungeon(player: Player) = selectedDungeon.remove(player)
    fun isTriggerSelected(player: Player) = player in selectedTrigger
    fun getSelectedTrigger(player: Player) = selectedTrigger[player]
    fun selectTrigger(player: Player, trigger: String) = selectedTrigger.put(player, trigger)
}