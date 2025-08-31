package com.ruineko.evori.server

import com.google.gson.Gson
import com.ruineko.evori.common.RedisManager
import com.ruineko.evori.common.extensions.int
import com.ruineko.evori.common.extensions.string
import com.ruineko.evori.server.commands.player.About
import com.ruineko.evori.server.commands.player.Fly
import com.ruineko.evori.server.commands.player.Kaboom
import com.ruineko.evori.server.commands.player.Node
import com.ruineko.evori.server.commands.player.StaffChat
import com.ruineko.evori.server.listeners.PlayerListener
import com.ruineko.evori.server.objects.GlobalUpdater
import com.ruineko.evori.server.redis.Redis
import kotlinx.serialization.json.Json
import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.yaml.YamlConfigurationLoader

class EvoriServer : JavaPlugin() {
    val json = Json { ignoreUnknownKeys = true }

    lateinit var configNode: ConfigurationNode
    lateinit var gson: Gson
    lateinit var redisManager: RedisManager
    lateinit var redis: Redis

    override fun onEnable() {
        loadConfig()
        gson = Gson()
        redisManager = RedisManager(configNode.string("redis.host"), configNode.int("redis.port"))
        redis = Redis(this)
        redis.init()

        GlobalUpdater.start(this)

        logger.info("Registering commands...")
        registerCommand("about", About(this))
        registerCommand("fly", Fly())
        registerCommand("kaboom", Kaboom(this))
        registerCommand("node", Node(this))
        registerCommand("staffchat", StaffChat(this))

        logger.info("Registering listeners...")
        registerListener(PlayerListener(this))

        logger.info("Plugin enabled")
    }

    override fun onDisable() {
        GlobalUpdater.stop()
        redis.close()
        logger.info("Plugin disabled")
    }

    fun JavaPlugin.registerCommand(name: String, executor: CommandExecutor) {
        getCommand(name)?.setExecutor(executor) ?: logger.warning("Command $name is not defined in plugin.yml")
    }

    fun JavaPlugin.registerListener(listener: Listener) {
        server.pluginManager.registerEvents(listener, this)
    }

    fun loadConfig() {
        saveDefaultConfig()

        val loader = YamlConfigurationLoader.builder()
            .path(dataFolder.toPath().resolve("config.yml"))
            .build()

        configNode = loader.load()
    }
}
