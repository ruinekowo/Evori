package com.ruineko.evori.extensions

import org.spongepowered.configurate.ConfigurationNode

fun ConfigurationNode.int(path: String, default: Int = 0): Int =
    this.node(*path.split(".").toTypedArray()).getInt(default)

fun ConfigurationNode.string(path: String, default: String = "Undefined"): String =
    this.node(*path.split(".").toTypedArray()).getString(default)

fun ConfigurationNode.boolean(path: String, default: Boolean = false): Boolean =
    this.node(*path.split(".").toTypedArray()).getBoolean(default)