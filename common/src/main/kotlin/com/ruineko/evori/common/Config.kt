package com.ruineko.evori.common

abstract class Config {
    protected lateinit var manager: ConfigManager

    fun init(manager: ConfigManager) {
        this.manager = manager
    }

    fun reload() {
        if (::manager.isInitialized) {
            manager.reload()
        }
    }
}