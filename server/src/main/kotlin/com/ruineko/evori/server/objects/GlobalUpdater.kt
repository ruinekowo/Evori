package com.ruineko.evori.server.objects

import com.ruineko.evori.server.EvoriServer
import org.bukkit.scheduler.BukkitRunnable

object GlobalUpdater {
    private var task: BukkitRunnable? = null

    fun start(plugin: EvoriServer) {
        if (task != null) return

        task = object : BukkitRunnable() {
            override fun run() {
                if (plugin.serverName == null) return

                EvoriPlayerManager.all().forEach {
                    it.actionBarManager.update()
                }
            }
        }.also {
            it.runTaskTimer(plugin, 0L, 20L)
        }
    }

    fun stop() {
        task?.cancel()
        task = null
    }
}