package com.ruineko.evori.server.redis

import com.ruineko.evori.common.AckMessage
import com.ruineko.evori.common.RegisterMessage
import com.ruineko.evori.common.StaffChatMessage
import com.ruineko.evori.common.UnregisterMessage
import com.ruineko.evori.common.utils.ComponentUtils
import com.ruineko.evori.server.EvoriServer
import com.ruineko.evori.server.config.ServerConfig
import kotlinx.serialization.json.Json
import org.bukkit.Bukkit
import java.util.*

class Redis(private val plugin: EvoriServer) {
    private var serverHostname: String = ServerConfig.SERVER_HOSTNAME
    private var serverPort: Int = plugin.server.port
    private var nodeType: String = ServerConfig.SERVER_TYPE

    fun init() {
        subscribe()
        registerServer()
    }

    fun close() {
        val serverName = plugin.serverName

        if (serverName != null) {
            val payload = UnregisterMessage(serverName, nodeType)
            plugin.redisManager.publish("node:unregister", Json.encodeToString<UnregisterMessage>(payload))
            plugin.logger.info("Sent unregister request for node ${plugin.serverName}")
        }

        plugin.redisManager.pool.close()
    }

    private fun registerServer() {
        val payload = RegisterMessage(serverHostname, serverPort, nodeType)

        plugin.redisManager.publish("node:register", Json.encodeToString(payload))
        plugin.logger.info("Sent register request for $serverHostname:$serverPort")
    }

    private fun subscribe() {
        plugin.redisManager.subscribe("node:sc") { raw ->
            val payload = Json.decodeFromString<StaffChatMessage>(raw)

            val uuid = payload.uuid
            val node = payload.node
            val message = payload.message

            val player = Bukkit.getPlayer(UUID.fromString(uuid))
            val name = player?.name ?: uuid

            Bukkit.getOnlinePlayers().forEach { player ->
                val nodePart = node?.let { "<gray>[$it]</gray> " } ?: ""
                player.sendMessage(ComponentUtils.parse("<aqua>[STAFF] <yellow>[CHAT] $nodePart<red>$name<white>: $message"))
            }
        }

        plugin.redisManager.subscribe("node:ack") { raw ->
            val payload = Json.decodeFromString<AckMessage>(raw)

            if (payload.hostname == serverHostname && payload.port == serverPort) {
                val serverName = payload.serverName
                plugin.serverName = payload.serverName
                plugin.logger.info("Got server name from proxy: $serverName")
            }
        }
    }
}