package com.ruineko.evori.server.player

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.ruineko.evori.server.EvoriServer
import com.ruineko.evori.server.player.managers.ActionBarManager
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.UUID

class EvoriPlayer(val uuid: UUID) {
    val actionBarManager = ActionBarManager(this)

    var lastJoined: Long = 0
    var isVanished: Boolean = false
    var maxStackHeight: Int = 1

    fun base(): Player? = Bukkit.getPlayer(uuid)

    fun mount(target: EvoriPlayer, maxHeight: Int, onLimitReached: () -> Unit) {
        var current = base() as Entity
        var height = 0

        while (current.passengers.isNotEmpty()) {
            current = current.passengers[0]
            height++
        }

        if (height <= maxHeight) {
            target.base()?.addPassenger(current)
        } else {
            onLimitReached()
        }
    }

    fun unmount() {
        val player = base() ?: return
        val passenger = player.passengers.firstOrNull() as? Player ?: return
        player.removePassenger(passenger)
    }

    fun disappear(plugin: EvoriServer) {
        val self = base() ?: return
        isVanished = true

        plugin.server.onlinePlayers.forEach { other ->
            if (other.uniqueId != uuid) {
                val removePacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO_REMOVE)
                removePacket.uuidLists.write(0, listOf(uuid))
                ProtocolLibrary.getProtocolManager().sendServerPacket(other, removePacket)

                val destroyPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_DESTROY)
                destroyPacket.intLists.write(0, listOf(self.entityId))
                ProtocolLibrary.getProtocolManager().sendServerPacket(other, destroyPacket)
            }
        }
    }

    fun reappear(plugin: EvoriServer) {
        val self = base() ?: return
        isVanished = false

        plugin.server.onlinePlayers.forEach { other ->
            if (other.uniqueId != uuid) {
                other.showPlayer(plugin, self)
            }
        }
    }
}