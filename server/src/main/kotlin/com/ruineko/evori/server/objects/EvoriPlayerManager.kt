package com.ruineko.evori.server.objects

import com.ruineko.evori.server.player.EvoriPlayer
import org.bukkit.entity.Player
import java.util.UUID

object EvoriPlayerManager {
    private val players = mutableMapOf<UUID, EvoriPlayer>()

    fun get(uuid: UUID): EvoriPlayer {
        return players.computeIfAbsent(uuid) { EvoriPlayer(it) }
    }

    fun get(player: Player): EvoriPlayer {
        return get(player.uniqueId)
    }

    fun remove(player: Player) {
        players.remove(player.uniqueId)
    }

    fun all(): Collection<EvoriPlayer> = players.values
}