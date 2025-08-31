package com.ruineko.evori.server.objects

import com.ruineko.evori.common.utils.ComponentUtils
import com.ruineko.evori.server.EvoriServer
import org.bukkit.scheduler.BukkitRunnable

object GlobalUpdater {
    private var task: BukkitRunnable? = null

    fun start(plugin: EvoriServer) {
        if (task != null) return
        if (plugin.redis.nodeId == null) return

        task = object : BukkitRunnable() {
            override fun run() {
                EvoriPlayerManager.all().forEach {
                    it.actionBarManager.message = ComponentUtils.parseString("<green>You are currently on node ${plugin.redis.nodeId}")
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