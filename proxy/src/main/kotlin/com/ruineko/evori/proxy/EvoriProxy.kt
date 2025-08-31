package com.ruineko.evori.proxy

import com.ruineko.evori.common.AckMessage
import com.ruineko.evori.common.RedisManager
import com.ruineko.evori.common.RegisterMessage
import com.ruineko.evori.common.UnregisterMessage
import com.ruineko.evori.common.extensions.int
import com.ruineko.evori.common.extensions.string
import com.ruineko.evori.proxy.listeners.ServerConnectListener
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.server.RegisteredServer
import com.velocitypowered.api.proxy.server.ServerInfo
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.lang.Exception
import java.net.InetSocketAddress
import java.nio.file.Files
import java.nio.file.Path
import javax.inject.Inject

@Plugin(id = "evori-proxy", name = "EvoriProxy", version = "2025.0.0.1")
class EvoriProxy @Inject constructor(
    private val proxyServer: ProxyServer,
    private val logger: Logger,
    @param:DataDirectory private val dataDirectory: Path
) {
    var availableNode: MutableList<RegisteredServer> = mutableListOf()

    lateinit var redis: RedisManager
    lateinit var configNode: ConfigurationNode

    @Subscribe
    fun onInit(event: ProxyInitializeEvent) {
        loadConfig()

        redis = RedisManager(configNode.string("redis.host", "127.0.0.1"), configNode.int("redis.port", 6379))

        redis.subscribe("node:register") { raw ->
            val payload = Json.decodeFromString<RegisterMessage>(raw)

            val index = redis.pool.resource.incr("node:index:${payload.type}")
            val serverName = "${payload.mode}${payload.type}$index"

            redis.hset("node:$serverName", mapOf(
                    "serverName" to serverName,
                    "hostname" to payload.hostname,
                    "port" to payload.port.toString(),
                    "type" to payload.type,
                    "mode" to payload.mode,
                    "registeredAt" to System.currentTimeMillis().toString()
                )
            )

            val serverInfo = ServerInfo(serverName, InetSocketAddress(payload.hostname, payload.port))
            val registered = proxyServer.registerServer(serverInfo)
            availableNode.add(registered)

            val ack = AckMessage(serverName, payload.hostname, payload.port)
            redis.publish("node:ack", Json.encodeToString(ack))

            logger.info("Registered node $serverName at ${payload.hostname}:${payload.port}")
        }

        redis.subscribe("node:unregister") { raw ->
            val payload = Json.decodeFromString<UnregisterMessage>(raw)
            val serverName = payload.serverName

            redis.del("node:$serverName")

            availableNode.find { it.serverInfo.name == serverName }?.let { server ->
                proxyServer.unregisterServer(server.serverInfo)
                availableNode.remove(server)
                redis.pool.resource.del("node:index:${payload.type}")
            }

            logger.info("Unregistered node $serverName")
        }

        // Register listeners
        registerListener(ServerConnectListener(this))

        logger.info("Plugin enabled!")
    }

    @Subscribe
    fun onShutDown(event: ProxyShutdownEvent) {
        redis.close()
        logger.info("Plugin disabled!")
    }

    fun loadConfig() {
        try {
            val configFile = dataDirectory.resolve("config.yml")
            if (!Files.exists(configFile)) {
                Files.createDirectory(dataDirectory)
                javaClass.getResourceAsStream("./config.yml")?.use { input ->
                    Files.copy(input, configFile)
                }
            }

            val loader = YamlConfigurationLoader.builder()
                .path(configFile)
                .build()

            configNode = loader.load()
        } catch (e: Exception) {
            logger.error(e.message)
        }
    }

    fun registerListener(listener: Any) {
        proxyServer.eventManager.register(this, listener)
    }

    fun getNode(nodeId: String): RegisteredServer? {
        return availableNode.find { it.serverInfo.name == nodeId }
    }
}