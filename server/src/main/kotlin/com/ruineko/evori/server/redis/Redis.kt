package com.ruineko.evori.server.redis

import com.ruineko.evori.common.AckMessage
import com.ruineko.evori.common.RegisterMessage
import com.ruineko.evori.common.StaffChatMessage
import com.ruineko.evori.common.utils.ComponentUtils
import com.ruineko.evori.server.EvoriServer
import kotlinx.serialization.json.Json
import org.bukkit.Bukkit
import java.net.InetAddress
import java.util.*

class Redis(private val plugin: EvoriServer) {
    var nodeId: String? = null

    fun init() {
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

            if (payload.ip == InetAddress.getLocalHost().hostAddress && payload.port == plugin.server.port) {
                nodeId = payload.id
                plugin.logger.info("Got node ID from proxy: $nodeId")
            }
        }

        registerServer()
    }

    fun close() {
        plugin.redisManager.pool.close()
        plugin.logger.info("Unregistered node $nodeId")
    }

    private fun registerServer() {
        val ip = InetAddress.getLocalHost().hostAddress
        val port = plugin.server.port
        val payload = RegisterMessage(ip, port)

        plugin.redisManager.publish("node:register", Json.encodeToString(payload))
        plugin.logger.info("Sent register request for $ip:$port")
    }
}