package com.ruineko.evori.proxy.listeners

import com.ruineko.evori.common.utils.PlaceholderFormatter
import com.ruineko.evori.proxy.EvoriProxy
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.ServerPreConnectEvent
import net.kyori.adventure.text.Component

class ServerConnectListener(private val plugin: EvoriProxy) {
    @Subscribe
    fun onServerPreConnect(event: ServerPreConnectEvent) {
        val player = event.player

        if (plugin.availableNode.isEmpty()) {
            player.disconnect(Component.text(PlaceholderFormatter.format("<c>There are no available nodes. Please try again later.")))
            return
        }

        val selectedNode = plugin.availableNode.random()
        event.result = ServerPreConnectEvent.ServerResult.allowed(selectedNode)
        player.sendMessage(Component.text(PlaceholderFormatter.format("<8>Sending you to ${selectedNode.serverInfo.name}...")))
    }
}