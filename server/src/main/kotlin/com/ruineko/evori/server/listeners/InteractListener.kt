package com.ruineko.evori.server.listeners

import com.ruineko.evori.server.config.MessageConfig
import com.ruineko.evori.server.extensions.error
import com.ruineko.evori.server.objects.EvoriPlayerManager
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent

class InteractListener: Listener {
    @EventHandler
    fun onPlayerInteractAtEntity(event: PlayerInteractAtEntityEvent) {
        val rightClicked = event.rightClicked as? Player ?: return
        val player = event.player
        val evoriPlayer = EvoriPlayerManager.get(player.uniqueId)

        evoriPlayer.mount(EvoriPlayerManager.get(rightClicked.uniqueId), evoriPlayer.maxStackHeight) {
            player.error(MessageConfig.MOUNT_LIMIT_REACHED)
        }
    }
}