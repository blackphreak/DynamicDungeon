package com.caxerx.mc.lib.userinput

import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch
import me.blackphreak.dynamicdungeon.DynamicDungeon
import me.blackphreak.dynamicdungeon.Messages.db
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerQuitEvent

object InputManager : Listener {
    val playerMap = hashMapOf<Player, Channel<String>>()

    fun registerPlayer(player: Player): Channel<String> {
        if (player in playerMap) {
            destroyChannel(player)
        }
        playerMap[player] = Channel()
        return playerMap[player]!!
    }

    fun destroyChannel(player: Player) {
        if (player in playerMap) {
            playerMap[player]!!.close()
            playerMap.remove(player)
        }
    }

    @EventHandler
    fun onChat(e: AsyncPlayerChatEvent) {
        if (e.player in playerMap) {
            if (e.message == "**") {
                destroyChannel(e.player)
                e.isCancelled = true
                return
            }
            launch {
                val channel = playerMap[e.player]!!
                channel.send(e.message)
            }
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onLeave(e: PlayerQuitEvent) {
        if (e.player in playerMap) {
            destroyChannel(e.player)
        }
    }
}