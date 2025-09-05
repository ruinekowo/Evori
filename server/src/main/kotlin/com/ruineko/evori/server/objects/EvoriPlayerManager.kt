package com.ruineko.evori.server.objects

import com.ruineko.evori.server.EvoriServer
import com.ruineko.evori.server.player.EvoriPlayer
import org.bukkit.entity.Player
import java.util.UUID

object EvoriPlayerManager {
    private val players = mutableMapOf<UUID, EvoriPlayer>()
    private val vanished = mutableSetOf<UUID>()

    private lateinit var plugin: EvoriServer

    fun init(plugin: EvoriServer) {
        this.plugin = plugin
    }

    fun get(uuid: UUID): EvoriPlayer = players.computeIfAbsent(uuid) { EvoriPlayer(plugin, it) }

    fun remove(player: Player) = players.remove(player.uniqueId)

    fun all(): Collection<EvoriPlayer> = players.values

    fun isVanished(uuid: UUID): Boolean = vanished.contains(uuid)

    fun setVanished(player: Player, vanish: Boolean) {
        if (vanish) vanished.add(player.uniqueId) else vanished.remove(player.uniqueId)
    }

    fun vanishedPlayers(): Set<UUID> = vanished.toSet()
}