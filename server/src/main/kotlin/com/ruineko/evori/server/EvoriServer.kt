package com.ruineko.evori.server

import com.comphenix.protocol.ProtocolLibrary
import com.google.gson.Gson
import com.ruineko.evori.common.redis.RedisManager
import com.ruineko.evori.common.config.ConfigManager
import com.ruineko.evori.server.commands.admin.Reload
import com.ruineko.evori.server.commands.player.About
import com.ruineko.evori.server.commands.player.Fly
import com.ruineko.evori.server.commands.player.Kaboom
import com.ruineko.evori.server.commands.player.Node
import com.ruineko.evori.server.commands.player.StaffChat
import com.ruineko.evori.server.commands.player.Vanish
import com.ruineko.evori.server.config.ChatConfig
import com.ruineko.evori.server.config.MessageConfig
import com.ruineko.evori.server.config.ServerConfig
import com.ruineko.evori.server.listeners.ChatListener
import com.ruineko.evori.server.listeners.InteractListener
import com.ruineko.evori.server.listeners.PlayerListener
import com.ruineko.evori.server.listeners.VanishPacketListener
import com.ruineko.evori.server.objects.EvoriPlayerManager
import com.ruineko.evori.server.objects.GlobalUpdater
import com.ruineko.evori.server.redis.Redis
import kotlinx.serialization.json.Json
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.lang.management.ManagementFactory

class EvoriServer : JavaPlugin() {
    val startTime = ManagementFactory.getRuntimeMXBean().startTime

    val json = Json { ignoreUnknownKeys = true }
    var serverName: String? = null

    var protocolLibAvailable = checkPlugin("ProtocolLib")

    lateinit var gson: Gson
    lateinit var redisManager: RedisManager
    lateinit var redis: Redis

    companion object {
        lateinit var instance: EvoriServer
            private set
    }

    override fun onEnable() {
        instance = this

        loadConfigurations()
        gson = Gson()

        redisManager = RedisManager(ServerConfig.REDIS_HOSTNAME, ServerConfig.REDIS_PORT)
        redis = Redis(this)
        redis.init()

        EvoriPlayerManager.init(this)
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
        registerCommand("vanish", Vanish(this))

        // Admin commands
        registerCommand("reload", Reload(this))

        logger.info("Registering listeners")
        registerListener(ChatListener())
        registerListener(InteractListener())
        registerListener(PlayerListener(this))

        if (protocolLibAvailable) {
            logger.info("Registering packet listeners (ProtocolLib)")
            val protocolManager = ProtocolLibrary.getProtocolManager()
            protocolManager.addPacketListener(VanishPacketListener(this))
        }

        logger.info(MessageConfig.PLUGIN_ENABLED)
    }

    override fun onDisable() {
        GlobalUpdater.stop()
        redis.close()
        logger.info(MessageConfig.PLUGIN_DISABLED)
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

    fun checkPlugin(name: String): Boolean {
        val plugin = server.pluginManager.getPlugin(name)
        return if (plugin != null && plugin.isEnabled) {
            logger.info("$name found and enabled")
            true
        } else {
            logger.warning("$name not found or not enabled")
            false
        }
    }

    fun loadConfigurations() {
        ServerConfig.init(ConfigManager(dataFolder.resolve("config.yml")))
        ChatConfig.init(ConfigManager(dataFolder.resolve("chat.yml")))
        MessageConfig.init(ConfigManager(dataFolder.resolve("message.yml")))
    }

    fun reloadConfigurations() {
        ServerConfig.reload()
        ChatConfig.reload()
        MessageConfig.reload()
    }
}
