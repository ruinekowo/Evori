package com.ruineko.evori.player

import com.ruineko.evori.player.managers.ActionBarManager
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import java.util.UUID

class EvoriPlayer(val uuid: UUID) {
    val actionBarManager = ActionBarManager(this)

    var lastJoined: Long = 0

    fun base(): Player? = Bukkit.getPlayer(uuid)
}