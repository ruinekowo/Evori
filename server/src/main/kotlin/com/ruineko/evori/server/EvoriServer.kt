package com.ruineko.evori.server

import com.google.gson.Gson
import com.ruineko.evori.common.RedisManager
import com.ruineko.evori.common.ConfigManager
import com.ruineko.evori.server.commands.admin.Reload
import com.ruineko.evori.server.commands.player.About
import com.ruineko.evori.server.commands.player.Fly
import com.ruineko.evori.server.commands.player.Kaboom
import com.ruineko.evori.server.commands.player.Node
import com.ruineko.evori.server.commands.player.StaffChat
import com.ruineko.evori.server.config.ChatConfig
import com.ruineko.evori.server.config.ServerConfig
import com.ruineko.evori.server.listeners.ChatListener
import com.ruineko.evori.server.listeners.PlayerListener
import com.ruineko.evori.server.objects.GlobalUpdater
import com.ruineko.evori.server.redis.Redis
import kotlinx.serialization.json.Json
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class EvoriServer : JavaPlugin() {
    val json = Json { ignoreUnknownKeys = true }
    var serverName: String? = null

    lateinit var gson: Gson
    lateinit var redisManager: RedisManager
    lateinit var redis: Redis

    override fun onEnable() {
        loadConfigurations()
        gson = Gson()

        redisManager = RedisManager(ServerConfig.REDIS_HOSTNAME, ServerConfig.REDIS_PORT)
        redis = Redis(this)
        redis.init()

        GlobalUpdater.start(this)

        logger.info("Unregistering commands")
        unregisterCommand("plugins", "version", "help", "reload")

        logger.info("Registering commands")
        // Player commands
        registerCommand("about", About(this))
        registerCommand("fly", Fly())
        registerCommand("kaboom", Kaboom(this))
        registerCommand("node", Node(this))
        registerCommand("staffchat", StaffChat(this))

        // Admin commands
        registerCommand("reload", Reload(this))

        logger.info("Registering listeners")
        registerListener(ChatListener())
        registerListener(PlayerListener(this))

        logger.info("Plugin enabled")
    }

    override fun onDisable() {
        GlobalUpdater.stop()
        redis.close()
        logger.info("Plugin disabled")
    }

    fun unregisterCommand(vararg commands: String) {
        val commandMap = Bukkit.getCommandMap()
        for (command in commands) {
            commandMap.getCommand(command)?.unregister(commandMap)
            logger.info("Unregistered command $command")
        }
    }

    fun registerCommand(name: String, executor: CommandExecutor) {
        val command = getCommand(name)
        if (command != null) {
            command.setExecutor(executor)
            logger.info("Registered command $name")
        }
    }

    fun registerListener(listener: Listener) {
        server.pluginManager.registerEvents(listener, this)
    }

    fun loadConfigurations() {
        ServerConfig.init(ConfigManager(dataFolder.resolve("config.yml")))
        ChatConfig.init(ConfigManager(dataFolder.resolve("chat.yml")))
    }

    fun reloadConfigurations() {
        ServerConfig.reload()
        ChatConfig.reload()
    }
}
