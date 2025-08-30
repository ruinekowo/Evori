package com.ruineko.evori

import com.google.gson.Gson
import com.ruineko.evori.commands.player.About
import com.ruineko.evori.commands.player.Fly
import com.ruineko.evori.commands.player.Kaboom
import com.ruineko.evori.commands.player.StaffChat
import com.ruineko.evori.commands.player.Node
import com.ruineko.evori.listeners.PlayerListener
import com.ruineko.evori.objects.GlobalUpdater
import com.ruineko.evori.redis.Redis
import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.yaml.YamlConfigurationLoader

class EvoriServer : JavaPlugin() {
    lateinit var configNode: ConfigurationNode
    lateinit var gson: Gson
    lateinit var redis: Redis

    override fun onEnable() {
        loadConfig()
        gson = Gson()
        redis = Redis(this)
        redis.connect()

        GlobalUpdater.start(this)

        logger.info("Registering commands...")
        registerCommand("about", About(this))
        registerCommand("fly", Fly())
        registerCommand("kaboom", Kaboom(this))
        registerCommand("node", Node(this))
        registerCommand("staffchat", StaffChat(this))

        logger.info("Registering listeners...")
        registerListener(PlayerListener(this))

        logger.info("Registering server to Redis...")
        redis.registerServer()

        logger.info("Plugin enabled")
    }

    override fun onDisable() {
        GlobalUpdater.stop()
        redis.disconnect()
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
