package com.ruineko.evori.server.listeners

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import com.ruineko.evori.server.EvoriServer
import com.ruineko.evori.server.objects.EvoriPlayerManager

class VanishPacketListener(plugin: EvoriServer) : PacketAdapter(
    plugin,
    ListenerPriority.NORMAL,
    PacketType.Play.Server.PLAYER_INFO,
) {
    override fun onPacketSending(event: PacketEvent) {
        val packet = event.packet

        if (event.packetType == PacketType.Play.Server.PLAYER_INFO) {
            val infos = packet.playerInfoDataLists.read(0)
            val filtered = infos.filter { !EvoriPlayerManager.isVanished(it.profile.uuid) }
            if (filtered.size != infos.size) {
                if (filtered.isEmpty()) {
                    event.isCancelled = true
                } else {
                    packet.playerInfoDataLists.write(0, filtered)
                }
            }
        }
    }
}