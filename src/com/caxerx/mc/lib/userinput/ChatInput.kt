package com.caxerx.mc.lib.userinput

import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.ClosedReceiveChannelException
import kotlinx.coroutines.experimental.launch
import me.blackphreak.dynamicdungeon.DynamicDungeon
import me.blackphreak.dynamicdungeon.dungeonobject.DungeonLocation
import me.blackphreak.dynamicdungeon.dungeonobject.GlobalLocation
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.function.Consumer

class ChatInput(val player: Player, val constraint: List<Pair<String, Class<*>>>, val callback: Consumer<List<String>>) : BukkitRunnable() {
    private val channel: Channel<String> = InputManager.registerPlayer(player)

    init {
        this.runTask(DynamicDungeon.plugin)
    }

    override fun run() {
        launch {
            val inputList = mutableListOf<String>()
            for ((name, clz) in constraint) {
                while (true) {
                    try {
                        player.sendMessage("Require: $name")
                        var input = channel.receive()
                        when (clz) {
                            Int::class.java -> input.toInt()
                            Double::class.java -> input.toDouble()
                            Boolean::class.java -> input.toBoolean()
                            DungeonLocation::class.java ->
                                if (input == "ok") {
                                    input = "${player.location.x},${player.location.y},${player.location.z}"
                                } else {
                                    DungeonLocation.createFromString(input)
                                }
                            GlobalLocation::class.java ->
                                if (input == "ok") {
                                    input = "${player.location.world.name},${player.location.x},${player.location.y},${player.location.z}"
                                } else {
                                    GlobalLocation.createFromString(input)
                                }
                        }
                        inputList += input
                        break
                    } catch (e: ClosedReceiveChannelException) {
                        return@launch
                    } catch (e: Exception) {
                        player.sendMessage("Error on Input")
                    }
                }
            }

            InputManager.destroyChannel(player)

            object : BukkitRunnable() {
                override fun run() {
                    callback.accept(inputList)
                }
            }.runTask(DynamicDungeon.plugin)
        }
    }

}