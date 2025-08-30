package com.ruineko.evori.listeners

import com.ruineko.evori.EvoriServer
import com.ruineko.evori.extensions.messageAll
import com.ruineko.evori.extensions.warning
import com.ruineko.evori.objects.EvoriPlayerManager
import net.kyori.adventure.text.format.NamedTextColor
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
        event.player.warning("This server is currently under heavy development. Issues may arise.")
        messageAll("${event.player.name} joined.", NamedTextColor.GREEN)

        val evoriPlayer = EvoriPlayerManager.get(event.player)
        evoriPlayer.lastJoined = System.currentTimeMillis()
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        event.quitMessage(null)
        messageAll("${event.player.name} left.", NamedTextColor.GREEN)

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