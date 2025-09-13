package com.ruineko.evori.server.listeners

import com.ruineko.evori.server.objects.EvoriPlayerManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class MovementListeners: Listener {
    @EventHandler
    fun onMove(event: PlayerMoveEvent) {
        val player = event.player
        val evoriPlayer = EvoriPlayerManager.get(player.uniqueId)

        if (player.isSneaking) {
            evoriPlayer.unmount()
        }
    }
}