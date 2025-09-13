package com.ruineko.evori.server.objects

import com.ruineko.evori.server.EvoriServer
import com.ruineko.evori.server.extensions.success
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

    fun get(uuid: UUID): EvoriPlayer = players.computeIfAbsent(uuid) { EvoriPlayer(it) }

    fun remove(player: Player) = players.remove(player.uniqueId)

    fun all(): Collection<EvoriPlayer> = players.values

    fun isVanished(uuid: UUID): Boolean = vanished.contains(uuid)

    fun setVanished(player: Player, vanish: Boolean) {
        val evoriPlayer = get(player.uniqueId)

        if (vanish) {
            vanished.add(player.uniqueId)
            evoriPlayer.disappear(plugin)
            evoriPlayer.actionBarManager.setMessage("<white>You are currently <RED>DISAPPEARING")
            evoriPlayer.base()?.success("You have disappeared!")
        } else {
            vanished.remove(player.uniqueId)
            evoriPlayer.reappear(plugin)
            evoriPlayer.actionBarManager.clearMessage()
            evoriPlayer.base()?.success("You have reappeared!")
        }
    }

    fun vanishedPlayers(): Set<UUID> = vanished.toSet()
}