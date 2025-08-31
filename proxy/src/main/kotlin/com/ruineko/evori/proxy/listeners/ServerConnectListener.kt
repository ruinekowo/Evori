package com.ruineko.evori.proxy.listeners

import com.ruineko.evori.common.utils.ComponentUtils
import com.ruineko.evori.proxy.EvoriProxy
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.ServerPreConnectEvent

class ServerConnectListener(private val plugin: EvoriProxy) {
    @Subscribe
    fun onServerPreConnect(event: ServerPreConnectEvent) {
        val player = event.player

        if (plugin.availableNode.isEmpty()) {
            event.result = ServerPreConnectEvent.ServerResult.denied()
            player.disconnect(ComponentUtils.parseString("<red>No servers available."))
            return
        }

        val selectedNode = plugin.availableNode.random()
        event.result = ServerPreConnectEvent.ServerResult.allowed(selectedNode)
        player.sendMessage(ComponentUtils.parseString("<green>Sending you to ${selectedNode.serverInfo.name}!"))
    }
}