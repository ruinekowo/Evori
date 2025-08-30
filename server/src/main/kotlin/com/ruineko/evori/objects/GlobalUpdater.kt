package com.ruineko.evori.objects

import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

object GlobalUpdater {
    private var task: BukkitRunnable? = null

    fun start(plugin: JavaPlugin) {
        if (task != null) return

        task = object : BukkitRunnable() {
            override fun run() {
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