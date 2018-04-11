package com.caxerx.mc.lib.userinput

import com.caxerx.mc.dynamicdungeon.command.manager.DungeonSelectManager
import com.sk89q.worldedit.Vector
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.ClosedReceiveChannelException
import kotlinx.coroutines.experimental.launch
import me.blackphreak.dynamicdungeon.DynamicDungeon
import me.blackphreak.dynamicdungeon.dungeonobject.DungeonLocation
import me.blackphreak.dynamicdungeon.dungeonobject.GlobalLocation
import me.blackphreak.dynamicdungeon.dungeonobject.OffsetLocation
import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.function.Consumer

class ChatInput(val player: Player, val constraint: List<Pair<String, Class<*>>>, val callback: Consumer<List<Any>>) : BukkitRunnable() {
    private val channel: Channel<String> = InputManager.registerPlayer(player)

    companion object {
        @JvmStatic
        fun parseObject(clz: Class<*>, input: String, player: Player): Any {
            return when (clz) {
                String::class.java -> input
                CharSequence::class.java -> ChatColor.translateAlternateColorCodes('&', input)
                Int::class.java -> input.toInt()
                Double::class.java -> input.toDouble()
                Boolean::class.java -> input.toBoolean()
                Long::class.java -> input.toLong()
                OffsetLocation::class.java -> {
                    var region = DungeonSelectManager.getSelectedDungeon(player)?.second!!
                    if (input == "ok") {
                        var location = player.location
                        if (region.contains(Vector(location.x, location.y, location.z))) {
                            OffsetLocation.createFromMinPoint(region.minimumPoint, location.x, location.y, location.z)
                        } else {
                            throw NotWithRegionException()
                        }
                    } else {
                        OffsetLocation.createFromString(input)
                    }
                }
                GlobalLocation::class.java ->
                    if (input == "ok") {
                        GlobalLocation.fromBukkitLoc(player.location)
                    } else {
                        GlobalLocation.createFromString(input)
                    }
                DungeonLocation::class.java -> {
                    if (input == "ok") {
                        var region = DungeonSelectManager.getSelectedDungeon(player)?.second
                        var location = player.location
                        if (region == null) {
                            GlobalLocation.fromBukkitLoc(player.location)
                        } else {
                            if (region.contains(Vector(location.x, location.y, location.z))) {
                                OffsetLocation.createFromMinPoint(region.minimumPoint, location.x, location.y, location.z)
                            } else {
                                GlobalLocation.fromBukkitLoc(location)
                            }
                        }
                    } else {
                        try {
                            GlobalLocation.createFromString(input)
                        } catch (e: Exception) {
                            OffsetLocation.createFromString(input)
                        }
                    }
                }
                else -> input
            }
        }
    }

    init {
        this.runTask(DynamicDungeon.plugin)
    }

    override fun run() {
        launch {
            val inputList = mutableListOf<Any>()
            for ((name, clz) in constraint) {
                var magicObject: Any? = null
                loop@ while (true) {
                    try {
                        if (magicObject == null) {
                            player.sendMessage("Require: $name")
                        } else {
                            player.sendMessage("Continue: $name")
                        }
                        var input = channel.receive()
                        when (clz) {
                            Array<String>::class.java -> {
                                if (magicObject == null) {
                                    magicObject = mutableListOf<String>()
                                }
                                var magicList = magicObject as MutableList<String>
                                if (input == ">ok<") {
                                    inputList.add(magicList.toTypedArray())
                                    magicObject = null
                                    break@loop
                                } else {
                                    magicList.add(input)
                                }
                            }
                            Array<CharSequence>::class.java -> {
                                if (magicObject == null) {
                                    magicObject = mutableListOf<String>()
                                }
                                var magicList = magicObject as MutableList<String>
                                if (input == ">ok<") {
                                    inputList.add(magicList.toTypedArray())
                                    magicObject = null
                                    break@loop
                                } else {
                                    magicList.add(ChatColor.translateAlternateColorCodes('&', input))
                                }
                            }
                            else -> inputList.add(parseObject(clz, input, player))
                        }
                        if (clz != Array<String>::class.java && clz != Array<CharSequence>::class.java) {
                            break
                        }
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