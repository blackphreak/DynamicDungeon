package com.caxerx.mc.lib.userinput

import org.bukkit.command.CommandExecutor
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class UIPMain : JavaPlugin(), Listener {
    companion object {
        lateinit var instance: Plugin
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(InputManager, this)
        server.getPluginCommand("uip").executor = CommandExecutor { sender, _, _, args ->
            if (sender is Player && args.size >= 2) {
                InputSession(sender, args.slice(1 until args.size).joinToString(separator = " "), args[0].split("|"), InputManager.registerPlayer(sender)).runTask(instance)
                true
            } else {
                false
            }
        }

        instance = this
    }


}