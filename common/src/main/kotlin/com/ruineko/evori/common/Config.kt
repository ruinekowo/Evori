package com.ruineko.evori.common

import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

abstract class Config {
    protected lateinit var manager: ConfigManager

    fun init(manager: ConfigManager) {
        this.manager = manager

        this::class.memberProperties.forEach { property ->
            @Suppress("UNCHECKED_CAST")
            val kProperty = property as KProperty1<Any, *>
            kProperty.get(this)
        }
    }

    fun reload() {
        if (::manager.isInitialized) {
            manager.reload()
        }
    }
}