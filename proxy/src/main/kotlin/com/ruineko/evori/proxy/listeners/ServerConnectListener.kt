package com.ruineko.evori.proxy.listeners

import com.ruineko.evori.common.utils.ComponentUtils
import com.ruineko.evori.proxy.EvoriProxy
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.ServerPreConnectEvent

class ServerConnectListener(private val plugin: EvoriProxy) {
    @Subscribe
    fun onServerPreConnect(event: ServerPreConnectEvent) {
        if (plugin.availableNode.isEmpty()) return

        val player = event.player
        val selectedNode = plugin.availableNode.random()
        event.result = ServerPreConnectEvent.ServerResult.allowed(selectedNode)
        player.sendMessage(ComponentUtils.parse("<green>Sending you to server ${selectedNode.serverInfo.name}!"))
    }
}