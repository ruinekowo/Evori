package com.ruineko.evori.common

import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.yaml.YamlConfigurationLoader

import java.io.File

class ConfigManager(private val file: File) {
    private val loader: YamlConfigurationLoader
    private var root: ConfigurationNode

    init {
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }

        loader = YamlConfigurationLoader.builder().file(file).build()
        root = loader.load()
    }

    fun getNode(vararg path: Any): ConfigurationNode {
        return root.node(*path)
    }

    fun getString(vararg path: Any, default: String = "undefined"): String {
        return getNode(*path).string ?: default
    }

    fun getInt(vararg path: Any, default: Int = 0): Int {
        return getNode(*path).int.takeIf { it != 0 } ?: default
    }

    fun set(value: Any?, vararg path: Any) {
        getNode(*path).set(value)
    }

    fun save() {
        loader.save(root)
    }

    fun reload() {
        root = loader.load()
    }
}
