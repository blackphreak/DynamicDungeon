package com.caxerx.mc.lib.userinput

import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.ClosedReceiveChannelException
import kotlinx.coroutines.experimental.launch
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class InputSession(val player: Player, val cmd: String, val input: List<String>, val channel: Channel<String>) : BukkitRunnable() {
    override fun run() {
        player.sendMessage("" + ChatColor.YELLOW + "已經進入指令模式 輸入" + ChatColor.AQUA + "**" + ChatColor.YELLOW + "取消執行指令")
        launch {
            var cm = cmd
            var ils = mutableListOf<String>()
            try {
                for (i in input) {
                    player.sendMessage("" + ChatColor.YELLOW + "請輸入 " + ChatColor.AQUA + i.replace("_", " "))
                    ils.add(channel.receive())
                }
                object : BukkitRunnable() {
                    override fun run() {
                        player.performCommand(cm.format(*ils.toTypedArray()))
                    }
                }.runTask(UIPMain.instance)

            } catch (e: ClosedReceiveChannelException) {
                player.sendMessage("" + ChatColor.RED + "指令已取消")
            } finally {
                InputManager.destroyChannel(player)
            }
        }

    }
}