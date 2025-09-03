package com.ruineko.evori.proxy.redis

import com.ruineko.evori.common.AckMessage
import com.ruineko.evori.common.RegisterMessage
import com.ruineko.evori.common.UnregisterMessage
import com.ruineko.evori.common.utils.StringUtils
import com.ruineko.evori.proxy.EvoriProxy
import com.velocitypowered.api.proxy.server.ServerInfo
import kotlinx.serialization.json.Json
import java.net.InetSocketAddress

class Redis(private val plugin: EvoriProxy) {
    fun init() {
        plugin.redisManager.subscribe("node:register") { raw ->
            val payload = Json.decodeFromString<RegisterMessage>(raw)

            val serverId = StringUtils.randomId(6)
            val serverName = "${payload.type}$serverId"

            plugin.redisManager.hset("node:$serverName", mapOf(
                "id" to serverId,
                "serverName" to serverName,
                "hostname" to payload.hostname,
                "port" to payload.port.toString(),
                "type" to payload.type,
                "registeredAt" to System.currentTimeMillis().toString())
            )

            val serverInfo = ServerInfo(serverName, InetSocketAddress(payload.hostname, payload.port))
            val registered = plugin.proxyServer.registerServer(serverInfo)
            plugin.availableNode.add(registered)

            val ack = AckMessage(serverName, payload.hostname, payload.port)
            plugin.redisManager.publish("node:ack", Json.encodeToString(ack))

            plugin.logger.info("Registered node $serverName at ${payload.hostname}:${payload.port}")
        }

        plugin.redisManager.subscribe("node:unregister") { raw ->
            val payload = Json.decodeFromString<UnregisterMessage>(raw)
            val serverName = payload.serverName

            plugin.redisManager.del("node:$serverName")

            plugin.availableNode.find { it.serverInfo.name == serverName }?.let { server ->
                plugin.proxyServer.unregisterServer(server.serverInfo)
                plugin.availableNode.remove(server)
                plugin.redisManager.del("node:index:${payload.type}")
            }

            plugin.logger.info("Unregistered node $serverName")
        }
    }

    fun close() {
        plugin.redisManager.close()
    }
}