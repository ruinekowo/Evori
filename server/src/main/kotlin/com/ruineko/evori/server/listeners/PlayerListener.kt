package com.ruineko.evori.server.listeners

import com.ruineko.evori.common.utils.ComponentUtils
import com.ruineko.evori.server.EvoriServer
import com.ruineko.evori.server.extensions.messageAll
import com.ruineko.evori.server.objects.EvoriPlayerManager
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerListener(private val plugin: EvoriServer) : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        event.joinMessage(null)
        event.player.sendMessage("") // Empty message to separate chat
        messageAll("<yellow>${event.player.name} joined.")

        val evoriPlayer = EvoriPlayerManager.get(event.player.uniqueId)
        evoriPlayer.lastJoined = System.currentTimeMillis()
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        event.quitMessage(null)
        messageAll("<yellow>${event.player.name} left.")

        EvoriPlayerManager.remove(event.player)
    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        if (event.entity is Player && event.cause == EntityDamageEvent.DamageCause.FALL) {
            val player = event.entity as Player
            if (player.hasMetadata("kaboom")) {
                event.isCancelled = true
                player.velocity = player.velocity.setY(0)
                player.removeMetadata("kaboom", plugin)
            }
        }
    }
}