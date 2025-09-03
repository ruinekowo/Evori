package com.ruineko.evori.proxy

import com.ruineko.evori.common.ConfigManager
import com.ruineko.evori.common.RedisManager
import com.ruineko.evori.proxy.config.ProxyConfig
import com.ruineko.evori.proxy.listeners.ServerConnectListener
import com.ruineko.evori.proxy.redis.Redis
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.server.RegisteredServer
import org.slf4j.Logger
import java.nio.file.Path
import javax.inject.Inject

@Plugin(id = "evori-proxy", name = "EvoriProxy", version = "2025.0.0.1")
class EvoriProxy @Inject constructor(
    val proxyServer: ProxyServer,
    val logger: Logger,
    @param:DataDirectory private val dataDirectory: Path
) {
    var availableNode: MutableList<RegisteredServer> = mutableListOf()

    lateinit var redisManager: RedisManager
    lateinit var redis: Redis

    @Subscribe
    fun onInit(event: ProxyInitializeEvent) {
        loadConfig()

        redisManager = RedisManager(ProxyConfig.REDIS_HOSTNAME, ProxyConfig.REDIS_PORT)
        redis = Redis(this)
        redis.init()

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
        ProxyConfig.init(ConfigManager(dataDirectory.resolve("config.yml").toFile()))
    }

    fun registerListener(listener: Any) {
        proxyServer.eventManager.register(this, listener)
    }

    fun getNode(nodeId: String): RegisteredServer? {
        return availableNode.find { it.serverInfo.name == nodeId }
    }
}