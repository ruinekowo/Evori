package com.ruineko.evori.server.redis

import com.ruineko.evori.common.AckMessage
import com.ruineko.evori.common.RegisterMessage
import com.ruineko.evori.common.StaffChatMessage
import com.ruineko.evori.common.UnregisterMessage
import com.ruineko.evori.common.extensions.string
import com.ruineko.evori.common.utils.ComponentUtils
import com.ruineko.evori.server.EvoriServer
import kotlinx.serialization.json.Json
import org.bukkit.Bukkit
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class Redis(private val plugin: EvoriServer) {
    private var hostname: String = plugin.configNode.string("server.hostname", "127.0.0.1")
    private var port: Int = plugin.server.port

    lateinit var nodeType: String
    lateinit var nodeMode: String

    fun init() {
        nodeType = plugin.configNode.string("server.type", "server")
        nodeMode = plugin.configNode.string("server.mode", "dynamic")

        plugin.redisManager.subscribe("node:sc") { raw ->
            val payload = Json.decodeFromString<StaffChatMessage>(raw)

            val uuid = payload.uuid
            val node = payload.node
            val message = payload.message

            val player = Bukkit.getPlayer(UUID.fromString(uuid))
            val name = player?.name ?: uuid

            Bukkit.getOnlinePlayers().forEach { player ->
                val nodePart = node?.let { "<gray>[$it]</gray> " } ?: ""
                player.sendMessage(ComponentUtils.parseString("<aqua>[STAFF] <yellow>[CHAT] $nodePart<red>$name<white>: $message"))
            }
        }

        plugin.redisManager.subscribe("node:ack") { raw ->
            val payload = Json.decodeFromString<AckMessage>(raw)

            if (payload.hostname == hostname && payload.port == port) {
                val serverName = payload.serverName
                plugin.serverName = payload.serverName
                plugin.logger.info("Got server name from proxy: $serverName")
            }
        }

        registerServer()
    }

    fun close() {
        val serverName = plugin.serverName

        if (serverName != null) {
            val payload = UnregisterMessage(serverName, nodeType)
            plugin.redisManager.publish("node:unregister", Json.encodeToString<UnregisterMessage>(payload))
            plugin.logger.info("Sent unregister request for server node ${plugin.serverName}")
        }

        plugin.redisManager.pool.close()
    }

    private fun registerServer() {
        val payload = RegisterMessage(hostname, port, nodeType, nodeMode)

        plugin.redisManager.publish("node:register", Json.encodeToString(payload))
        plugin.logger.info("Sent register request for $hostname:$port")
    }
}