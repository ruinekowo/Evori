package com.ruineko.evori.common.config

import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.yaml.NodeStyle
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

        loader = YamlConfigurationLoader.builder().file(file).nodeStyle(NodeStyle.BLOCK).build()
        root = loader.load()
    }

    fun getNode(vararg path: Any): ConfigurationNode {
        return root.node(*path)
    }

    fun getString(vararg path: Any, default: String = "undefined"): String {
        val node = getNode(*path)
        val value = node.string
        if (value.isNullOrEmpty()) {
            node.set(default)
            save()
            return default
        }
        return value
    }

    fun getInt(vararg path: Any, default: Int = 0): Int {
        val node = getNode(*path)
        var value = node.int
        if (node.virtual()) {
            node.set(default)
            save()
            value = default
        }
        return value
    }

    fun getBoolean(vararg path: Any, default: Boolean = false): Boolean {
        val node = getNode(*path)
        val value = node.boolean
        return if (node.virtual()) {
            node.set(default)
            save()
            default
        } else {
            value
        }
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
